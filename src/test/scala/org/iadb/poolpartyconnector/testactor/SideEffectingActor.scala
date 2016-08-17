package org.iadb.poolpartyconnector.testactor

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import org.iadb.poolpartyconnector.testactor.SideEffectingActor.Greetings




/**
  * Created by Daniel Maatari Okouya on 5/12/16.
  */

object SideEffectingActor {

  case class Greetings(msg:String)

}



class SideEffectingActor extends Actor with ActorLogging {


  import SideEffectingActor._

  override def receive: Receive = {


    case Greetings(msg) => log.info("Hello {}", msg)
    case _ => ()

  }

}


object ActorApp extends App {


  val system = ActorSystem("testsystem")

  val sideEffectingActor = system.actorOf(Props[SideEffectingActor])


  sideEffectingActor ! Greetings("greetings")
  sideEffectingActor ! Greetings("how are you")
  sideEffectingActor ! Greetings("I am John")
  sideEffectingActor ! Greetings("and you")



}