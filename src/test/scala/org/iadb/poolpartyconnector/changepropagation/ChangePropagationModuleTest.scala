package org.iadb.poolpartyconnector.changepropagation

import java.time.Instant

import akka.actor.{ActorRef, ActorSystem, Props}
import org.iadb.poolpartyconnector.dspacextension.dspaceconnectorconfiguration.DspaceDspacePoolPartyConnectorSettingImpl
import org.iadb.poolpartyconnector.thesaurusoperation.{ThesaurusSparqlConsumerModule}
import scala.concurrent.duration._

import scala.concurrent.duration.{Duration, FiniteDuration}


/**
  * Created by Daniel Maatari Okouya on 8/6/16.
  */
class ChangePropagationModuleTest(val asystem: ActorSystem,
                                  val afromtime: Instant,
                                  requestInterval: FiniteDuration = 3 seconds)
  extends ChangePropagationModule(
                                   asystem.name,
                                   DspaceDspacePoolPartyConnectorSettingImpl("file:///Users/maatary/Dev/IdeaProjects/PoolpartyConnector/src/test/resources/poolpartydspace.conf"),
                                   ThesaurusSparqlConsumerModule.thesaurusSparqlConsumer,
                                   afromtime.toString,
                                   requestInterval, 10, 10, Duration.Inf, Duration.Inf

                                 ) {

  import com.softwaremill.macwire._
  import com.softwaremill.tagging._


  override lazy val system = asystem
  //override lazy val fromtime = afromtime //Very bad bad


   lazy val getChangePropagationActor = (writer: ActorRef @@ Writer, fetcher: ActorRef @@ Fetcher) => {
    system.actorOf(Props(wire[ChangePropagationActor]))
  }


}
