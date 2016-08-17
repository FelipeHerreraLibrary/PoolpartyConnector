package org.iadb.poolpartyconnector.changepropagation

import java.time.Instant
import java.time.temporal.ChronoUnit

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import org.iadb.poolpartyconnector.changepropagation.ChangeFetcherActor.{FetchNewChangeSet, NewChangeSet, NoAvailableChangeSet}
import org.iadb.poolpartyconnector.testactor.StopSystemAfterAllFixture
import org.scalatest.{FeatureSpec, FeatureSpecLike, GivenWhenThen, Matchers}
import com.softwaremill.tagging._

import scala.concurrent.duration._
/**
  * Created by Daniel Maatari Okouya on 7/22/16.
  */
class FetchChangeSetActorfeatureTest  extends TestKit (ActorSystem("testSystem")) with FeatureSpecLike
with Matchers with GivenWhenThen with StopSystemAfterAllFixture with ImplicitSender {


  feature("a Fetching Actor Fetch new ChangeSet From the PoolParty History when Requested To from the time supplied in the request") {


    val log = system.log

    scenario("An Actor is requested to fetch the changeSet starting from a given date to that has changes to date") {

      Given("a FetchChangeSet Actor")



        val sparqlEndPoint = "http://127.0.0.1:8086/PoolParty/sparql/publicthesauri".taggedWith[EndPoint]

        val startDate = "2016-08-07T00:00:00Z"

        val fetchActor = new ChangePropagationModuleTest(system, Instant.parse(startDate)truncatedTo(ChronoUnit.SECONDS)).changeFetcherActor

      When("requested to fetch the new change set starting from a given date")

        fetchActor ! FetchNewChangeSet(Instant.parse(startDate))

      Then("")

        expectMsgPF(10 second) {
          case x @ NewChangeSet(m) => log.info(x.toString())
          case NoAvailableChangeSet => log.info(NoAvailableChangeSet.toString)
          case e => log.info(s"received Wierd stuff ${e.toString}")
        }

    }

  }

}
