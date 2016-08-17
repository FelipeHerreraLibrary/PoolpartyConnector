package org.iadb.poolpartyconnector.testactor

import akka.actor.{ActorLogging, ActorSystem, Props}
import akka.persistence.{PersistentActor, RecoveryCompleted}
import org.iadb.poolpartyconnector.testactor.MyPersistentActor.NewMessage


object MyPersistentActor {

  sealed trait Command
  case class NewMessage(msg: String) extends Command
  case object GetLastMsg extends Command
  case object NoLastMsg extends Command


  sealed trait Response
  case class LastMsg(msg: String) extends Response


  sealed trait Event
  case class   MessageReceived(msg: String) extends Event



  def props = Props (new MyPersistentActor)

}

/**
  * Created by Daniel Maatari Okouya on 5/24/16.
  */
class MyPersistentActor extends PersistentActor with ActorLogging {

  import MyPersistentActor._

  var lastmsg: Option[String] = None
  val persistenceId: String   = "testPersist"




  def receiveCommand: Receive = {

    case GetLastMsg => {

      log.info(s"receive request of last msg and the current one is: ${lastmsg.getOrElse("none")}")
      lastmsg.fold (sender() ! NoLastMsg)( e => sender() ! LastMsg(e))

    }

    case NewMessage(msg) =>  persist(MessageReceived(msg)) { event =>

        log.info("persisted State: {}", event.toString)
        updateState(event)
        this.saveSnapshot()
    }

  }



  def receiveRecover: Receive = {

    case event: Event => { updateState(event); log.info(s"recovered event ${event.toString}") }

    case RecoveryCompleted => log.info("Persistent actor Fully Recovered")
  }



  def updateState (event: Event) = {

    event match  {
      case MessageReceived(msg) => {
        log.info("updating state from message: " + msg)
        lastmsg = Some(msg)
      }
      case _ => log.info("unkown Event")

    }
  }

}






object PersistentActorApp extends App {

  val system = ActorSystem("persistentSystem")

  val persts = system.actorOf(MyPersistentActor.props)


  persts ! MyPersistentActor.GetLastMsg
  persts ! NewMessage("msg0")
  persts ! NewMessage("msg1")
  persts ! MyPersistentActor.GetLastMsg

}
