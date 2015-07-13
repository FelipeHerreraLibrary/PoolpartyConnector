package org.iadb.poolpartyconnector.thesaurusoperation

import akka.actor.ActorSystem

/**
 * Created by Daniel Maatari Okouya on 6/20/15.
 *
 * The module of creation of the ThesaurusCacheService for dependency injection
 *
 * Usage: object ThesaurusCacheServiceModule extends ThesaurusCacheServiceModule
 */
trait ThesaurusCacheServiceModule {

  import com.softwaremill.macwire._

  def system = ActorSystem()
  def service: ThesaurusCacheService = wire[ThesaurusCacheServicePoolPartyImpl]

}


object ThesaurusCacheServiceModule extends ThesaurusCacheServiceModule