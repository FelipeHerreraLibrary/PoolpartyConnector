package org.iadb.poolpartyconnector.testactor

import akka.actor.{Actor, ActorRef, Props}
import akka.actor.Actor.Receive


object FilteringActor {

  case class Event(id:Int)

  def props(recevingActor: ActorRef) = Props(new FilteringActor(recevingActor))

}

/**
  * Created by Daniel Maatari Okouya on 5/12/16.
  */
class FilteringActor(recevingActor: ActorRef) extends Actor {

  import FilteringActor._

  var state = List[Int]()

  override def receive: Receive = {

    case Event(id) => {
      if ((state contains id) == false) {
        state :+= id
        recevingActor ! Event(id)
      }
    }

  }
}
