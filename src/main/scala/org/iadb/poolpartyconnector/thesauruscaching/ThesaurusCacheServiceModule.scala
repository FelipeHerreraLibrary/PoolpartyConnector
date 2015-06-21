package org.iadb.poolpartyconnector.thesauruscaching

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

  lazy val system = ActorSystem()
  lazy val service: ThesaurusCacheService = wire[ThesaurusCacheServicePoolPartyImpl]

}


object ThesaurusCacheServiceModule extends ThesaurusCacheServiceModule