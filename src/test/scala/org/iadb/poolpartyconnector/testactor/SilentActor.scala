package org.iadb.poolpartyconnector.testactor

import akka.actor.{Actor, ActorRef}



object SilentActor {

  case class SilentMessage(msg:String)

  case class Getstate(msg: ActorRef)


}

/**
  * Created by Daniel Maatari Okouya on 5/9/16.
  */
class SilentActor extends Actor {


  import SilentActor._

  var state = List[String]()


  override def receive: Receive = {

    case SilentMessage(msg) => state = msg::state reverse

    case Getstate(testActor) => testActor ! state


  }
}

