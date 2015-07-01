package org.iadb.poolpartyconnector.conceptsrecommendation

import java.io.File
import java.nio.file.Files

import org.scalatest.{FeatureSpec, Matchers, GivenWhenThen}

/**
 *  Created by Daniel Maatari Okouya on 6/2/15.
 */
class RecommendRevelevantConceptsFeatureSpec extends FeatureSpec with Matchers with GivenWhenThen with TestActorSystemFixture {



  feature("Classifying a content") {


    scenario("a Content as Input stream is submitted for classification") {

      //pending

      Given("a content as inputStream and the poolparty Service of classification")


        val in = Files.newInputStream(new File(getClass.getResource("/UNWOMEN_surveyreport_ADVANCE_16Oct-short.pdf").toURI).toPath)

        val classificationService = new RelevantConceptsRecommendationServicePoolPartyImpl(system)


      When("the content is submitted to the classification service for metadata recommendation")

        val recommendatedConcepts = classificationService.recommendMetadata(in)


      Then("the recommendedConcepts should contain a non empty list of suggested concept")

        recommendatedConcepts.document should not be(None)


    }

  }
}
