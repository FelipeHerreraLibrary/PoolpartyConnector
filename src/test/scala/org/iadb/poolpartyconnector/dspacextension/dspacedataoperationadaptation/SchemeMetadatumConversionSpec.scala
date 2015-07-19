package org.iadb.poolpartyconnector.dspacextension.dspacedataoperationadaptation

import org.iadb.poolpartyconnector.dspacextension.dspaceconnectorconfiguration.DspaceDspacePoolPartyConnectorSettingImpl
import org.iadb.poolpartyconnector.dspacextension.dspacedatamodeladaptation.DspaceMetadatum
import org.iadb.poolpartyconnector.dspacextension.dspacedataoperationadaptation.schememapping.{SchemeMetadatumMappingServiceImpl, SchemeMetadatumMappingService}
import org.scalatest.{ Matchers, GivenWhenThen, FunSpec}

/**
 * Created by Daniel Maatari Okouya on 7/13/15.
 *
 * Specification of the service that maps/convert between dspace metadata field and skos schemes
 */
class SchemeMetadatumConversionSpec extends FunSpec with GivenWhenThen with Matchers {



    describe("The SKosSchemeMetaDataFieldMappingService") {


      describe("Requestiong the Dspace Metadatum corresponding to an existing SKOs scheme ") {


        it("should return a non empty list of Dspace Metadatum that are mapped to that Skos Scheme: \"http://thesaurus.iadb.org/publicthesauri/IdBAuthors\", as present in the Connector settings with which it is initialized") {


          Given("the following Connector Setting: ")

            //info("")

            val loadedConnectorSettings = DspaceDspacePoolPartyConnectorSettingImpl("file:///Users/maatary/Dev/IdeaProjects/PoolpartyConnector/src/test/resources/poolpartydspace.conf")
            val authordatum = DspaceMetadatum("dc.contributor.author")
            val editordatum = DspaceMetadatum("dc.contributor.editor")

            val schemeMapping: SchemeMetadatumMappingService = SchemeMetadatumMappingServiceImpl(loadedConnectorSettings)

          When("requesting the fields for the scheme: \"http://thesaurus.iadb.org/publicthesauri/IdBAuthors\"")

            val metadatums = schemeMapping.getFieldsforScheme("http://thesaurus.iadb.org/publicthesauri/IdBAuthors")

          Then("it should return the fields: {\"dc.contributor.author\", \"dc.contributor.editor\"}")

            metadatums should equal (List(authordatum, editordatum))

        }


        it("should return a non empty list of Dspace Metadatum that are mapped to that Skos Scheme: \"http://thesaurus.iadb.org/publicthesauri/IdBInstitutions\", as present in the Connector settings with which it is initialized") {


          Given("the following Connector Setting: ")

          //info("")

          val loadedConnectorSettings = DspaceDspacePoolPartyConnectorSettingImpl("file:///Users/maatary/Dev/IdeaProjects/PoolpartyConnector/src/test/resources/poolpartydspace.conf")
          val  institutiondatum = DspaceMetadatum("dc.contributor.institution")

          val schemeMapping: SchemeMetadatumMappingService = SchemeMetadatumMappingServiceImpl(loadedConnectorSettings)

          When("requesting the fields for the scheme: \"http://thesaurus.iadb.org/publicthesauri/IdBInstitutions\"")

          val metadatums = schemeMapping.getFieldsforScheme("http://thesaurus.iadb.org/publicthesauri/IdBInstitutions")

          Then("it should return the fields: {\"dc.contributor.institution\"}")

          metadatums should equal (List(institutiondatum))

        }

      }

    }

}
