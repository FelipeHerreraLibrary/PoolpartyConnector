package org.iadb.poolpartyconnector.testactor

import akka.actor.{ActorSystem, Props}
import akka.testkit.{CallingThreadDispatcher, EventFilter, TestKit}
import com.typesafe.config.ConfigFactory
import org.iadb.poolpartyconnector.testactor.SideEffectingActor.Greetings
import org.scalatest.{FeatureSpecLike, GivenWhenThen, Matchers}


/**
  * Created by Daniel Maatari Okouya on 5/12/16.
  */
class SideEffectorActorTest extends TestKit(Greeter01Test.testSystem) with FeatureSpecLike with Matchers with GivenWhenThen with StopSystemAfterAllFixture {


  feature("Testing the SideEffecting Actor") {

    scenario("An Actor Receive a message and perform a side effect, and log it") {

      Given("a sideEffecting actor")

        val dispatcherId = CallingThreadDispatcher.Id
        val greeter = system.actorOf(Props[SideEffectingActor].withDispatcher(dispatcherId))


      When("sent a message")


      Then("upon expecting the log, we should see the side effect that he logs")

      EventFilter.info(message = "Hello World", occurrences = 1).intercept {
        greeter ! Greetings("World")
      }

    }

  }

}


object Greeter01Test {
  val testSystem = {
    val config = ConfigFactory.parseString(
      """
             akka.loggers = [akka.testkit.TestEventListener]
      """
    )
    ActorSystem("testsystem", config)
  }
}