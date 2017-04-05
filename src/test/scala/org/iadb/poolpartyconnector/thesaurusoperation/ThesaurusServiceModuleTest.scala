package org.iadb.poolpartyconnector.thesaurusoperation

import akka.actor.ActorSystem
import org.iadb.poolpartyconnector.dspacextension.dspaceconnectorconfiguration.{DspaceDspacePoolPartyConnectorSettingImpl, DspacePoolPartyConnectorSettings}
import spray.caching.{Cache, ExpiringLruCache}

import scala.concurrent.duration.Duration

/**
 * Created by Daniel Maatari Okouya on 7/20/15.
 */
trait ThesaurusServiceModuleTest {


  import com.softwaremill.macwire._

  val loadedConnectorSettings : DspacePoolPartyConnectorSettings = DspaceDspacePoolPartyConnectorSettingImpl("file:////Users/luizfr/Development/ideaprojects/PoolpartyConnector/src/test/resources/poolpartydspace.conf")

  def system                                                     = ActorSystem()
  lazy val cache             : Cache[String]                     = new ExpiringLruCache[String](2000, 2000, Duration.Inf , Duration.Inf)
  def thesaurusSparqlConsumer : ThesaurusSparqlConsumer          = wire[ThesaurusSparqlConsumerJenaImpl]
  def service: ThesaurusCacheService                             = wire[ThesaurusCacheServicePoolPartyImpl]

}



object ThesaurusServiceModuleTest extends ThesaurusServiceModuleTest