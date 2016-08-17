package org.iadb.poolpartyconnector.dspacextension.springadaptation

import akka.actor.ActorSystem
import scala.concurrent.duration._
import scala.concurrent.Await

/**
 * Created by Daniel Maatari Okouya on 6/10/15.
 *
 * A Trait that represent the behavior of a class that wrap an ActorSystem
 * so it can be instantiate with Spring.
 *
 */
trait ActorSystemSpringWrapperBean {
  val system   : ActorSystem
  def shutdown : Unit
}


class ActorSystemSpringWrapperBeanImpl extends ActorSystemSpringWrapperBean {

  println(s"I m the ActorSystemSpringWrapperBeanImpl numer: ${System.identityHashCode(this)}")

  val system   = ActorSystem("PoolPartyConnector-ActorSystem")
  def shutdown = {system.log.info("Shutting down the actor system"); Await.result(system.terminate(), 5 seconds)}
}