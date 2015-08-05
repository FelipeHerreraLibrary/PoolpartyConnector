package org.iadb.poolpartyconnector.dspacextension.dspacedataoperationadaptation.consumeroperation

import org.dspace.content.DCValue
import org.iadb.poolpartyconnector.dspacextension.dspacedataoperationadaptation.DspaceDcValueUtils
import org.scalactic.{Equality, Equivalence}
import org.scalatest.{Matchers, GivenWhenThen, FunSpec}

/**
 * Created by Daniel Maatari Okouya on 8/4/15.
 */
class ConsumerOperationSpec extends FunSpec with GivenWhenThen with Matchers {


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

    describe ("when provided with a list of DC Value and corresponding URIs for Update") {

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

  }





}
