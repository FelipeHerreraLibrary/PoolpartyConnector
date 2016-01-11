
package org.iadb.poolpartyconnector.thesaurusoperation

import org.scalatest.{Matchers, GivenWhenThen, FeatureSpec}

/**
 * Created by Daniel Maatari Okouya on 8/31/15.
 */
class FindConceptIdbDocTopicFeatureSpec extends FeatureSpec with ThesaurusConsumerServiceFixture with GivenWhenThen with Matchers {


  feature("Finding IdbDocs Topics correspondence for our Topic Concept") {

    info("Each Top Concept in the Thesaurus has IdbDoc Web Topic correspondence")
    info("As Dspace, I want to fetch the corresponding Web Topics of PoolParty Concept in order to submit them to IdbDocs when submiting a document")
    info("If the Topic does not have a WebTopic corespondance I shall take the one of his top Term")


    scenario("A Request for to fetch the Idbdoc code of a non Top Concept is submitted to the ThesaurusOperation Service") {


      Given("The URI of a non Top Concept that exist in Thesarus and the ThesaurusConsumer service")

        val nonTopConceptUri = "http://thesaurus.iadb.org/publicthesauri/43075198238442720"
        //val existingPrefLabel = "Barbados"
        val consumerService = cache

      When("")

      //consumerService.getIdbDocWebTopic(nonTopConceptUri)
      //consumerService.getIdbDocWebTopic(nonTopConceptUri)
      //consumerService.getIdbDocWebTopic(nonTopConceptUri)
        val topics = consumerService.getIdbDocWebTopic(nonTopConceptUri)

      Then("")
        topics should contain allOf ("DU-DUR", "TR-COL", "TR-GST", "TR-PRI", "TR-TFR", "TR-TRP", "TR-TRO")

    }












    scenario(" A Request for to fetch the Idbdoc code of a Top Term is submitted to the ThesaurusOperation Service") {

      pending
    }



  }

}

