package org.iadb.poolpartyconnector.dspacextension.dspacedataoperationadaptation

import org.scalatest.{FunSpec, GivenWhenThen, Matchers}

/**
 * Created by Daniel Maatari Okouya on 6/19/15.
 */
class MetaDatumToSchemeSpec extends FunSpec with GivenWhenThen with Matchers {


  info("As the system I want to get the scheme that corresponds to a Dspace Metadatum if it exists or None otherwise")

  describe("The SchemeUtil asScheme Operation") {

    it("should return None when a null MetaDatum is passed to it") {

      Given("The SchemeUtil and Null as MetaDatum ")

        val metadatum = null

      When("passed to the findScheme operation")

        val res = DspaceMetaDatumToSchemeUtil.asScheme(metadatum)

      Then("the result shall be none")

        info(s"the operation result was: $res ")
        res should be (None)
    }

    it("should return None when an empty MetaDatum is passed to it") {

      Given("The SchemeUtil and \"\" as MetaDatum ")

        val metadatum = ""

      When("the metadatadum is passed to the SchemeUtil asScheme operation")

        val res = DspaceMetaDatumToSchemeUtil.asScheme(metadatum)

      Then("the result shall be none")

        info(s"the operation result was: $res ")
        res should be (None)
    }

    it("should return \"http://thesaurus.iadb.org/publicthesauri/IdBTopics\" when a \"dc.subject\" MetaDatum is passed to it") {

      Given("The SchemeUtil and \"dc.subject\" as MetaDatum ")

        val metadatum = "dc.subject"

      When("the metadatadum is passed to the SchemeUtil asScheme operation")

        val res = DspaceMetaDatumToSchemeUtil.asScheme(metadatum)

      Then("the result shall be none")

        info(s"the operation result was: $res ")
          res should be (Some("http://thesaurus.iadb.org/publicthesauri/IdBTopics"))
    }

    it("should return \"http://thesaurus.iadb.org/publicthesauri/IdBAuthors\" when a \"dc.contributor.author\" MetaDatum is passed to it") {


      Given("The SchemeUtil and \"dc.contributor.author\" as MetaDatum ")

        val metadatum = "dc.contributor.author"

      When("the metadatadum is passed to the SchemeUtil asScheme operation")

        val res = DspaceMetaDatumToSchemeUtil.asScheme(metadatum)

      Then("the result shall be \"http://thesaurus.iadb.org/publicthesauri/IdBAuthors\"")

        info(s"the operation result was: $res")
        res should be (Some("http://thesaurus.iadb.org/publicthesauri/IdBAuthors"))
    }


    it("should return \"http://thesaurus.iadb.org/publicthesauri/IdBInstitutions\" when a \"dc.contributor.institution\" MetaDatum is passed to it") {


      Given("The SchemeUtil and \"dc.contributor.institution\" as MetaDatum ")

        val metadatum = "dc.contributor.institution"

      When("the metadatadum is passed to the SchemeUtil asScheme operation")

        val res = DspaceMetaDatumToSchemeUtil.asScheme(metadatum)

      Then("the result shall be \"http://thesaurus.iadb.org/publicthesauri/IdBInstitutions\"")

        info(s"the operation result was: $res")
        res should be (Some("http://thesaurus.iadb.org/publicthesauri/IdBInstitutions"))
    }


    it("should return \"http://thesaurus.iadb.org/publicthesauri/IdBCountries\" when a \"dc.coverage.placename\" MetaDatum is passed to it") {


      Given("The SchemeUtil and \"dc.coverage.placename\" as MetaDatum ")

        val metadatum = "dc.coverage.placename"

      When("the metadatadum is passed to the SchemeUtil asScheme operation")

        val res = DspaceMetaDatumToSchemeUtil.asScheme(metadatum)

      Then("the result shall be \"http://thesaurus.iadb.org/publicthesauri/IdBCountries\"")

        info(s"the operation result was: $res")
        res should be (Some("http://thesaurus.iadb.org/publicthesauri/IdBCountries"))
    }

    it("should return \"http://thesaurus.iadb.org/publicthesauri/IdBDepartments\" when a \"iadb.department\" MetaDatum is passed to it") {


      Given("The SchemeUtil and \"iadb.department\" as MetaDatum ")

        val metadatum = "iadb.department"

      When("the metadatadum is passed to the SchemeUtil asScheme operation")

        val res = DspaceMetaDatumToSchemeUtil.asScheme(metadatum)

      Then("the result shall be \"http://thesaurus.iadb.org/publicthesauri/IdBDepartments\"")

        info(s"the operation result was: $res")
        res should be (Some("http://thesaurus.iadb.org/publicthesauri/IdBDepartments"))
    }

  }

}
