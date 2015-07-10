package org.iadb.poolpartyconnector.dspaceutils.dspaceconnectorconfiguration

import java.net.URL
import com.typesafe.config.ConfigFactory
import org.iadb.poolpartyconnector.connectorconfiguration.PoolpartySettings
import scala.collection.JavaConverters._



/**
 * Created by Daniel Maatari Okouya on 7/8/15.
 *
 */
trait DspacePoolPartyConnectorSettings {

  def poolpartyServerSettings : PoolpartySettings
  def fieldsSettingsList      : List[FieldSettings]
  def fieldsSettingsMap       : Map[String, FieldSettings]

}


case class DspaceDspacePoolPartyConnectorSettingImpl (configUri: String) extends DspacePoolPartyConnectorSettings {


  private val config                 = ConfigFactory.parseURL(new URL(configUri)).resolve()
  private val configFieldSettinglist = config.getConfigList("PoolPartyConnectorSettings.FieldSettings").asScala.toList
  private val configPoolPartySetting = config.getConfig("PoolPartyConnectorSettings.PoolPartySettings")


  val poolpartyServerSettings        = PoolpartySettings( configPoolPartySetting.getString("apirootEndpoint"),
                                                          configPoolPartySetting.getString("thesaurusapiEndpoint"),
                                                          configPoolPartySetting.getString("extratorapiEndpoint"),
                                                          configPoolPartySetting.getString("coreProjectId"),
                                                          configPoolPartySetting.getString("coreThesaurusUri"),
                                                          configPoolPartySetting.getString("jelProjectId"),
                                                          configPoolPartySetting.getString("jelThesaurusUri"))

  val fieldsSettingsList             =  configFieldSettinglist map { e =>
                                                  FieldSettings(e.getString("fieldname"), e.getBoolean("treeBrowser"),
                                                  e.getBoolean("multilanguage"), e.getStringList("languages").asScala.toList,
                                                  e.getBoolean("closed"), e.getString("scheme"), e.getString("poolpartyProjectId"))
  }


  val fieldsSettingsMap = Map(fieldsSettingsList map {e => (e.fieldName, e)}: _*)


}