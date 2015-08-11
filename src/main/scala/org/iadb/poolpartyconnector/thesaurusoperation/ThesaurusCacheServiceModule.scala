package org.iadb.poolpartyconnector.thesaurusoperation

import akka.actor.ActorSystem
import org.iadb.poolpartyconnector.dspacextension.dspaceconnectorconfiguration.DspaceDspacePoolPartyConnectorSettingImpl

/**
 * Created by Daniel Maatari Okouya on 6/20/15.
 *
 * The module of creation of the ThesaurusCacheService for dependency injection
 *
 * Usage: object ThesaurusCacheServiceModule extends ThesaurusCacheServiceModule
 */

trait ThesaurusCacheServiceModule {

  import com.softwaremill.macwire._

  val loadedConnectorSettings   = DspaceDspacePoolPartyConnectorSettingImpl("file:///Users/maatary/Dev/IdeaProjects/PoolpartyConnector/src/test/resources/poolpartydspace.conf")

  def system = ActorSystem()
  def service: ThesaurusCacheService = wire[ThesaurusCacheServicePoolPartyImpl]

}


object ThesaurusCacheServiceModule extends ThesaurusCacheServiceModule
