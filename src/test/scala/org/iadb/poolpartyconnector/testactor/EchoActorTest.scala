package org.iadb.poolpartyconnector.testactor

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import org.dspace.content.Item
import org.dspace.core.Context
import org.dspace.utils.DSpace
import org.iadb.poolpartyconnector.testactor.EchoActor.EchoMsg
import org.scalatest.{FeatureSpecLike, GivenWhenThen, Matchers}

//import org.dspace.utils.DSpace

/**
  * Created by Daniel Maatari Okouya on 5/12/16.
  */
class EchoActorTest extends TestKit (ActorSystem("testSystem")) with FeatureSpecLike
  with Matchers with GivenWhenThen with StopSystemAfterAllFixture with ImplicitSender {


  feature("An echo actor send back what he receive to the testActor as implicit Actor") {

    scenario("sending a message") {

      Given("an Echo Actor")
        val echoActor = system.actorOf(Props[EchoActor])
      //new Item(new Context, 1).update()
      When("sending a message to it")
        echoActor ! EchoMsg("hello")
      Then("it shall reply with the same message")
        expectMsg(EchoMsg("hello"))
    }

  }


}
