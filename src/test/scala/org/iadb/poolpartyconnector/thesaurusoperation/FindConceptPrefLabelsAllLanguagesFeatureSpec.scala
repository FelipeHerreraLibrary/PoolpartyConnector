package org.iadb.poolpartyconnector.thesaurusoperation

import org.iadb.poolpartyconnector.thesaurusoperation.JsonProtocolSpecification.LanguageLiteral
import org.scalatest.{FeatureSpec, Matchers, GivenWhenThen}

/**
  * Created by Daniel Maatari Okouya on 11/9/15.
  */
class FindConceptPrefLabelsAllLanguagesFeatureSpec extends FeatureSpec with ThesaurusConsumerServiceFixture with Matchers with GivenWhenThen {


  feature("Resolution of a Concept Preferred Labels in all Languages") {


    info("As a client of the ThesaurusConsumerService, I want to be able to resolve at once the Preferred Labels of a Concept in all Languages")


    scenario("The URI of a Concept that exist in the thesaurus with 4 labels is submitted to the ThesaurusConsumer for Resolution") {

      Given("The URI \"http://thesaurus.iadb.org/publicthesauri/15340669183286907\" of an existing Concept")

        val conceptUri = "http://thesaurus.iadb.org/publicthesauri/15340669183286907"

      When("The URI is submitted to the Thesaurus Consumer Service for resolution")

        val labels = cache.getAllLangPrefLabels(conceptUri)

      Then("it should return the following list of language literals: ")

        info("[United States, @en]")
        info("[Estados Unidos, @es]")
        info("[Estados Unidos, @pt]")
        info("[États-Unis, @fr]")

        labels should contain allOf (
          LanguageLiteral("United States","en"),
          LanguageLiteral("Estados Unidos","es"),
          LanguageLiteral("Estados Unidos","pt"),
          LanguageLiteral("États-Unis","fr")
        )


    }

    scenario("The URI of a Concept that does not exist in the thesaurus is submitted to the ThesaurusConsumer for Resolution") {

      Given("The URI of a non existing concept")

        val conceptUri = "http://thesaurus.iadb.org/publicthesauri/20499449456021084801192"

      When("The URI is submitted to the Thesaurus Consumer Service for resolution")

        val labels = cache.getAllLangPrefLabels(conceptUri)

      Then("the return list of labels i.e. language literals should be empty")

        labels should be (empty)

    }
  }
}
