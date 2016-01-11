package org.iadb.poolpartyconnector.thesaurusoperation

import org.iadb.poolpartyconnector.thesaurusoperation.JsonProtocolSpecification.LanguageLiteral
import org.scalatest.{Matchers, GivenWhenThen, FeatureSpec}

/**
 * Created by Daniel Maatari Okouya on 8/2/15.
 */
class SuggestFreeConceptFeautureSpec extends FeatureSpec with ThesaurusConsumerServiceFixture with GivenWhenThen with Matchers {




  feature("The creation of suggested Concept in the Thesaurus by the ThesarusServiceClient") {



    scenario("A List of PreferedLabel in the default language (english) together with a scheme are provided such that to create suggested Concepts within that scheme") {



      Given("A new PrefLabel in english and a targeted scheme")

        val suggestedPrefLabels = List(LanguageLiteral("suggested01", "en"), LanguageLiteral("suggested02", "en"), LanguageLiteral("suggested03", "en"), LanguageLiteral("suggested04", "en"))
        val lang = "en"
        val scheme = "http://thesaurus.iadb.org/publicthesauri/IdBTopics"
        val service = cache

      When("")

        val suggestedCocneptUris = service.createSuggestedFreeConcepts(suggestedPrefLabels, scheme, false)


      Then("")

        suggestedCocneptUris should not contain("")


    }




    /*scenario("A List of PreferedLabel in the default language (english) together with a scheme are provided such that to create suggested Concepts within that scheme:Concurent method") {

      pending

      Given("A new PrefLabel in english and a targeted scheme")

      val suggestedPrefLabels = List(LanguageLiteral("suggested011", "en"), LanguageLiteral("suggested022", "en"), LanguageLiteral("suggested033", "en"), LanguageLiteral("suggested044", "en"))
      val lang = "en"
      val scheme = "http://thesaurus.iadb.org/publicthesauri/IdBTopics"
      val service = cache

      When("")

      val suggestedCocneptUris = service.createSuggestedFreeConceptsConcurrent(suggestedPrefLabels, scheme, false)


      Then("")

      suggestedCocneptUris should not contain("")
    }*/

  }

}
