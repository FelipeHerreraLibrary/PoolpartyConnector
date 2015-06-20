package org.iadb.poolpartyconnector.dspaceutils

import akka.actor.ActorSystem

/**
 * Created by Daniel Maatari Okouya on 6/10/15.
 */
class ActorSystemSpringWrapperBeanImpl extends ActorSystemSpringWrapperBean {


  //TODO Execption handling
  private val system = ActorSystem("PoolPartyConnector-ActorSystem")

  override def getActorSystem: ActorSystem = system

  override def shutdown: Unit = {println("Shutting down the actor system"); system.shutdown()}
}
