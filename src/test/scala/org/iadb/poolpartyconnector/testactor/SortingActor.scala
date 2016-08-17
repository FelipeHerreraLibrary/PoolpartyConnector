package org.iadb.poolpartyconnector.testactor

import akka.actor.{Actor, ActorRef, Props}




/**
  * Created by Daniel Maatari Okouya on 5/10/16.
  */


object SortingActor {

  case class Event(id:Int)
  case class SortEvent(events: List[Event])
  case class EventSorted(events: List[Event])

  def props(receiveActor: ActorRef) = Props (new SortingActor(receiveActor))

}




class SortingActor(receiveActor: ActorRef) extends Actor {

  import SortingActor._

  override def receive: Receive = {

    case SortEvent(list) => println(s"received: ${list.toString()}"); receiveActor ! EventSorted(list.sortBy(_.id))
  }

}


