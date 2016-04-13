package org.iadb.poolpartyconnector.thesaurusoperation

import org.scalatest.{FeatureSpec, GivenWhenThen, Matchers}

/**
  * Created by Daniel Maatari Okouya on 4/13/16.
  */
class FindAuthorRepecIdFeatureSpec extends FeatureSpec with ThesaurusConsumerServiceFixture with GivenWhenThen with Matchers  {



  feature("Find an Author RepecID") {

    info("As a client I want to be able to retreive the RepecID of an author")
    info("and send this information when uploading item record to repec")
    info("in order to make sure that our item and author are fully connected in repec")


    scenario("a client request an Author RepecId by supplying the URI of an Author that has a RepecId in the thesaurus") {

      Given("The URI of a the URI of an Author that has RepecId in the thesaurus: \"pbe356\"")

        val authorUri = "http://thesaurus.iadb.org/publicthesauri/105256297968197264076392"

        val thesaurusConsumerService = cache

      When("a request to obtain its repecid is made to the thesaurusConsumerService")

        val repecId = thesaurusConsumerService.getauthorRepecId(authorUri)

      Then(s"the return $repecId should equal pbe356")

        repecId should be ("pbe356")
    }


    scenario("a client request an Author RepecId by supplying the URI of an Author that do not have RepecId in the thesaurus") {

      pending
    }


    scenario("a client request an Author RepecId by supplying the URI of a concept that is not an Author") {

      pending
    }


    scenario("a client request an Author RepecId by supplying the URI of a non existing Concept") {

      pending
    }

  }


}
