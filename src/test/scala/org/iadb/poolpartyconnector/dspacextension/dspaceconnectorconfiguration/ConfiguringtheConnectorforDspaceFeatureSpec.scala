package org.iadb.poolpartyconnector.dspacextension.dspaceconnectorconfiguration

import org.iadb.poolpartyconnector.connectorconfiguration.{CorpusScoringSettings, PoolpartySettings}
import org.scalatest._

/**
 * Created by Daniel Maatari Okouya on 7/8/15.
 *
 * Configuring the PoolParty Connector for Dspace
 *
 */

trait ConfigFixture extends BeforeAndAfterAll { this: Suite =>


  var expectedConfdcfieldSettingsFixtVal     : FieldSettings                    = _
  var expectedConfauthorfieldSettingsfixtVal : FieldSettings                    = _
  var expectedConfpoolpartySettingsFixedVal  : PoolpartySettings                = _
  var expectedconfigInfoFixtVal              : String                           = _
  var loadedConnectorSettingsFixtVal         : DspacePoolPartyConnectorSettings = _


  override def beforeAll() {

    expectedConfdcfieldSettingsFixtVal     = FieldSettings("dc.subject", true, true, List("english", "spanish"), false, "http://thesaurus.iadb.org/publicthesauri/IdBTopics", "1DCE4A2B-1F43-0001-B54C-1F2015001F4B", 10, 3)
    expectedConfauthorfieldSettingsfixtVal = FieldSettings("dc.contributor.author", false, false, List("english"), false, "http://thesaurus.iadb.org/publicthesauri/IdBAuthors", "1DCE4A2B-1F43-0001-B54C-1F2015001F4B", 6, 2)
    loadedConnectorSettingsFixtVal         = DspaceDspacePoolPartyConnectorSettingImpl ("file:///Users/maatary/Dev/IdeaProjects/PoolpartyConnector/src/test/resources/poolpartydspace.conf")

    expectedConfpoolpartySettingsFixedVal  = PoolpartySettings("http://127.0.0.1:8086","http://127.0.0.1:8086/PoolParty/api/thesaurus","http://127.0.0.1:8086/extractor/api/extract","1DCE4A2B-1F43-0001-B54C-1F2015001F4B",
                                                               "http://thesaurus.iadb.org/publicthesauri","1DCE5A8E-25DA-0001-AB36-11DD12701707","http://thesaurus.iadb.org/jelcodes", 100, 3,
                                                               CorpusScoringSettings("corpus:df58f8fc-08d0-4cc0-ba46-e9e1149dfcf5", "corpus:e515ab1d-fd34-4c2e-9a33-11ecf9a670d5", "", ""))

    expectedconfigInfoFixtVal = """PoolPartyConnectorSettings : {
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
                                  |      "fieldname" : "dc.subject"
                                  |
                                  |      "treeBrowser" : true
                                  |
                                  |      "multilanguage": true
                                  |
                                  |      "languages": ["english", "spanish"]
                                  |
                                  |      "closed" : false
                                  |
                                  |      "scheme": ${PoolPartyConnectorSettings.PoolPartySettings.coreThesaurusUri}"/IdBTopics"
                                  |
                                  |      "poolpartyProjectId": ${PoolPartyConnectorSettings.PoolPartySettings.coreProjectId}
                                  |
                                  |    },
                                  |
                                  |    {
                                  |      "fieldname" : "dc.contributor.author"
                                  |
                                  |      "treeBrowser" : false
                                  |
                                  |      "multilanguage": false
                                  |
                                  |      "languages": ["english"]
                                  |
                                  |      "closed" : false
                                  |
                                  |      "scheme": ${PoolPartyConnectorSettings.PoolPartySettings.coreThesaurusUri}"/IdBAuthors"
                                  |
                                  |      "poolpartyProjectId": ${PoolPartyConnectorSettings.PoolPartySettings.coreProjectId}
                                  |
                                  |    },
                                  |.......
                                  |
                                  |    "skoslangcodesmappings": [
                                  |
                                  |    {
                                  |      "language": "english"
                                  |      "skoscode": "en"
                                  |    },
                                  |
                                  |    {
                                  |      "language": "spanish"
                                  |      "skoscode": "es"
                                  |    },
                                  |
                                  |    {
                                  |      "language": "french"
                                  |      "skoscode": "fr"
                                  |    },
                                  |
                                  |    {
                                  |      "language": "portuguese"
                                  |      "skoscode": "pt"
                                  |    },
                                  |
                                  |  ]"""

    super.beforeAll() // To be stackable, must call super.beforeEach
  }

  override def afterAll() {
    super.afterAll() // To be stackable, must call super.afterEach

  }
}


class ConfiguringtheConnectorforDspaceFeatureSpec extends FeatureSpec with ConfigFixture with GivenWhenThen with Matchers {



  feature("Configuring the PoolpartyConnector for Dspace") {




    scenario("Dspace wants to obtain the Connector metadata fields settings that it manages and that are served by the Poolparty Server") {


      Given("the Setting Object set up with a Connector Configuration file that contain the following settings for the PoolParty Server and the metadata fields: ")

        info(expectedconfigInfoFixtVal)

        val loadedConnectorSettings = loadedConnectorSettingsFixtVal


      When("getting the metadata field settings List from the connector Settings object")

        val fieldSettinglist  = loadedConnectorSettings.fieldsSettingsList


      Then("it should contain the same metadata field settings as in the configuration files")

        fieldSettinglist should (contain(expectedConfdcfieldSettingsFixtVal) and contain (expectedConfauthorfieldSettingsfixtVal));


    }


    scenario("Dspace wants to obtain the Connector's Poolparty Server settings") {


      Given("the Setting Object set up with a Connector Configuration file that contain the following settings for the PoolParty Server: ")


        info(expectedconfigInfoFixtVal)

        val loadedConnectorSettings = loadedConnectorSettingsFixtVal

      When("getting the PoolParty Server Settings from the Connector Settings Object")

        val poolpartySetting  = loadedConnectorSettings.poolpartyServerSettings


      Then("it should contain the settings values as in the configuration files")

        poolpartySetting should equal (expectedConfpoolpartySettingsFixedVal)


    }

    scenario("Dspace wants to obtain the Connector's Skos Code Mapping") {


      Given("the Setting Object set up with a Connector Configuration file that contain the following settings for the PoolParty Server: ")


        info(expectedconfigInfoFixtVal)

        val loadedConnectorSettings = loadedConnectorSettingsFixtVal

      When("getting the Connector Skos language code mapping settings")

        val skoscodesmappings  = loadedConnectorSettings.skoslangCodesMappingsMap


      Then("it should contain the same settings values as in the configuration files for english")

        skoscodesmappings.get("english") should equal (Some(SkoslangCodeMapping("english", "en")))

      And("it should contain the same settings values as in the configuration files for spanish")

        skoscodesmappings.get("spanish") should equal (Some(SkoslangCodeMapping("spanish", "es")))

      And("it should contain the same settings values as in the configuration files for french")

        skoscodesmappings.get("french") should equal (Some(SkoslangCodeMapping("french", "fr")))

      And("it should contain the same settings values as in the configuration files for portuguese")

        skoscodesmappings.get("portuguese") should equal (Some(SkoslangCodeMapping("portuguese", "pt")))

    }


  }



}
