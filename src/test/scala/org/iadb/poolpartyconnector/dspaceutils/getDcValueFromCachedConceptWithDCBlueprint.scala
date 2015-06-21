package org.iadb.poolpartyconnector.dspaceutils

import org.iadb.poolpartyconnector.thesauruscaching.{CachedConcept, IndexCachedConcept}
import org.scalatest.{Matchers, GivenWhenThen, FunSpec}

/**
 * Created by Daniel Maatari Okouya on 6/21/15.
 *
 *
 */
class getDcValueFromCachedConceptWithDCBlueprint extends FunSpec with GivenWhenThen with Matchers {



  info(
    """As the System I want to get a CachedConcept as a DspaceDCValue
      |so I can add it to item Metadata, thereby allowing the concepts to be updated without affecting my item records""".stripMargin)


  describe("DspaceDcValueUtils") {

    describe("asDCValue") {

      it("should return the supplied CachedConcept converted as DCValue according to the Supplied DCValue Blueprint ") {

        Given("a DC Value and a CachedConcept")

          val dcvalueblueprint = DspaceDcValueUtils.getDCValue("element", "qualifier", "value", "language", "schema", "authority", 500)
          val cachedconcept    = IndexCachedConcept("http://iadb.org", "authority=", 600);

        When("passed to the asDCValue procedure")

          val res = DspaceDcValueUtils.asDCValue(dcvalueblueprint, Some(cachedconcept))

        Then("the returned DcValue should contained the blueprint value updated with the CachedConceptValue")

          res should have (
            'element (dcvalueblueprint.element),
            'qualifier (dcvalueblueprint.qualifier),
            'language (dcvalueblueprint.language),
            'schema (dcvalueblueprint.schema),
            'value (cachedconcept.uri),
            'authority (cachedconcept.authority),
            'confidence (cachedconcept.confidence)
          )

      }

      it ("should return a copy of the blueprint DCValue if the CachedConcept is None") {


        Given("a DC Value and a None CachedConcept")

          val dcvalueblueprint = DspaceDcValueUtils.getDCValue("element", "qualifier", "value", "language", "schema", "authority", 700)
          val cachedconcept    = None

        When("passed to the asDCValue procedure")

          val res = DspaceDcValueUtils.asDCValue(dcvalueblueprint, cachedconcept)

        Then("the returned DcValue should be a copy of the blueprint DCValue")

          res should have (
            'element (dcvalueblueprint.element),
            'qualifier (dcvalueblueprint.qualifier),
            'language (dcvalueblueprint.language),
            'schema (dcvalueblueprint.schema),
            'value (dcvalueblueprint.value),
            'authority (dcvalueblueprint.authority),
            'confidence (dcvalueblueprint.confidence)
          )



      }
    }
  }

}
