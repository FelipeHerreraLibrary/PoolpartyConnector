package org.iadb.poolpartyconnector.conceptsrecommendation

import akka.actor.ActorSystem
import org.scalatest.{BeforeAndAfterAll, Suite}

/**
 * Created by Daniel Maatari Okouya on 6/9/15.
 */
trait TestActorSystemFixture extends BeforeAndAfterAll { this: Suite =>

  lazy val system = ActorSystem("Test-Actor-System")

  override def beforeAll(): Unit = {

    super.beforeAll()
  }

  override def afterAll(): Unit = {

    system.shutdown();
    super.afterAll()

  }

}
