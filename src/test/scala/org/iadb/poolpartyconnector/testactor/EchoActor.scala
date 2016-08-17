package org.iadb.poolpartyconnector.testactor

import akka.actor.Actor


object EchoActor {

  case class EchoMsg(id: String)

}

/**
  * Created by Daniel Maatari Okouya on 5/12/16.
  */
class EchoActor extends Actor {

  import EchoActor._

  override def receive: Receive = {

    case EchoMsg(id) => sender() ! EchoMsg(id)


  }
}
