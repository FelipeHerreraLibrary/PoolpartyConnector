package org.iadb.poolpartyconnector.testactor

import akka.actor.ActorSystem
import akka.testkit.TestKit
import org.iadb.poolpartyconnector.testactor.FilteringActor.Event
import org.scalatest.{FeatureSpecLike, GivenWhenThen, Matchers}

/**
  * Created by Daniel Maatari Okouya on 5/12/16.
  */
class FilteringActorTest extends TestKit(ActorSystem("testSystem")) with FeatureSpecLike with Matchers with GivenWhenThen with StopSystemAfterAllFixture {



  feature("Filtering Message to not send duplicates") {


    scenario("An actor is sent multiple message that contains duplicates, filter them before sending it to a receiver actor") {



      Given("a Filtering Actor, and a Receiver Actor")

        val filteringActor = system.actorOf(FilteringActor.props(testActor))


      When("the Filtering actor receive messages that contains duplicates")

        filteringActor ! Event(1)
        filteringActor ! Event(2)
        filteringActor ! Event(2)
        filteringActor ! Event(3)


      Then("the message received by the Receiver Actor should not contains the duplicates one")

       val received = receiveWhile() {
          case Event(id) if id < 5 => id
        }

        received should equal(List(1, 2, 3))


    }





  }

}
