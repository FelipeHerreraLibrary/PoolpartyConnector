package org.iadb.poolpartyconnector.testactor

import akka.actor.{ActorSystem, Props}
import akka.testkit.TestKit
import org.scalatest.{FeatureSpecLike, GivenWhenThen, Matchers}

import scala.util.Random

/**
  * Created by Daniel Maatari Okouya on 5/10/16.
  */
class SortingActorTest extends TestKit(ActorSystem("testSystem")) with FeatureSpecLike with Matchers with GivenWhenThen with StopSystemAfterAllFixture {



  feature("SendingActor") {


    scenario("Starting an Actor with an ActorRef in its construction") {


      Given("A Sorting Actor initialized with a receiving actor as the testActor actorRef")

        import SortingActor._

        val sortingActor = system.actorOf(SortingActor.props(testActor))

      When("sending it a list of event")

        val size         = 10
        val maxInclusive = 100000
        val unsorted     = ((1 to size) map { _ => Event(Random.nextInt(maxInclusive))}).toList

        sortingActor ! SortEvent(unsorted)


      Then("The testActor should receive the list of event sorted")

        expectMsgPF () {

          case EventSorted(events:List[Event]) => {

            events.size should be (unsorted.size)

            unsorted.sortBy(_.id) should equal(events)

          }

        }




    }


  }

}
