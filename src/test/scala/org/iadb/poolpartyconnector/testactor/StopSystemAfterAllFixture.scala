package org.iadb.poolpartyconnector.testactor

import akka.testkit.TestKit
import org.scalatest.{BeforeAndAfterAll, Suite}

/**
  * Created by Daniel Maatari Okouya on 5/9/16.
  */
trait StopSystemAfterAllFixture extends BeforeAndAfterAll { this:  TestKit with Suite =>


  override protected def afterAll(): Unit = {
    super.afterAll();
    system.terminate()

  }

}
