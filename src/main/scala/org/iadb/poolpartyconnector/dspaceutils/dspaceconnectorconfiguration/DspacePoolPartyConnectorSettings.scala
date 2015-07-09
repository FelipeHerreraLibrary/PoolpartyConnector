package org.iadb.poolpartyconnector.dspaceutils.dspaceconnectorconfiguration

import java.net.URL
import com.typesafe.config.ConfigFactory
import scala.collection.JavaConverters._



/**
 * Created by Daniel Maatari Okouya on 7/8/15.
 *
 */
trait DspacePoolPartyConnectorSettings {

  def fieldsSettings : List[FieldSettings]

}


case class DspaceDspacePoolPartyConnectorSettingImpl (configUri: String) extends DspacePoolPartyConnectorSettings {


  private val conf            = ConfigFactory.parseURL(new URL(configUri)).resolve()
  private val confSettinglist = conf.getConfigList("PoolPartyConnectorSettings.FieldSettings").asScala.toList


  val fieldsSettings          =  confSettinglist map { e =>
                                                  FieldSettings(e.getString("fieldname"), e.getBoolean("treeBrowser"),
                                                  e.getBoolean("multilanguage"), e.getStringList("languages").asScala.toList,
                                                  e.getBoolean("closed"), e.getString("scheme"), e.getString("poolpartyProjectId")
    )
  }
}