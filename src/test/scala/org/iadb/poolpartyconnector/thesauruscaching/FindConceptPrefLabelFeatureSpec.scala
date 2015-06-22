package org.iadb.poolpartyconnector.thesauruscaching

import org.scalatest.{FeatureSpec, fixture, Matchers, GivenWhenThen}

/**
 * Created by Daniel Maatari Okouya on 6/21/15.
 */
class FindConceptPrefLabelFeatureSpec extends FeatureSpec with ThesaurusCacheServiceFixture with GivenWhenThen with Matchers {


  feature("Concept Prefered Label Resolution") {


    scenario("A Uri that exists in the Thesaurus Server is supplied to the CacheService for Label Resolution") {



      Given("An URI that exists in the Thesaurus Server and the CacheService")

        info("uri = \"http://thesaurus.iadb.org/publicthesauri/11203801621176635\" and its corresponding PrefLabel: \"Barbados\"")

        val existingUri = "http://thesaurus.iadb.org/publicthesauri/11203801621176635"
        val existingPrefLabel = "Barbados"
        val service = cache


      When("The URI is passed to the CacheService for Resolution")

        val prefLabel = service.getPrefLabelforConcept(existingUri)


      Then("it should return the Prefered Label  \"Barbados\"")

        prefLabel should be (existingPrefLabel)
    }


  }

}
