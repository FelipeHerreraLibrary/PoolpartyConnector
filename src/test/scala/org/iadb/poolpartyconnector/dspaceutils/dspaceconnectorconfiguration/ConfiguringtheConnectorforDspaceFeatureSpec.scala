package org.iadb.poolpartyconnector.dspaceutils.dspaceconnectorconfiguration

import org.scalatest.{Matchers, GivenWhenThen, FeatureSpec}

/**
 * Created by Daniel Maatari Okouya on 7/8/15.
 *
 * Configuring the PoolParty Connector for Dspace
 *
 */
class ConfiguringtheConnectorforDspaceFeatureSpec extends FeatureSpec with GivenWhenThen with Matchers {



  feature("Configuring the PoolpartyConnector for Dspace") {



    scenario("Dspace wants to obtain the settings for each metadata field that it manages and that are served by the Poolparty Server") {


      Given("the Setting Object set up with a Connector Configuration file that contain the following settings for the PoolParty Server and the metadata fields: ")


        info("""PoolPartyConnectorSettings : {
               |
               |
               |  "PoolPartySettings" : {
               |
               |    "apirootEndpoint": "http://127.0.0.1:8086"
               |
               |    "thesaurusapiEndpoint": ${PoolPartyConnectorSettings.PoolPartySettings.apirootEndpoint}"/PoolParty/api/thesaurus"
               |
               |    "extratorapiEndpoint" : ${PoolPartyConnectorSettings.PoolPartySettings.apirootEndpoint}"/extractor/api/extract"
               |
               |    "coreProjectId" : "1DCDFC5D-3876-0001-EEE6-BC9C1B8016CF"
               |
               |    "coreThesaurusUri": "http://thesaurus.iadb.org/publicthesauri"
               |
               |    "jelProjectId" : " "
               |
               |    "jelThesaurusUri": ""
               |  }
               |
               |  "FieldSettings" : [
               |
               |    {
               |      "fieldname" : "dc.subject",
               |
               |      "treeBrowser" : true,
               |
               |      "multilanguage": true,
               |
               |      "languages": ["english", "spanish"],
               |
               |      "closed" : false,
               |
               |      "scheme": ${PoolPartyConnectorSettings.PoolPartySettings.coreThesaurusUri}"/IdBTopics",
               |
               |      "poolpartyProjectId": ${PoolPartyConnectorSettings.PoolPartySettings.coreProjectId},
               |
               |    },
               |
               |    {
               |      "fieldname" : "dc.contributor.author",
               |
               |      "treeBrowser" : false,
               |
               |      "multilanguage": false,
               |
               |      "languages": ["english"],
               |
               |      "closed" : false,
               |
               |      "scheme": ${PoolPartyConnectorSettings.PoolPartySettings.coreThesaurusUri}"/IdBAuthors",
               |
               |      "poolpartyProjectId": ${PoolPartyConnectorSettings.PoolPartySettings.coreProjectId},
               |
               |    }
               |  ]
               |}""")

        val field1 = FieldSettings("dc.subject", true, true, List("english", "spanish"), false, "http://thesaurus.iadb.org/publicthesauri/IdBTopics", "1DCDFC5D-3876-0001-EEE6-BC9C1B8016CF")
        val field2 = FieldSettings("dc.contributor.author", false, false, List("english"), false, "http://thesaurus.iadb.org/publicthesauri/IdBAuthors", "1DCDFC5D-3876-0001-EEE6-BC9C1B8016CF")


      When("getting the metadata field settings List from the connector Settings object")

        val connectorsettings = DspaceDspacePoolPartyConnectorSettingImpl ("file:///Users/maatary/Dev/IdeaProjects/PoolpartyConnector/src/test/resources/poolpartydspace.conf")
        val fieldSettinglist  = connectorsettings.fieldsSettings



      Then("it should contain the same metadata field settings as in the configuration files")

        fieldSettinglist should (contain(field1) and contain (field2));




    }


  }



}
