package org.iadb.poolpartyconnector.dspacextension.dspacedataoperationadaptation

import org.iadb.poolpartyconnector.dspacextension.dspaceconnectorconfiguration.DspaceDspacePoolPartyConnectorSettingImpl
import org.iadb.poolpartyconnector.dspacextension.dspacedatamodeladaptation.DspaceMetadatum
import org.iadb.poolpartyconnector.dspacextension.dspacedataoperationadaptation.schememapping.{SchemeMetadatumMappingService, SchemeMetadatumMappingServiceImpl}
import org.scalatest.{ Matchers, GivenWhenThen, FunSpec}

/**
 * Created by Daniel Maatari Okouya on 7/13/15.
 *
 * Specification of the service that maps/convert between dspace metadata field and skos schemes
 */
class SchemeMetadatumConversionSpec extends FunSpec with GivenWhenThen with Matchers {



    describe("The SchemeMetadatumMappingService") {


      describe("Requesting the Dspace Metadatum corresponding to an existing SKOs scheme ") {


        it("should return a non empty list of Dspace Metadatum that are mapped to that Skos Scheme: \"http://thesaurus.iadb.org/publicthesauri/IdBAuthors\", as present in the Connector settings with which it is initialized") {


          Given("we initialize the SchemeMetadatumMappingService with the following Connector Setting: the following Connector Setting: ")

            //info("")

            val loadedConnectorSettings = DspaceDspacePoolPartyConnectorSettingImpl("file:///Users/maatary/Dev/IdeaProjects/PoolpartyConnector/src/test/resources/poolpartydspace.conf")
            val authordatum = DspaceMetadatum("dc.contributor.author")
            val editordatum = DspaceMetadatum("dc.contributor.editor")

            val schemeMapping: SchemeMetadatumMappingService = SchemeMetadatumMappingServiceImpl(loadedConnectorSettings)

          When("requesting the fields for the scheme: \"http://thesaurus.iadb.org/publicthesauri/IdBAuthors\"")

            val metadatums = schemeMapping.getMetadatumforScheme("http://thesaurus.iadb.org/publicthesauri/IdBAuthors")

          Then("it should return the fields: {\"dc.contributor.author\", \"dc.contributor.editor\"}")

            metadatums should equal (List(authordatum, editordatum))

        }


        it("should return a non empty list of Dspace Metadatum that are mapped to that Skos Scheme: \"http://thesaurus.iadb.org/publicthesauri/IdBInstitutions\", as present in the Connector settings with which it is initialized") {


          Given("we initialize the SchemeMetadatumMappingService with the following Connector Setting:  the following Connector Setting: ")

          //info("")

            val loadedConnectorSettings = DspaceDspacePoolPartyConnectorSettingImpl("file:///Users/maatary/Dev/IdeaProjects/PoolpartyConnector/src/test/resources/poolpartydspace.conf")
            val  institutiondatum = DspaceMetadatum("dc.contributor.institution")

            val schemeMapping: SchemeMetadatumMappingService = SchemeMetadatumMappingServiceImpl(loadedConnectorSettings)

          When("requesting the fields for the scheme: \"http://thesaurus.iadb.org/publicthesauri/IdBInstitutions\"")

            val metadatums = schemeMapping.getMetadatumforScheme("http://thesaurus.iadb.org/publicthesauri/IdBInstitutions")

          Then("it should return the fields: {\"dc.contributor.institution\"}")

            metadatums should equal (List(institutiondatum))

        }

      }

      describe("Requesting if a metadatum is part of the mapping") {


        it("should return true if a mapping for the metadatum is defined in the connector configuration settings") {

          Given("we initialize the SchemeMetadatumMappingService with a Connector Settings which contains a mapping for the metadatum \"dc.subject\": ")

            info("....")

            val loadedConnectorSettings = DspaceDspacePoolPartyConnectorSettingImpl("file:///Users/maatary/Dev/IdeaProjects/PoolpartyConnector/src/test/resources/poolpartydspace.conf")
            val schemeMapping: SchemeMetadatumMappingService = SchemeMetadatumMappingServiceImpl(loadedConnectorSettings)


          When("requesting if there exist a mapping for it")

            val ismapped = schemeMapping.mappsMetadatumWithName("dc.subject")


          Then("it should return true")

            ismapped should be (true)

        }

        it("should return false if there is no mapping for the metadatum in the connector configuration settings") {
          pending
        }

      }

    }

}
