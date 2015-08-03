package org.iadb.poolpartyconnector.conceptsrecommendation

import java.io.File
import java.nio.file.Files

import org.iadb.poolpartyconnector.dspacextension.dspaceconnectorconfiguration.{FieldSettings, DspaceDspacePoolPartyConnectorSettingImpl}
import org.scalatest.{Inspectors, FeatureSpec, Matchers, GivenWhenThen}
import Inspectors._
/**
 *  Created by Daniel Maatari Okouya on 6/2/15.
 */
class RecommendRevelevantConceptsFeatureSpec extends FeatureSpec with Matchers with GivenWhenThen with TestActorSystemFixture {


  //private def check


  feature("Extracting the Relevant Concepts of a document content") {


    /*scenario("As the Client I want to make sure that if a valid well-known Content is submitted, the extraction returns at least one concept from the Taxonomy") {

      //pending

      Given("a content as inputStream and the poolparty Service of classification")

        val loadedConnectorSettings = DspaceDspacePoolPartyConnectorSettingImpl ("file:///Users/maatary/Dev/IdeaProjects/PoolpartyConnector/src/test/resources/poolpartydspace.conf")

        val in                      = Files.newInputStream(new File(getClass.getResource("/UNWOMEN_surveyreport_ADVANCE_16Oct.pdf").toURI).toPath)

        val RecommendationService   = new RelevantConceptsRecommendationServicePoolPartyImpl(system, loadedConnectorSettings)


      When("the content is submitted to the classification service for metadata recommendation")

        val recommendatedConcepts = RecommendationService.recommendMetadata(in)


      Then("the recommendedConcepts should contain a non empty list of suggested concept")

        recommendatedConcepts.document should not be(None)


    }*/

    scenario("As a connector service client I want to obtain an extraction results that conform to the configuration of the connector in terms of the number of concepts and terms to extract per fields.") {

      Given("a connector Recommendation Service initialized with the following connector Settings: ")

        val loadedConnectorSettings = DspaceDspacePoolPartyConnectorSettingImpl ("file:///Users/maatary/Dev/IdeaProjects/PoolpartyConnector/src/test/resources/poolpartydspace.conf")

        val in                      = Files.newInputStream(new File(getClass.getResource("/2012 Annual Report - The Year in Review.pdf").toURI).toPath)

        val RecommendationService   = new RelevantConceptsRecommendationServicePoolPartyImpl(system, loadedConnectorSettings)



      When("a document is submitted to the RecommendationService")

        val recommendatedConcepts = RecommendationService.recommendMetadata(in, "en")
        val conceptlist           = recommendatedConcepts.document.get.concepts.get
        val extractedfields       = loadedConnectorSettings.fieldsSettingsList.withFilter(e => e.maxConceptsExtraction > 0).flatMap(e=>List(e))


      Then("the resulted extraction should contain")

        forAll(extractedfields) { extractedfield =>
            extractedfield.maxConceptsExtraction should be >= conceptlist.withFilter(e => e.conceptSchemes.exists(x => x.uri == extractedfield.scheme)).flatMap(e=>List(e)).size
        }

      conceptlist foreach{ e => println(e); println("----------------")}

    }

  }
}
