package org.iadb.poolpartyconnector.thesauruscaching

import org.scalatest.{FunSpec, Matchers, GivenWhenThen, FeatureSpec}

/**
 * Created by Daniel Maatari Okouya on 6/19/15.
 */
class SchemeCacheExistenceCheckUnitTest extends FunSpec with GivenWhenThen with Matchers {


  info("As the System I want to be able to check if a particular scheme exist in the cache")


  describe("The Thesaurus Cache Service: ThesaurusCacheService") {


    val cache: ThesaurusCacheService = new ThesaurusCacheServicePoolPartyImpl

    it("should return false when ask if None Scheme is being Cached") {

      Given("the scheme None")

        val scheme = None

      When("checked against the ThesaurusCacheService")

        val res = cache.isSchemeInCache(scheme)

      Then("it should respond false")

        res should be (false)
        info (s"the result was: $res")


    }


    it("should return false when ask if a \"\" Scheme is being Cached") {

      Given("the scheme None")

        val scheme = Some("\"\"")

      When("checked against the ThesaurusCacheService")

        val res = cache.isSchemeInCache(scheme)

      Then("it should responds false")

        res should be (false)
        info (s"the result was: $res")
    }

    it("should return false when ask if a any non existing Scheme e.g. \"http://thesaurus.iadb.org/publicthesauri/Countries\" is being Cached") {

      Given("the scheme \"http://thesaurus.iadb.org/publicthesauri/Countries\"")

        val scheme = Some("http://thesaurus.iadb.org/publicthesauri/Countries")

      When("checked against the ThesaurusCacheService")

        val res = cache.isSchemeInCache(scheme)

      Then("it should responds false")

        res should be (false)
        info (s"the result was: $res")

    }


    it("should return true when ask if an existing Scheme e.g. \"http://thesaurus.iadb.org/publicthesauri/IdBCountries\" is being Cached") {

      Given("the scheme \"http://thesaurus.iadb.org/publicthesauri/IdBCountries\"")

      val scheme = Some("http://thesaurus.iadb.org/publicthesauri/IdBCountries")

        When("checked against the ThesaurusCacheService")

      val res = cache.isSchemeInCache(scheme)

        Then("it should responds true")

        res should be (true)
        info (s"the result was: $res")
    }


    it("should return true when ask if an existing Scheme e.g. \"http://thesaurus.iadb.org/publicthesauri/IdBInstitutions\" is being Cached") {

      Given("the scheme \"http://thesaurus.iadb.org/publicthesauri/IdBInstitutions\"")

        val scheme = Some("http://thesaurus.iadb.org/publicthesauri/IdBInstitutions")

      When("checked against the ThesaurusCacheService")

        val res = cache.isSchemeInCache(scheme)

      Then("it should responds true")

        res should be (true)
        info (s"the result was: $res")
    }


    it("should return true when ask if an existing Scheme e.g. \"http://thesaurus.iadb.org/publicthesauri/IdBDepartments\" is being Cached") {

      Given("the scheme \"http://thesaurus.iadb.org/publicthesauri/IdBDepartments\"")

        val scheme = Some("http://thesaurus.iadb.org/publicthesauri/IdBDepartments")

      When("checked against the ThesaurusCacheService")

        val res = cache.isSchemeInCache(scheme)

      Then("it should responds true")

        res should be (true)
        info (s"the result was: $res")
    }


    it("should return true when ask if an existing Scheme e.g. \"http://thesaurus.iadb.org/publicthesauri/IdBTopics\" is being Cached") {

      Given("the scheme \"http://thesaurus.iadb.org/publicthesauri/IdBTopics\"")

        val scheme = Some("http://thesaurus.iadb.org/publicthesauri/IdBTopics")

      When("checked against the ThesaurusCacheService")

        val res = cache.isSchemeInCache(scheme)

      Then("it should responds true")

        res should be (true)
        info (s"the result was: $res")
    }

  }


}
