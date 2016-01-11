package org.iadb.poolpartyconnector.dspacextension.dspacedataoperationadaptation.consumeroperation

import org.dspace.content.DCValue
import org.iadb.poolpartyconnector.dspacextension.dspacedataoperationadaptation.DspaceDcValueUtils
import org.iadb.poolpartyconnector.thesaurusoperation.JsonProtocolSpecification.LanguageLiteral
import org.scalactic.{Equality, Equivalence}
import org.scalatest.{Matchers, GivenWhenThen, FunSpec}

/**
 * Created by Daniel Maatari Okouya on 8/4/15.
 */
class ThesaurusConsumerUtilOperationSpec extends FunSpec with GivenWhenThen with Matchers {


  /**
   * Defines an implicit equality for the DCValue Class to be used in the test.
   */
  implicit val DCValueEq = new Equality[DCValue] {

    override def areEqual(a: DCValue, b: Any): Boolean = {
      b match {

        case p: DCValue => {(p.value == a.value) && (p.authority == a.authority) && (p.schema == a.schema) && (p.element == a.element) && (p.qualifier == a.qualifier) && (p.language== a.language) && (p.confidence == a.confidence)}
        case _ => false

      }
    }
  }


  describe("The Dspace Consumer Util") {

    describe ("The Update the DCValue.Value of a List of DcValue with Uri from a List of Uris") {


      it("should return the List of DCValues updated with the Uris for their Value field") {


        Given("The a list of DcValues and an equivalent list in size of Uris")

          info("(\"subject\", null, \"animal1\", \"en\", \"dc\", \"suggest:animal1\", -2)")
          info("(\"subject\", null, \"animal2\", \"en\", \"dc\", \"suggest:animal2\", -2)")
          info("\"http://thesaurus.iadb.org/publicthesauri/12345\", \"http://thesaurus.iadb.org/publicthesauri/6666\"")

          val dcValues = List(DspaceDcValueUtils.getDCValue("subject", null, "animal1", "en", "dc", "suggest:animal1", -2),
                              DspaceDcValueUtils.getDCValue("subject", null, "animal2", "en", "dc", "suggest:animal2", -2))

          val uris    = List("http://thesaurus.iadb.org/publicthesauri/12345", "http://thesaurus.iadb.org/publicthesauri/6666")



        When("calling the Consumer Util update")

          val updatedDCValues = ConsumerUtil.copyDCValuesWithSuggestionsUris(dcValues, uris)


        Then("the updated DCValues should contain the original DCValues updated with their index corresponding Uris")


          updatedDCValues should (
            contain (DspaceDcValueUtils.getDCValue("subject", null, "http://thesaurus.iadb.org/publicthesauri/12345", "en", "dc", "http://thesaurus.iadb.org/publicthesauri/12345", -2))
              and contain (DspaceDcValueUtils.getDCValue("subject", null, "http://thesaurus.iadb.org/publicthesauri/6666", "en", "dc", "http://thesaurus.iadb.org/publicthesauri/6666", -2))
            )

      }

    }


    describe("The Creation of a list of DCValue modeled after a DCValueBluePrint and a list of Language Literals for their Value and Language field") {

      it("should return a list of DCValues modeled after the bluePrint for each language Literals.") {

        info("The Language Literal shall provide the DcValue.Value and the DcValue.Language")


          Given("A DCValue BluePrint and a List of Language Literals")

            val languageLiterals = List(LanguageLiteral("label1", "en"), LanguageLiteral("label2", "es"), LanguageLiteral("label3", "en"))

            val dcValueBluePrint = DspaceDcValueUtils.getDCValue("subject", null, "animal1", "en", "dc", null, 0);


          When("When submitted to the The Dspace Thesaurus Consumer Util required Operation")

            val NewDcValues = ConsumerUtil.createDcValues(dcValueBluePrint, languageLiterals)


          Then("The size of the new DcValue List shall equal the size of the List of Language Literral")

            NewDcValues.size should be (languageLiterals.size)

          And("Each DcValue shall be modeled after the blueprint and one language literal for its value and language")

            NewDcValues should contain allOf (
              DspaceDcValueUtils.getDCValue("subject", null, "label1", "en", "dc", null, 0),
              DspaceDcValueUtils.getDCValue("subject", null, "label2", "es", "dc", null, 0),
              DspaceDcValueUtils.getDCValue("subject", null, "label3", "en", "dc", null, 0)
              )


      }


    }
  }





}
