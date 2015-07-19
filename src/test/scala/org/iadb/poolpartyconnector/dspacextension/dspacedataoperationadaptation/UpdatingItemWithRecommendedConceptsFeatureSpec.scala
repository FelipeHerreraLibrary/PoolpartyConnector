package org.iadb.poolpartyconnector.dspacextension.dspacedataoperationadaptation

import org.iadb.poolpartyconnector.conceptsrecommendation.JsonProtocolSpecification._
import org.iadb.poolpartyconnector.dspacextension.dspaceconnectorconfiguration.{DspaceDspacePoolPartyConnectorSettingImpl, DspacePoolPartyConnectorSettings}
import org.iadb.poolpartyconnector.dspacextension.dspacedatamodeladaptation.DspaceItemWrapper
import org.iadb.poolpartyconnector.dspacextension.dspacedataoperationadaptation.dspaceitemoperation.DspaceItemOperatorService
import org.iadb.poolpartyconnector.dspacextension.dspacedataoperationadaptation.schememapping.{SchemeMetadatumMappingService, SchemeMetadatumMappingServiceImpl}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FeatureSpec, GivenWhenThen, Matchers}

/**
 * Created by Daniel Maatari Okouya on 6/6/15.
 */
      //UpdatingItemWithRecommendedConceptsFeatureSpec
class UpdatingItemWithRecommendedConceptsFeatureSpec extends FeatureSpec with MockFactory with Matchers with GivenWhenThen {


    scenario("An ItemWrapper metadata is to be updated with a list of Concept returned by the recommendation service"){



      Given("a set of recommended Concepts, an ItemWrapper and the Dspace Item Operator Service, initialized with the SchemeMetadatumMappingService")

        val concepts  = List(Concept("1DCE2E49-7DD8-0001-524C-1A1B14A0141A", "http://thesaurus.iadb.org/publicthesauri/11780158025330376", 80, "Chile",
                                     "1DCE2E49-7DD8-0001-524C-1A1B14A0141A:http://thesaurus.iadb.org/publicthesauri/11780158025330376@es", "es",
                                     List(ConceptScheme("IdBCountries", "http://thesaurus.iadb.org/publicthesauri/IdBCountries")), 28),
                             Concept("1DCE2E49-7DD8-0001-524C-1A1B14A0141A", "http://thesaurus.iadb.org/publicthesauri/9435479308461137", 100, "Inversión",
                                     "1DCE2E49-7DD8-0001-524C-1A1B14A0141A:http://thesaurus.iadb.org/publicthesauri/9435479308461137@es", "es",
                                     List(ConceptScheme("IdBTopics", "http://thesaurus.iadb.org/publicthesauri/IdBTopics")), 28),
                             Concept("1DCE2E49-7DD8-0001-524C-1A1B14A0141A", "http://thesaurus.iadb.org/publicthesauri/57176084515534345392272", 1, "economist Intelligence Unit (EIU)",
                                     "1DCE2E49-7DD8-0001-524C-1A1B14A0141A:http://thesaurus.iadb.org/publicthesauri/57176084515534345392272@es", "es",
                                     List(ConceptScheme("IdBAuthors", "http://thesaurus.iadb.org/publicthesauri/IdBAuthors")), 5))

        val freeTerms = List(FreeTerm("asociaciones público-privadas en latinoamérica", 82, 42),
                             FreeTerm("ambiente para asociaciones público-privadas", 79, 41),
                             FreeTerm("asociaciones para el progreso", 69, 41))

        val document       = Document(Some(concepts), None)
        val conceptResults = ConceptResults(None, Some(document))
        val item           = mock[DspaceItemWrapper]

        val loadedConnectorSettings   = DspaceDspacePoolPartyConnectorSettingImpl("file:///Users/maatary/Dev/IdeaProjects/PoolpartyConnector/src/test/resources/poolpartydspace.conf")
        val schemeMapping: SchemeMetadatumMappingService = SchemeMetadatumMappingServiceImpl(loadedConnectorSettings)
        val dspaceItemOperatorService = DspaceItemOperatorService(schemeMapping)





      Then("the itemWrapper.addMetadata should be called as follows:")


        info("(\"dc\", \"coverage\", \"placename\", \"es\", \"http://thesaurus.iadb.org/publicthesauri/11780158025330376\", \"http://thesaurus.iadb.org/publicthesauri/11780158025330376\", *)")
        (item.addMetadata(_: String, _:String, _:String, _:String, _:String, _:String, _:Int)).
          expects ("dc", "coverage", "placename", "es", "http://thesaurus.iadb.org/publicthesauri/11780158025330376", "http://thesaurus.iadb.org/publicthesauri/11780158025330376", *)

        info("(\"dc\", \"subject\", null, \"es\", \"http://thesaurus.iadb.org/publicthesauri/9435479308461137\", \"http://thesaurus.iadb.org/publicthesauri/9435479308461137\", *)")
        (item.addMetadata(_: String, _:String, _:String, _:String, _:String, _:String, _:Int)).
          expects ("dc", "subject", null, "es", "http://thesaurus.iadb.org/publicthesauri/9435479308461137", "http://thesaurus.iadb.org/publicthesauri/9435479308461137", *)

        info("(\"dc\", \"contributor\", \"author\", \"es\", \"http://thesaurus.iadb.org/publicthesauri/57176084515534345392272\", \"http://thesaurus.iadb.org/publicthesauri/57176084515534345392272\", *)")
        (item.addMetadata(_: String, _:String, _:String, _:String, _:String, _:String, _:Int)).
          expects ("dc", "contributor", "author", "es", "http://thesaurus.iadb.org/publicthesauri/57176084515534345392272", "http://thesaurus.iadb.org/publicthesauri/57176084515534345392272", *)

        (item.update _) expects()

        (item.item _) expects()

      When("requesting the dspaceItemOperatorService to update the item metadata with the recommended concepts")


        dspaceItemOperatorService.updateItemMetadataWithRecommendedConcepts(item, conceptResults)





  }


}
