package org.iadb.poolpartyconnector.thesaurusoperation

import akka.actor.ActorSystem
import org.iadb.poolpartyconnector.dspacextension.dspaceconnectorconfiguration.DspaceDspacePoolPartyConnectorSettingImpl

/**
 * Created by Daniel Maatari Okouya on 7/20/15.
 */
trait ThesaurusServiceModuleTest {


  import com.softwaremill.macwire._

  val loadedConnectorSettings   = DspaceDspacePoolPartyConnectorSettingImpl("file:///Users/maatary/Dev/IdeaProjects/PoolpartyConnector/src/test/resources/poolpartydspace.conf")

  def system                                           = ActorSystem()
  def thesaurusSparqlConsumer: ThesaurusSparqlConsumer = wire[ThesaurusSparqlConsumerJenaImpl]
  def service: ThesaurusCacheService                   = wire[ThesaurusCacheServicePoolPartyImpl]

}



object ThesaurusServiceModuleTest extends ThesaurusServiceModuleTest