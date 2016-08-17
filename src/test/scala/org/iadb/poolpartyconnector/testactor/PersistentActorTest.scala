package org.iadb.poolpartyconnector.testactor

import akka.actor.{ActorSystem, PoisonPill}
import akka.persistence._
import org.iadb.poolpartyconnector.testactor.MyPersistentActor.{GetLastMsg, LastMsg, NewMessage}
import org.scalatest.{FeatureSpecLike, GivenWhenThen, Matchers}

import scala.concurrent.Await

/**
  * Created by Daniel Maatari Okouya on 5/24/16.
  */
class PersistentActorTest extends PersistenceSpec(ActorSystem("TestSystem"))

  with PersistenceCleanup {


  feature("Persisting State") {

    scenario("An Actor with with no previous message is asked his Last Received Message") {

      Given("a persistent actor with no previous message")

        val prsts = system.actorOf(MyPersistentActor.props)

      When("asked its last message")

        prsts ! MyPersistentActor.GetLastMsg

      Then("it should respond with NoLastMsg")

        expectMsg(MyPersistentActor.NoLastMsg)

    }


    scenario("An Actor Receive messages, save the last message he receive, get stop and restart, and ask its last message") {

      Given("A persistent actor with no previous message")

        val prsts = system.actorOf(MyPersistentActor.props)

      When("it get send 3 messages and asked")

        prsts ! NewMessage("msg10")
        prsts ! NewMessage("msg20")
        prsts ! NewMessage("msg30")

        prsts ! GetLastMsg

      Then("the Expected Msg should be the last one sent")

        expectMsg(LastMsg("msg30"))


      When("it get killed and resurected")

        //killActors(prsts)
        prsts ! PoisonPill
        val resurectedprsts = system.actorOf(MyPersistentActor.props)

      And("it is asked its last message")

        resurectedprsts ! GetLastMsg


      Then("it should respond with the last message that was sent to it before it was restarted")

        expectMsg(LastMsg("msg30"))

    }








  }

}
