package org.iadb.poolpartyconnector.changepropagation
import java.time.Instant
import java.time.temporal.ChronoUnit

import akka.actor.{ActorRef, ActorSystem}
import org.iadb.poolpartyconnector.changepropagation.ChangePropagationActor._
import org.iadb.poolpartyconnector.testactor.{PersistenceCleanup, PersistenceSpec}
import akka.testkit.{TestActor, TestProbe}
import org.iadb.poolpartyconnector.changepropagation.ChangeFetcherActor.{FetchNewChangeSet, NewChangeSet, NoAvailableChangeSet}
import org.iadb.poolpartyconnector.thesaurusoperation.JsonProtocolSpecification.LanguageLiteral

import scala.concurrent.duration._
import com.softwaremill.tagging._

/**
  * Created by Daniel Maatari Okouya on 5/25/16.
  */
class ChangePropagationActorFeatureTest extends PersistenceSpec(ActorSystem("Persistent-test-System")) with PersistenceCleanup {


  val log = system.log

  val changeSet = List(ChangeEvent(PrefAdded("http://thesaurus.iadb.org/publicthesauri/11d3a49a-8c1c-40b6-9aa3-dca578896638", LanguageLiteral("Andares","pt")), Instant.parse("2016-08-02T18:33:21Z")),
  ChangeEvent(PrefAdded("http://thesaurus.iadb.org/publicthesauri/11d3a49a-8c1c-40b6-9aa3-dca578896638",LanguageLiteral("Andares","fr")), Instant.parse("2016-08-02T18:33:16Z")),
  ChangeEvent(PrefAdded("http://thesaurus.iadb.org/publicthesauri/11d3a49a-8c1c-40b6-9aa3-dca578896638",LanguageLiteral("Andares","es")), Instant.parse("2016-08-02T18:33:11Z")),
  ChangeEvent(PrefAdded("http://thesaurus.iadb.org/publicthesauri/11d3a49a-8c1c-40b6-9aa3-dca578896638",LanguageLiteral("Andares","en")),Instant.parse("2016-08-02T18:32:42Z")),
  ChangeEvent(PrefAdded("http://thesaurus.iadb.org/publicthesauri/82a45f2d-d3b7-4701-82c3-25cc6a16c22c",LanguageLiteral("Promessas de Campanha","pt")),Instant.parse("2016-08-02T17:14:02Z")),
  ChangeEvent(PrefAdded("http://thesaurus.iadb.org/publicthesauri/67634106921252965107454",LanguageLiteral("Eleições","pt")),Instant.parse("2016-08-02T17:13:45Z")),
  ChangeEvent(PrefChanged("http://thesaurus.iadb.org/publicthesauri/155b652f-56a7-4166-b879-a1e2a0454e58",LanguageLiteral("Lucena, André F. P.","pt"),LanguageLiteral("Lucena, André F.P.","pt")), Instant.parse("2016-08-01T22:20:20Z")),
  ChangeEvent(PrefChanged("http://thesaurus.iadb.org/publicthesauri/155b652f-56a7-4166-b879-a1e2a0454e58",LanguageLiteral("Lucena, André F. P.","fr"),LanguageLiteral("Lucena, André F.P.","fr")), Instant.parse("2016-08-01T22:20:01Z")),
  ChangeEvent(PrefChanged("http://thesaurus.iadb.org/publicthesauri/155b652f-56a7-4166-b879-a1e2a0454e58",LanguageLiteral("Lucena, André F. P.","es"),LanguageLiteral("Lucena, André F.P.","es")), Instant.parse("2016-08-01T22:19:35Z")),
  ChangeEvent(PrefChanged("http://thesaurus.iadb.org/publicthesauri/155b652f-56a7-4166-b879-a1e2a0454e58",LanguageLiteral("Lucena, André F. P.","en"),LanguageLiteral("Lucena, André F.P.","en")), Instant.parse("2016-08-01T22:19:03Z")),
  ChangeEvent(ConceptMerged("http://thesaurus.iadb.org/publicthesauri/d40e0325-85dc-41ef-92cd-7600b83caad6","http://thesaurus.iadb.org/publicthesauri/ac5e5e16-ef06-4166-ba94-2529602194da"), Instant.parse("2016-07-30T23:09:39Z")),
  ChangeEvent(ConceptMerged("http://thesaurus.iadb.org/publicthesauri/a2359f60-8528-48a3-8a61-b8fd107e6cc4","http://thesaurus.iadb.org/publicthesauri/ac5e5e16-ef06-4166-ba94-2529602194da"), Instant.parse("2016-07-30T23:08:17Z")),
  ChangeEvent(PrefAdded("http://thesaurus.iadb.org/publicthesauri/ac5e5e16-ef06-4166-ba94-2529602194da",LanguageLiteral("merge3","en")), Instant.parse("2016-07-30T23:07:59Z")),
  ChangeEvent(PrefAdded("http://thesaurus.iadb.org/publicthesauri/d40e0325-85dc-41ef-92cd-7600b83caad6",LanguageLiteral("merge1","en")), Instant.parse("2016-07-30T23:07:51Z")),
  ChangeEvent(PrefAdded("http://thesaurus.iadb.org/publicthesauri/a2359f60-8528-48a3-8a61-b8fd107e6cc4",LanguageLiteral("merge0","en")), Instant.parse("2016-07-30T23:07:40Z")),
  ChangeEvent(ConceptMerged("http://thesaurus.iadb.org/publicthesauri/d0165bc4-08af-4c1d-b976-791c56537b52","http://thesaurus.iadb.org/publicthesauri/6e065ad4-fb2e-475b-bdf9-1c33a42e8aff"), Instant.parse("2016-07-29T02:50:01Z")),
  ChangeEvent(AltAdded("http://thesaurus.iadb.org/publicthesauri/d0165bc4-08af-4c1d-b976-791c56537b52",LanguageLiteral("Ceramic water filters","en")), Instant.parse("2016-07-29T02:48:30Z")),
  ChangeEvent(AltAdded("http://thesaurus.iadb.org/publicthesauri/d0165bc4-08af-4c1d-b976-791c56537b52",LanguageLiteral("Clay pot filter","en")), Instant.parse("2016-07-29T02:48:30Z")),
  ChangeEvent(AltAdded("http://thesaurus.iadb.org/publicthesauri/d0165bc4-08af-4c1d-b976-791c56537b52",LanguageLiteral("Clay pot filters","en")), Instant.parse("2016-07-29T02:48:30Z")),
  ChangeEvent(PrefAdded("http://thesaurus.iadb.org/publicthesauri/d0165bc4-08af-4c1d-b976-791c56537b52",LanguageLiteral("Ceramic water filter","en")), Instant.parse("2016-07-29T02:48:29Z")),
  ChangeEvent(AltAdded("http://thesaurus.iadb.org/publicthesauri/82a45f2d-d3b7-4701-82c3-25cc6a16c22c",LanguageLiteral("Promesas Electorales","es")), Instant.parse("2016-07-28T22:59:59Z")),
  ChangeEvent(PrefAdded("http://thesaurus.iadb.org/publicthesauri/82a45f2d-d3b7-4701-82c3-25cc6a16c22c",LanguageLiteral("Promesas de Campaña","es")), Instant.parse("2016-07-28T22:59:34Z")),
  ChangeEvent(PrefAdded("http://thesaurus.iadb.org/publicthesauri/e5e149cd-fee1-4e16-8d34-93e8ddfc46f7",LanguageLiteral("Compra de Votos","pt")), Instant.parse("2016-07-25T18:49:58Z")),
  ChangeEvent(PrefChanged("http://thesaurus.iadb.org/publicthesauri/1f69c6e9-c611-4cef-aacb-fec6e5df03bb",LanguageLiteral("YouYou","en"),LanguageLiteral("You","en")), Instant.parse("2016-07-25T18:37:13Z")),
  ChangeEvent(PrefAdded("http://thesaurus.iadb.org/publicthesauri/e5e149cd-fee1-4e16-8d34-93e8ddfc46f7", LanguageLiteral("Compra de Votos","es")), Instant.parse("2016-07-22T18:56:50Z")),
  ChangeEvent(AltChanged("http://thesaurus.iadb.org/publicthesauri/70212f5c-229d-448b-b69e-79fb25a6bd9e", LanguageLiteral("I I","en"),LanguageLiteral("I","en")), Instant.parse("2016-07-22T18:04:37Z")),
  ChangeEvent(AltAdded("http://thesaurus.iadb.org/publicthesauri/70212f5c-229d-448b-b69e-79fb25a6bd9e",LanguageLiteral("I","en")),Instant.parse("2016-07-22T18:02:36Z")),
  ChangeEvent(AltChanged("http://thesaurus.iadb.org/publicthesauri/1f69c6e9-c611-4cef-aacb-fec6e5df03bb",LanguageLiteral("yii","es"),LanguageLiteral("yi","es")),Instant.parse("2016-07-22T18:02:26Z")),
  ChangeEvent(AltAdded("http://thesaurus.iadb.org/publicthesauri/1f69c6e9-c611-4cef-aacb-fec6e5df03bb",LanguageLiteral("yi","es")),Instant.parse("2016-07-22T18:02:17Z")),
  ChangeEvent(PrefAdded("http://thesaurus.iadb.org/publicthesauri/1f69c6e9-c611-4cef-aacb-fec6e5df03bb",LanguageLiteral("You","en")),Instant.parse("2016-07-22T18:02:04Z")),
  ChangeEvent(PrefAdded("http://thesaurus.iadb.org/publicthesauri/70212f5c-229d-448b-b69e-79fb25a6bd9e",LanguageLiteral("me","en")),Instant.parse("2016-07-22T18:01:55Z")),
  ChangeEvent(AltAdded("http://thesaurus.iadb.org/publicthesauri/0b09f15d-b854-4eb8-9847-cdf68114e8ae",LanguageLiteral("Administración Aduanera","es")),Instant.parse("2016-07-21T22:41:25Z")),
  ChangeEvent(PrefAdded("http://thesaurus.iadb.org/publicthesauri/0b09f15d-b854-4eb8-9847-cdf68114e8ae",LanguageLiteral("Administración de Aduanas","es")), Instant.parse("2016-07-21T22:41:01Z")))

  val ChangeSet2 = List(ChangeEvent(PrefAdded("http://thesaurus.iadb.org/publicthesauri/11d3a49a-8c1c-40b6-9aa3-dca578896638", LanguageLiteral("Andares","pt")), Instant.parse("2016-08-02T18:33:21Z")),
                         ChangeEvent(PrefAdded("http://thesaurus.iadb.org/publicthesauri/11d3a49a-8c1c-40b6-9aa3-dca578896638",LanguageLiteral("Andares","fr")), Instant.parse("2016-08-02T18:33:16Z")),
                         ChangeEvent(PrefAdded("http://thesaurus.iadb.org/publicthesauri/11d3a49a-8c1c-40b6-9aa3-dca578896638",LanguageLiteral("Andares","es")), Instant.parse("2016-08-02T18:33:11Z")),
                         ChangeEvent(PrefAdded("http://thesaurus.iadb.org/publicthesauri/11d3a49a-8c1c-40b6-9aa3-dca578896638",LanguageLiteral("Andares","en")),Instant.parse("2016-08-02T18:32:42Z")),
                         ChangeEvent(PrefAdded("http://thesaurus.iadb.org/publicthesauri/82a45f2d-d3b7-4701-82c3-25cc6a16c22c",LanguageLiteral("Promessas de Campanha","pt")),Instant.parse("2016-08-02T17:14:02Z")),
                         ChangeEvent(PrefAdded("http://thesaurus.iadb.org/publicthesauri/67634106921252965107454",LanguageLiteral("Eleições","pt")),Instant.parse("2016-08-02T17:13:45Z")))

  val ChangeSet1 = List(ChangeEvent(PrefChanged("http://thesaurus.iadb.org/publicthesauri/155b652f-56a7-4166-b879-a1e2a0454e58",LanguageLiteral("Lucena, André F. P.","pt"),LanguageLiteral("Lucena, André F.P.","pt")), Instant.parse("2016-08-01T22:20:20Z")),
                         ChangeEvent(PrefChanged("http://thesaurus.iadb.org/publicthesauri/155b652f-56a7-4166-b879-a1e2a0454e58",LanguageLiteral("Lucena, André F. P.","fr"),LanguageLiteral("Lucena, André F.P.","fr")), Instant.parse("2016-08-01T22:20:01Z")),
                         ChangeEvent(PrefChanged("http://thesaurus.iadb.org/publicthesauri/155b652f-56a7-4166-b879-a1e2a0454e58",LanguageLiteral("Lucena, André F. P.","es"),LanguageLiteral("Lucena, André F.P.","es")), Instant.parse("2016-08-01T22:19:35Z")),
                         ChangeEvent(PrefChanged("http://thesaurus.iadb.org/publicthesauri/155b652f-56a7-4166-b879-a1e2a0454e58",LanguageLiteral("Lucena, André F. P.","en"),LanguageLiteral("Lucena, André F.P.","en")), Instant.parse("2016-08-01T22:19:03Z")),
                         ChangeEvent(ConceptMerged("http://thesaurus.iadb.org/publicthesauri/d40e0325-85dc-41ef-92cd-7600b83caad6","http://thesaurus.iadb.org/publicthesauri/ac5e5e16-ef06-4166-ba94-2529602194da"), Instant.parse("2016-07-30T23:09:39Z")),
                         ChangeEvent(ConceptMerged("http://thesaurus.iadb.org/publicthesauri/a2359f60-8528-48a3-8a61-b8fd107e6cc4","http://thesaurus.iadb.org/publicthesauri/ac5e5e16-ef06-4166-ba94-2529602194da"), Instant.parse("2016-07-30T23:08:17Z")))


  val requestInterval = 3 seconds
  val processingLag   = 1 seconds

  feature("Propagating Poolparty ChanSet with Fault Tolerance") {

    scenario("A ChangeSetActor request a new ChangetSet following a specific interval between request, from a Fetcher Actor that only return an empty ChangeSet"){

     info("--Verifying that the the ChangeSetActor Follows its schedule")



     Given(s"a ChangeSetActor with a schedule of fetching a changeSet every ${requestInterval}, a Probe ChangeFetcher and a Probe ChangeWriter")

      val chgtFetcher    = TestProbe()
      val chgtWriter     = TestProbe()
      val fromTime       = Instant.now().truncatedTo(ChronoUnit.SECONDS)
      val injectorModule = new ChangePropagationModuleTest(system, fromTime, requestInterval)
      val chgtActor      = injectorModule.getChangePropagationActor(chgtWriter.ref.taggedWith[Writer], chgtFetcher.ref.taggedWith[Fetcher])

     When("all are started")


     Then(s"The ChangeFetcher should receive at least 3 FetchNewChangeSet request from the ChangeSetActor within ${(requestInterval + processingLag).*(3)}")

      val received = chgtFetcher.receiveWhile((requestInterval + processingLag).*(3)) {
        case msg: FetchNewChangeSet => log.info(s"received: ${msg}"); chgtFetcher.reply(NoAvailableChangeSet); msg
      }

      received.size should be (3)

    }


    scenario("A ChangeSetActor fetch a ChangeSet from a Fetcher, start Propagating them, get restarted after the first Propagation, Recover and finishes its Propagation") {

      info("--Verifying that when restarted in the middle of a propagation, the ChangeSetActor restart Propagating from right after the last message it Propagated, and finishes the remaining one")



      Given(s"a ChangeSetActor that fetch ChangeSet every ${requestInterval}, a Probe ChangeWriter that acknowledge every Write Request and a Probe ChangeFetcher that responds with one pre-defined ChangetSet")

        val chgtFetcher    = TestProbe()
        val chgtWriter     = TestProbe()
        val fromTime       = Instant.now().truncatedTo(ChronoUnit.SECONDS)
        val injectorModule = new ChangePropagationModuleTest(system, fromTime, requestInterval)
        val chgtActor      = injectorModule.getChangePropagationActor(chgtWriter.ref.taggedWith[Writer], chgtFetcher.ref.taggedWith[Fetcher])


      When("all are started")


      Then(s"the ChangeFetcher should receive request of new changeSet from the ChangeSetActor within ${(requestInterval + processingLag)} ")

        chgtFetcher.expectMsg(requestInterval + processingLag, FetchNewChangeSet(fromTime))

      When("the ChangeFetcher replies with a new mock changeSet")

        chgtFetcher.reply(NewChangeSet(changeSet))

      Then("the ChangeWriter should receive the first ChangeEvent to Propagate from the mock ChangeSet")

        chgtWriter.expectMsg(ConceptUpdatePropagation("http://thesaurus.iadb.org/publicthesauri/0b09f15d-b854-4eb8-9847-cdf68114e8ae", Instant.parse("2016-07-21T22:41:25Z")))

      When("the ChangeWriter acknowledge the propagation of the change")

        chgtWriter.reply(ACK); chgtWriter.ignoreMsg {case msg => true}

      And("the ChangetSetActor is killed and started again")
        Thread.sleep(processingLag.toMillis) // Sleep to ensure that the message is received by the ChgtActor
        killActors(chgtActor)
        Thread.sleep(processingLag.toMillis) // Time to kill
        chgtWriter.ignoreNoMsg
        val res = injectorModule.getChangePropagationActor(chgtWriter.ref.taggedWith[Writer], chgtFetcher.ref.taggedWith[Fetcher])
        Thread.sleep(processingLag.toMillis) // Time to restart

      Then("the ChangeWriter Should receive the next message in the processing line")

        chgtWriter.expectMsg(ConceptUpdatePropagation("http://thesaurus.iadb.org/publicthesauri/70212f5c-229d-448b-b69e-79fb25a6bd9e", Instant.parse("2016-07-22T18:04:37Z")))

      When("the ChangeWriter Acknowledges back")

        chgtWriter.reply(ACK)

      Then("the ChangeWriter should receive and acknowledge exactly all the remaining message and not one more")

        val received = chgtWriter.receiveWhile(max = processingLag * 5) {
          case propagationOp: PropagationOp => log.info(s"Writer received: ${propagationOp} \n") ; chgtWriter.reply(ACK); propagationOp
        }

        log.debug(received.toString())

        received.size should be(13)

      //killActors(res, chgtActor, chgtFetcher.ref, chgtWriter.ref)
      //deleteStorageLocations()

    }


    scenario("A ChangeSetActor fetch new ChangeSet from a Fetcher Actor that return 1 mock ChangeSet, then an Empty ChangeSet and final mock ChangeSet. The ChangeWriter systematically acknowledge Propagation Request"){

      info("--Verifying that even if a NoAvailableChangeSet Occur, the ChangeSetActor does its next FetchNewChangeSet(time) from the time of the last change of the preceding ChangeSet Propagated\n")



      Given(s"a ChangeSetActor scheduled to fetch messages every ${requestInterval}, a ChangeFetcher and a ChangeWriter that acknowledge every write request")

        val chgtWriter  = TestProbe()
        chgtWriter.setAutoPilot(new TestActor.AutoPilot {
          def run(sender: ActorRef, msg: Any): TestActor.AutoPilot =
          {
            log.info(s"\nWriter received: ${msg} | Replying with ACK\n")
            sender ! ACK
            TestActor.KeepRunning
          }
        })

        val chgtFetcher    = TestProbe()
        val fromTime       = Instant.now().truncatedTo(ChronoUnit.SECONDS)
        val injectorModule = new ChangePropagationModuleTest(system, fromTime, requestInterval)
        val chgtActor      = injectorModule.getChangePropagationActor(chgtWriter.ref.taggedWith[Writer], chgtFetcher.ref.taggedWith[Fetcher])

      When("all are started")


      Then(s"within ${requestInterval + processingLag} the ChangeFetcher should receive a 1st FetchNewChangeSet(fromTime) with the fromTime initially supplied to the ChangeSetActor: ${fromTime}")

        chgtFetcher.expectMsg(requestInterval + processingLag, FetchNewChangeSet(fromTime))

      When("the ChangeFetcher replies with a mock ChangeSet")

        chgtFetcher.reply(NewChangeSet(ChangeSet1))

      Then(s"within ${processingLag * ChangeSet1.size} the ChangeFetcher should receive a 2nd FetchNewChangeSet(fromTime) with fromTime = 2016-08-01T22:20:20Z the most recent Change propagated ")

        chgtFetcher.expectMsg(processingLag * ChangeSet1.size, FetchNewChangeSet(Instant.parse("2016-08-01T22:20:20Z")))

      When("the ChangeFetcher replies with a NoAvailableChangeSet")

        chgtFetcher.reply(NoAvailableChangeSet)

      Then(s"within ${requestInterval + processingLag} the ChangeFetcher should receive a 3rd FetchNewChangeSet(fromTime) with fromTime = 2016-08-01T22:20:20Z the most recent Change propagated")

        chgtFetcher.expectMsg(requestInterval + processingLag, FetchNewChangeSet(Instant.parse("2016-08-01T22:20:20Z")))

      When("the ChangeFetcher replies with a 2nd mock ChangeSet with dates of Changes that are more recent")

        chgtFetcher.reply(NewChangeSet(ChangeSet2))

      Then(s"within ${processingLag * ChangeSet1.size} the ChangeFetcher should receive a 4th FetchNewChangeSet(fromTime) with fromTime = 2016-08-02T18:33:21Z the most recent Change propagated")

        chgtFetcher.expectMsg(processingLag * ChangeSet1.size, FetchNewChangeSet(Instant.parse("2016-08-02T18:33:21Z")))

    }


    scenario("A ChangeSetActor fetch new ChangeSet from a Fetcher Actor that return 2 different mock ChangeSets, while the ChangeSet Actor is restarted between the two requests"){

      info("--Verifying that even if a Fault/Restart occur, the ChangeSetActor does its next FetchNewChangeSet(time) from the time of the last change of the preceding ChangeSet Propagated\n")


      Given("a ChangeSetActor scheduled to fetch messages every 10 seconds, a ChangeFetcher and a ChangeWriter that acknowledge every write request")

      val chgtWriter  = TestProbe()
      chgtWriter.setAutoPilot(new TestActor.AutoPilot {
        def run(sender: ActorRef, msg: Any): TestActor.AutoPilot =
        {
          log.info(s"\nWriter: received ${msg} | Replying with ACK\n")
          sender ! ACK
          TestActor.KeepRunning
        }
      })

      val chgtFetcher    = TestProbe()
      val fromTime       = Instant.now().truncatedTo(ChronoUnit.SECONDS)
      val injectorModule = new ChangePropagationModuleTest(system, fromTime, requestInterval)
      val chgtActor      = injectorModule.getChangePropagationActor(chgtWriter.ref.taggedWith[Writer], chgtFetcher.ref.taggedWith[Fetcher])

      When("all are started")


      Then(s"within 10 seconds the ChangeFetcher should receive a 1st FetchNewChangeSet(fromTime) with the fromTime initially supplied to the ChangeSetActor: ${fromTime}")

        chgtFetcher.expectMsg(15 seconds, FetchNewChangeSet(fromTime))

      When("the ChangeFetcher replies with a mock ChangeSet")

        chgtFetcher.reply(NewChangeSet(ChangeSet1))

      Then("within 10seconds the ChangeFetcher should receive a 2nd FetchNewChangeSet(fromTime) with fromTime = 2016-08-01T22:20:20Z the most recent Change propagated ")

        chgtFetcher.expectMsg(15 seconds, FetchNewChangeSet(Instant.parse("2016-08-01T22:20:20Z")))

      When("the ChangeSet actor is Restarted")

      //Thread.sleep(2000) // Sleep to ensure that the message is received by the ChgtActor
        killActors(chgtActor)
        Thread.sleep(2000)
        injectorModule.getChangePropagationActor(chgtWriter.ref.taggedWith[Writer], chgtFetcher.ref.taggedWith[Fetcher])
        Thread.sleep(2000)

      Then("within 10seconds the ChangeFetcher should receive a 3rd FetchNewChangeSet(fromTime) with fromTime = 2016-08-01T22:20:20Z the most recent Change propagated ")

        chgtFetcher.expectMsg(15 seconds, FetchNewChangeSet(Instant.parse("2016-08-01T22:20:20Z")))

      When("the ChangeFetcher replies with a 2nd mock ChangeSet with dates of Changes that are more recent")

        chgtFetcher.reply(NewChangeSet(ChangeSet2))

      Then("within 10seconds the ChangeFetcher should receive a 4th FetchNewChangeSet(fromTime) with fromTime = 2016-08-02T18:33:21Z the most recent Change propagated ")

        chgtFetcher.expectMsg(15 seconds, FetchNewChangeSet(Instant.parse("2016-08-02T18:33:21Z")))

    }


    scenario("A ChangeSetActor fetch new ChangeSet from a Fetcher Actor that return 1 plain ChangeSet and 2 Empty ChangeSet"){


      Given("a ChangeSetActor scheduled to fetch messages every 10 seconds, a ChangeFetcher and a ChangeWriter that acknowledge every write request")

       val chgtWriter  = TestProbe()
       chgtWriter.setAutoPilot(new TestActor.AutoPilot {
         def run(sender: ActorRef, msg: Any): TestActor.AutoPilot =
         {
           println(s"\nWriter: received ${msg} | Replying with ACK\n")
           sender ! ACK
           TestActor.KeepRunning
         }
       })

       val chgtFetcher = TestProbe()
       val fromTime    = Instant.now().truncatedTo(ChronoUnit.SECONDS)
      val injectorModule = new ChangePropagationModuleTest(system, fromTime, requestInterval)
      val chgtActor      = injectorModule.getChangePropagationActor(chgtWriter.ref.taggedWith[Writer], chgtFetcher.ref.taggedWith[Fetcher])

      When("all are started")

      Then(s"the ChangeFetcher should receive request of new changeSet from the ChangeSetActor within ${(requestInterval + processingLag)}")


        var changesetSNum = 1

        val received      = chgtFetcher.receiveWhile((requestInterval + processingLag)*3) {

          case msg @ FetchNewChangeSet(m) => {
            println(s"\nFetcher: received FetchNewChangeSet(${m}) \n")
            if (changesetSNum == 1) {
              chgtFetcher.reply(NewChangeSet(changeSet))
              changesetSNum += 1
              msg
            }
            else {
              chgtFetcher.reply(NoAvailableChangeSet)
              msg
            }
          }

         }

       received.size should be (3)

      And("The Last FetchNewChangeSet(m) should be such as m is the date of the most recent Change propagated m = 2016-08-02T18:33:21Z")

       received.last should be (FetchNewChangeSet(Instant.parse("2016-08-02T18:33:21Z")))

   }


  }
}
