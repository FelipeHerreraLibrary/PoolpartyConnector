package org.iadb.poolpartyconnector.thesaurusoperation

import org.scalatest.{Suite,  BeforeAndAfterAll}

/**
 * Created by Daniel Maatari Okouya on 6/21/15.
 */
trait ThesaurusCacheServiceFixture extends BeforeAndAfterAll {this: Suite =>


  val cache: ThesaurusCacheService = ThesaurusServiceModuleTest.service

  override def beforeAll(): Unit = {
    super.beforeAll()
  }


  override def afterAll(): Unit = {

    ThesaurusServiceModuleTest.system.shutdown()
    super.afterAll()

  }

}
