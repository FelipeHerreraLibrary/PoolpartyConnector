package org.iadb.poolpartyconnector.thesaurusoperation

import scala.concurrent.duration._
import akka.actor.ActorSystem
import org.iadb.poolpartyconnector.dspacextension.dspaceconnectorconfiguration.DspaceDspacePoolPartyConnectorSettingImpl
import spray.caching.ExpiringLruCache

/**
 * Created by Daniel Maatari Okouya on 6/20/15.
 *
 * The module of creation of the ThesaurusCacheService for dependency injection
 *
 * Usage: object ThesaurusCacheServiceModule extends ThesaurusCacheServiceModule
 */

trait ThesaurusCacheServiceModule {

  import com.softwaremill.macwire._

  val loadedConnectorSettings  = DspaceDspacePoolPartyConnectorSettingImpl("file:///Users/maatary/Dev/IdeaProjects/PoolpartyConnector/src/test/resources/poolpartydspace.conf")

  def system                                           = ActorSystem()
  def cache                                            = lruCache[String]()
  def thesaurusSparqlConsumer: ThesaurusSparqlConsumer = wire[ThesaurusSparqlConsumerJenaImpl]
  def service: ThesaurusCacheService                   = wire[ThesaurusCacheServicePoolPartyImpl]



  private def lruCache[T](maxCapacity: Int = 2000, initialCapacity: Int = 2000, timeToLive: Duration = Duration.Inf, timeToIdle: Duration = Duration.Inf) = {
    new ExpiringLruCache[T](maxCapacity, initialCapacity, timeToLive, timeToIdle)
  }
}


object ThesaurusCacheServiceModule extends ThesaurusCacheServiceModule
