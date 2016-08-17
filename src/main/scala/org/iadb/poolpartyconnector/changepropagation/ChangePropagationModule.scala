package org.iadb.poolpartyconnector.changepropagation

import java.time.Instant

import akka.actor.{ActorRef, ActorSystem, PoisonPill, Props}
import org.dspace.core.Context
import org.iadb.poolpartyconnector.dspacextension.dspaceconnectorconfiguration.DspacePoolPartyConnectorSettings
import org.iadb.poolpartyconnector.dspacextension.dspacedataoperationadaptation.schememapping.{SchemeMetadatumMappingService, SchemeMetadatumMappingServiceImpl}
import org.iadb.poolpartyconnector.thesaurusoperation.ThesaurusSparqlConsumer
import spray.caching.{Cache, ExpiringLruCache}

import scala.concurrent.{Await}
import scala.concurrent.duration._
import scala.util.{Failure, Success, Try}

/**
  * Created by Daniel Maatari Okouya on 8/9/16.
  */
class ChangePropagationModule (val actorSystemName         : String,
                               val settings                : DspacePoolPartyConnectorSettings,
                               val thesaurusSparqlConsumer : ThesaurusSparqlConsumer,
                               val fromTimeString          : String,
                               val requestInterval         : FiniteDuration,
                               val cacheMaxCapacity        : Int,
                               val cacheInitialCapacity    : Int,
                               val cacheTimeToLive         : Duration,
                               val cacheTimeToIdle         : Duration) {


  import com.softwaremill.macwire._
  import com.softwaremill.tagging._

  system.log.info(s"\n\nI m the ChangePropagationModule number: ${System.identityHashCode(this)}\n\n")

  lazy val system            : ActorSystem                   = ActorSystem(actorSystemName)
  lazy val cache             : Cache[String]                 = new ExpiringLruCache[String](cacheMaxCapacity, cacheInitialCapacity, cacheTimeToLive , cacheTimeToIdle)
  lazy val dContext                                          = Try {
                                                                new Context
                                                              } match {
                                                                case Success(e) => e.turnOffAuthorisationSystem(); Some(e);
                                                                case Failure(e) => None
                                                              }
  lazy val fromtime          : Instant                       = Instant.parse(fromTimeString)
  lazy val sparqlEnpoint                                     = settings.poolpartyServerSettings.sparqlEndpoint.taggedWith[EndPoint]
  lazy val mappingService    : SchemeMetadatumMappingService = wire[SchemeMetadatumMappingServiceImpl]
  lazy val dWriterService    : DspaceChangeWriterService     = wire[DspaceChangeWriterServiceImpl]
  lazy val changeFetcherActor: ActorRef                      = system.actorOf(Props(wire[ChangeFetcherActor]))
  lazy val changeWriterActor : ActorRef                      = system.actorOf(Props(wire[ChangeWriterActor]))
  lazy val fetcher                                           = changeFetcherActor.taggedWith[Fetcher]
  lazy val writer                                            = changeWriterActor.taggedWith[Writer]
  lazy val changePropagationActor                            = system.actorOf(Props(wire[ChangePropagationActor]))


  def shutdownModule: Try[Unit] = Try {
    system.terminate()
    Await.result(system.whenTerminated, Duration.Inf)
    dContext.fold(system.log.info("No Dspace Context was initially Started"))(e=>e.complete())
  }

}
