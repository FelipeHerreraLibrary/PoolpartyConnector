package org.iadb.poolpartyconnector.testactor

import SilentActor._
import akka.actor.{ActorSystem, Props}
import akka.testkit.{TestActorRef, TestKit}
import org.scalatest.{FeatureSpecLike, GivenWhenThen, Matchers}

/**
  * Created by Daniel Maatari Okouya on 5/9/16.
  */
class SilentActorTest extends TestKit(ActorSystem("testSystem")) with FeatureSpecLike with Matchers with GivenWhenThen with StopSystemAfterAllFixture {



  feature("Testing Actor Basic") {


    scenario("Single Thread Test") {

      Given("a Silent Actor")
        val silentActor = TestActorRef[SilentActor]


      When("it receive a message")
        silentActor ! SilentMessage("whisper")

      Then("it should change its state")

        silentActor.underlyingActor.state should contain ("whisper")


    }

    scenario("Multi-threaded Test") {

      Given("a Silent Actor")

        val silentActor = system.actorOf(Props[SilentActor])

      When("it is sent 2 messages and then requested its state")
        silentActor ! SilentMessage("whisper1")
        silentActor ! SilentMessage("whisper2")
        silentActor ! Getstate(testActor)


      Then("it should return the 2 messages")

        expectMsg(List("whisper1", "whisper2"))

    }


  }


}
