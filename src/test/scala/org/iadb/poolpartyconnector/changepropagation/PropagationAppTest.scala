package org.iadb.poolpartyconnector.changepropagation



import akka.actor.ActorSystem
import com.softwaremill.tagging._
import org.dspace.core.Context
import org.iadb.poolpartyconnector.dspacextension.dspaceconnectorconfiguration.DspaceDspacePoolPartyConnectorSettingImpl
import org.iadb.poolpartyconnector.thesaurusoperation.ThesaurusSparqlConsumerModule
import spray.caching.ExpiringLruCache

import scala.concurrent.duration._
import scala.io.StdIn
/**
  * Created by Daniel Maatari Okouya on 8/8/16.
  */
object PropagationAppTest extends App {


  //val system             = ActorSystem("PropagationSystem")
  val injector = new ChangePropagationModule("PoolPartyConnector-ActorSystem",
                                              DspaceDspacePoolPartyConnectorSettingImpl("file:///Users/maatary/Dev/IdeaProjects/PoolpartyConnector/src/test/resources/poolpartydspace.conf"),
                                              ThesaurusSparqlConsumerModule.thesaurusSparqlConsumer,
                                              "2016-06-01T00:00:00Z",
                                              new FiniteDuration(10, SECONDS),
                                              2000, 2000, Duration.Inf, Duration.Inf) {

    override lazy val dContext: Option[Context] = None
  }

  injector.changePropagationActor

  StdIn.readLine()

  //system.stop(changeSetActor)
  injector.shutdownModule



  /*private def lruCache[T](maxCapacity: Int = 2000, initialCapacity: Int = 2000, timeToLive: Duration = Duration.Inf, timeToIdle: Duration = Duration.Inf) = {

    new ExpiringLruCache[T](maxCapacity, initialCapacity, timeToLive, timeToIdle)

  }*/

}
