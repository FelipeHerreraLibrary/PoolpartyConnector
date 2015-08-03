package org.iadb.poolpartyconnector.thesaurusoperation

import org.scalatest.{Matchers, GivenWhenThen, FeatureSpec}

/**
 * Created by Daniel Maatari Okouya on 8/2/15.
 */
class SuggestFreeConceptFeautureSpec extends FeatureSpec with ThesaurusCacheServiceFixture with GivenWhenThen with Matchers {




  feature("The creation of suggested Concept in the Thesaurus by the ThesarusServiceClient") {




    scenario("A new PreferedLabel in the default language (english) together with a scheme are provided such that to create a suggested Concept within that scheme") {



      Given("A new PrefLabel in english and a targeted scheme")

        val suggestedPrefLabel = "SuggestedConcept3"
        val lang = "en"
        val scheme = "http://thesaurus.iadb.org/publicthesauri/IdBTopics"
        val service = cache

      When("")

        val suggestedCocneptUri = service.createSuggestedFreeConcept(suggestedPrefLabel, lang, scheme, false)



      Then("")

        suggestedCocneptUri should not be("")


    }







  }

}
