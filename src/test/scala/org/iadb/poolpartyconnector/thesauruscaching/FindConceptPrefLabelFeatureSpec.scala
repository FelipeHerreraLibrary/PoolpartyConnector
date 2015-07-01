package org.iadb.poolpartyconnector.thesauruscaching

import org.scalatest.{FeatureSpec, fixture, Matchers, GivenWhenThen}

/**
 * Created by Daniel Maatari Okouya on 6/21/15.
 */
class FindConceptPrefLabelFeatureSpec extends FeatureSpec with ThesaurusCacheServiceFixture with GivenWhenThen with Matchers {


  feature("ThesaurusService Concept Prefered Label Resolution") {


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


    scenario("The URI of an existing Concept with both a spanish and english prefered Label is supplied to the CacheService for the spanish Label Resolution") {


      Given("An URI that exists in the Thesaurus Server")

        info("uri = \"http://thesaurus.iadb.org/publicthesauri/49597111072074074\" and the corresponding spanish PrefLabel: \"Sector privado\"")

        val existingUri = "http://thesaurus.iadb.org/publicthesauri/49597111072074074"
        val existingPrefLabel = "Sector privado"
        val service = cache


      When("The URI is passed to the CacheService for Resolution")

        val prefLabel = service.getPrefLabelforConcept(existingUri, "es")


      Then("it should return the Prefered Label  \"Sector privado\"")

        prefLabel should be (existingPrefLabel)
    }


    scenario("The Prefered Label of an existing Concept does not exist in the Supplied Language. The Label of the Default Language is returned") {


      Given("the URI of an existing concept and a language for which it does not have a Label")

        info("uri = \"http://thesaurus.iadb.org/publicthesauri/108609331907140813946589\", Language: \"es\", DefaultPrefLabel: \"Goya, Daniel\"")

        val existingUri = "http://thesaurus.iadb.org/publicthesauri/108609331907140813946589"
        val lang = "es"
        val defaultPrefLabel = "Goya, Daniel"
        val service = cache


      When("The URI is passed to the CacheService for Resolution with the language for which it does not have a label")

        val prefLabel = service.getConceptPrefLabelWithDefaultLangfallback(existingUri, "es")


      Then("it should return the Prefered Label in the default language  \"Goya, Daniel\"")

        prefLabel should be (defaultPrefLabel)
    }


  }

}
