package org.iadb.poolpartyconnector.dspacextension.springadaptation

import akka.actor.ActorSystem

/**
 * Created by Daniel Maatari Okouya on 6/10/15.
 *
 * A Trait that represent the behavior of a class that wrap an ActorSystem
 * so it can be instantiate with Spring.
 *
 */
trait ActorSystemSpringWrapperBean {


  def getActorSystem: ActorSystem


  def shutdown: Unit

}
