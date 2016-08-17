package org.iadb.poolpartyconnector.changepropagation

import java.time.Instant
import java.time.temporal.ChronoUnit

import akka.actor.{ActorLogging, ActorRef, Props}
import akka.persistence._
import org.iadb.poolpartyconnector.changepropagation.ChangeFetcherActor.{FetchNewChangeSet, NewChangeSet, NoAvailableChangeSet}

import scala.concurrent.duration._
import com.softwaremill.tagging._


/**
  * Created by Daniel Maatari Okouya on 5/25/16.
  */

object ChangePropagationActor {


  sealed trait Command
  case object FetchChangeSet extends Command
  private case object HandleChgtPropagationEnd extends Command
  private case object PropagateChange extends Command
  private case object GetBusy extends Command
  private case object CreatePropagationOps extends Command

  sealed trait Event
  case class PropOpsPropagated(propOps: PropagationOp) extends Event {
    override def toString: String = {

      propOps match {
        case ConceptMergePropagation(subjectOfChangeURI, mergingConceptURI, historyTime) => {
          "\nPropOpsPropagated:ConceptMergePropagation:\n{\n" +
            s"subjectOfChangeURI: ${subjectOfChangeURI}\n" +
            s"mergingConceptURI: ${mergingConceptURI}\n" +
            s"historyTime: ${historyTime}\n}"
        }
        case ConceptUpdatePropagation(subjectOfChangeURI, historyTime) => {
          "\nPropOpsPropagated:ConceptUpdatePropagation:\n{\n" +
            s"subjectOfChangeURI: ${subjectOfChangeURI}\n" +
            s"historyTime: ${historyTime}\n}"
        }
      }
    }
  }
  case class ChangeSetFetched(changeSet: List[ChangeEvent]) extends Event {
    override def toString: String = {
      "\nChangeSetFetched{\n" +
        changeSet.foldLeft("") {(mystring,event) => mystring + s" ${event}\n"} +
        "}"
    }
  }
  case class PropagationOpsCreated(propOps: List[PropagationOp]) extends Event {
    override def toString: String = {
      "\nPropagationOpsCreated{\n" +
        propOps.foldLeft("") { (mystring, event) => mystring + s" ${event}\n" } +
        "}"
    }
  }
  case object ACK


  def props(chgtWriter: ActorRef @@ Writer , chgtFetcher: ActorRef @@ Fetcher, fromTime: Instant, requestInterval: FiniteDuration) = Props(new ChangePropagationActor(chgtWriter, chgtFetcher, fromTime, requestInterval))

}


case class State(changeSet: Option[List[ChangeEvent]], eventPropOps: Option[List[PropagationOp]], lastChangeTime:Instant = Instant.now().truncatedTo(ChronoUnit.SECONDS)) {

  def addChangeSet(achangeSet: Option[List[ChangeEvent]]) = copy(changeSet = achangeSet, eventPropOps = None)

  def addEventPropOps(aeventPropOps: Option[List[PropagationOp]]) = copy(changeSet = None, eventPropOps = aeventPropOps)

  def updatePropOps(aeventPropOps: Option[List[PropagationOp]], alastChangeTime:Instant ) = copy(eventPropOps = aeventPropOps, lastChangeTime = alastChangeTime)

  override def toString: String = {
    "\nState:{\n" +
      changeSet.fold(" None\n"){_.foldLeft("") {(mystring,event) => mystring + s" ${event}\n"}}  +
      eventPropOps.fold(" None\n"){_.foldLeft("") {(mystring,event) => mystring + s" ${event}\n"}} +
      " lastChangeTime: " + lastChangeTime.toString + "\n}"
  }

}


class ChangePropagationActor(chgtWriter: ActorRef @@ Writer, chgtFetcher: ActorRef @@ Fetcher, fromTime: Instant, requestInterval: FiniteDuration) extends PersistentActor with ActorLogging {

  import context._
  import org.iadb.poolpartyconnector.changepropagation.ChangePropagationActor._


  val persistenceId: String = "ChangePropagationActor"
  private var state: State = State(None, None, fromTime)

  ScheduleNextFetch()



  def receiveCommand: Receive = {

    case GetBusy => state match {
      case State(None, None, _)            => log.info("Getting Busy, State is empty, starting a new Fetch of Changes"); self ! FetchChangeSet; become(receiveFetchCommand)
      case State(None, Some(propOps), _)   => log.info("Getting Busy, state is non empty, resuming change Propagation"); self ! PropagateChange; become(receivePropagationCommand)
      case State(Some(changeset), None, _) => log.info("Getting Busy, state is non empty, creating Propagation Operations"); self ! CreatePropagationOps; become(receiveCreatePropagationOps)
      case _ => log.info("Terrible situation should be restarting, send something to the supervisor for restart");
    }

    case HandleChgtPropagationEnd              => log.info("Closing late ChangeSetFetch: Performing a snapshot of the state"); saveSnapshot(state)

    case SaveSnapshotSuccess(metadata)         =>
                                                  deleteSnapshots(SnapshotSelectionCriteria(metadata.sequenceNr - 1))
                                                  deleteMessages(metadata.sequenceNr)
                                                  ScheduleNextFetch()
                                                  log.info("Snapshot of the State was take with success old Event & Snapshot have been deleted")


    case SaveSnapshotFailure(metadata, reason) => log.warning("Snapshot Failed - keeping on going but one should check that"); ScheduleNextFetch()

    case e => log.info("Received Message:  {}", e)
  }


  def receiveFetchCommand: Receive = {

    case FetchChangeSet => {

      chgtFetcher ! FetchNewChangeSet(state.lastChangeTime)

      become({
        case NewChangeSet(changeSet) =>
          persist(ChangeSetFetched(changeSet)) { event =>
            log.info("Persisted event: {}", event.toString)
            updateState(event)
            self ! CreatePropagationOps
            context.become(receiveCreatePropagationOps)
          }
          unstashAll()
          unbecome()
        case NoAvailableChangeSet => ScheduleNextFetch; context.become(receiveCommand)
        case _ => stash()

      }, discardOld = false)
    }

    case e =>  log.error("Unexpected Message received: " + e.toString());

  }

  def receiveCreatePropagationOps: Receive = {

    case CreatePropagationOps => {

      val propsOps = createpropsOps(state.changeSet)

      persist(PropagationOpsCreated(propsOps)) { event =>

        log.info("Persisted event: {}", event.toString)
        updateState(event)
        self ! PropagateChange
        context.become(receivePropagationCommand)

      }

    }

    case e =>  log.error("Unexpected Message received: " + e.toString());

  }

  /**
    * Propagate all Propagation Operation to the ChangeWriter
    * @return
    */
  def receivePropagationCommand: Receive = {

    case PropagateChange =>

      state.eventPropOps match {

        case Some(head::tail) if !tail.isEmpty =>
          chgtWriter ! head
          become ({
            case ACK =>
              persist(PropOpsPropagated(head)) { event =>
                updateState(event)
                self ! PropagateChange ; unbecome()
                log.info("Persisted event: {}", event.toString)
              }
            case _   => stash()
          }, discardOld = false)

        case Some(head::tail) if tail.isEmpty  =>
          chgtWriter ! head
          become ({
            case ACK =>
              persist(PropOpsPropagated(head)) { event =>
                updateState(event)
                unstashAll(); self ! HandleChgtPropagationEnd; context.become(receiveCommand)
                log.info("Persisted event: {}", event.toString)
                log.info("All changes have been propagated, updating state lastChangetime and issuing HandleChgtPropagationEnd Command")
              }
            case _   => stash()
          }, discardOld = false)

        case Some(Nil)                         =>
          self ! HandleChgtPropagationEnd; context.become(receiveCommand)
          log.info("All changes have been propagated, issuing HandleChgtPropagationEnd Command")

        case None                              => log.error("Unexpected None State in receivePropagationCommand")
      }

    case _               => stash()

  }


  def updateState(event: Event) = {

    event match  {

      case ChangeSetFetched(changeSet) => {
        state = state.addChangeSet(Some(changeSet))
        log.info("state is now {}", state.toString())
      }
      case PropagationOpsCreated(propsOps) => {
        state = state.addEventPropOps(Some(propsOps))
        log.info("state is now {}", state.toString())
      }
      case PropOpsPropagated(propOp) =>  {

        if (state.eventPropOps.get.nonEmpty && state.eventPropOps.get.head == propOp) {
          if (state.eventPropOps.get.tail.isEmpty == false)
            state = state.updatePropOps(Some(state.eventPropOps.get.tail), state.eventPropOps.get.head.historyTime)
          else
            state = state.updatePropOps(None, state.eventPropOps.get.head.historyTime)
          log.info("state is now {}", state.toString())
        }
        else
          log.error("Problem with state")
      }
    }

  }

  def createpropsOps(changeSet: Option[List[ChangeEvent]]): List[PropagationOp] = {

    changeSet match {
      case Some(e) => e.foldLeft(List.empty[PropagationOp]){
        (b:List[PropagationOp], a:ChangeEvent) =>

          a match {
            case ChangeEvent(cm @ ConceptMerged(sujectOfChangeURI, mergingConceptURI), instant) =>
              ConceptMergePropagation(sujectOfChangeURI, mergingConceptURI, instant)::b
            case ChangeEvent(cm : AtomicChange, instant) =>  {
              if (!b.exists { case x: ConceptUpdatePropagation if x.subjectOfChangeURI == cm.subjectOfChangeURI => true; case _ => false })
                ConceptUpdatePropagation(cm.subjectOfChangeURI, instant)::b
              else b
            }
          }
      }
      case None => Nil
    }

  }


  def receiveRecover: Receive = {

    case event: Event => {log.info(s"recovered event ${event.toString}"); updateState(event)}

    case SnapshotOffer(metadata, offeredSnapshot) => {state = offeredSnapshot.asInstanceOf[State]; log.info("Recovering from Snapshot to State: {}", state.toString)}

    case RecoveryCompleted => {log.info(s"recovery completed")}
  }

  private def ScheduleNextFetch(): Unit = {

    context.system.scheduler.scheduleOnce(requestInterval, self, GetBusy)

  }

}

