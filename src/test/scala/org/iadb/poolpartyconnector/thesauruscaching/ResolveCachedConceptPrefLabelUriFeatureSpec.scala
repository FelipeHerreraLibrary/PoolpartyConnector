package org.iadb.poolpartyconnector.thesauruscaching

import org.scalatest.{Matchers, GivenWhenThen, FeatureSpec}

/**
 * Created by Daniel Maatari Okouya on 6/21/15.
 */
class ResolveCachedConceptPrefLabelUriFeatureSpec extends FeatureSpec with GivenWhenThen with Matchers {


  feature("Concept Prefered Label Resolution") {


    scenario("A Uri that exists in the Thesaurus Server is supplied to the CacheService for Label Resolution") {

      pending

      Given("An URI that exist in the Thesaurus Server and the CacheService")

        info("uri = \"\" corresponds to PrefLabel: \"\"")

        val existingUri = ""
        val existingPrefLabel = ""
        val service = ThesaurusCacheServiceModule.service


      When("The URI is passed to the CacheService for Resolution")

        val prefLabel = service.getPrefLabelforConcept(existingUri)


      Then("it should return the Prefered Label in the language of choice or None otherwise")


        prefLabel should be (existingPrefLabel)
    }


  }

}
