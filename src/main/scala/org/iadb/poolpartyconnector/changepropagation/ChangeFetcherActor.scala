package org.iadb.poolpartyconnector.changepropagation

import java.time.Instant

import akka.actor.{Actor, Props}
import org.iadb.poolpartyconnector.changepropagation.ChangeFetcherActor.{FetchNewChangeSet, NewChangeSet, NoAvailableChangeSet}
import org.iadb.poolpartyconnector.thesaurusoperation.ThesaurusSparqlConsumer
import com.softwaremill.tagging._

object ChangeFetcherActor {

  sealed trait Command
  case class FetchNewChangeSet(fromDate: Instant) extends Command


  sealed trait Response
  case class NewChangeSet(newChangeSet: List[ChangeEvent]) extends Response
  case object NoAvailableChangeSet extends Response


  def props(chgtConsumer: ThesaurusSparqlConsumer, SparqlEndPoint: String @@ EndPoint) = Props(new ChangeFetcherActor(chgtConsumer, SparqlEndPoint))

}
/**
  * Created by Daniel Maatari Okouya on 5/30/16.
  */
class ChangeFetcherActor(chgtConsumer: ThesaurusSparqlConsumer, SparqlEndPoint: String @@ EndPoint) extends Actor {



  override def receive: Receive = {

    case FetchNewChangeSet(date) => {

      chgtConsumer.getHistoryChangeSet(SparqlEndPoint, date.toString) match  {
        case Nil => sender() ! NoAvailableChangeSet
        case list => sender() ! NewChangeSet(list)
      }


    }

    case _ => println("Message not Understood")


  }
}
