import java.net.URL

import com.typesafe.config.ConfigFactory
import org.iadb.poolpartyconnector.dspacextension.dspaceconnectorconfiguration.FieldSettings
import scala.collection.JavaConverters._
val conf = ConfigFactory.parseURL(new URL("file:///Users/maatary/Dev/IdeaProjects/PoolpartyConnector/src/spike/resources/poolpartydspace.conf"))
.resolve()
conf.hasPath("PoolPartyConnectorSettings.FieldSettings")
val settinglist = conf.getConfigList("PoolPartyConnectorSettings.FieldSettings").asScala.toList
settinglist foreach { e =>
    println("fieldname" + ": " + e.getString("fieldname"))
    println("treeBrowser" + ": " + e.getBoolean("treeBrowser"))
    println("multilanguage" + ": " + e.getBoolean("multilanguage"))
    val languages = e.getStringList("languages").asScala.toList
    print("languages: "); languages foreach (e => print(e + " ")); println();
    println("closed" + ": " + e.getBoolean("closed"))
    println("scheme" + ": " + e.getString("scheme"))
    println("poolpartyProjectId" + ": " + e.getString("poolpartyProjectId"))
    println();
}
val fieldlist = settinglist map { e =>
    FieldSettings(e.getString("fieldname"), e.getBoolean("treeBrowser"),
        e.getBoolean("multilanguage"), e.getStringList("languages").asScala.toList,
        e.getBoolean("closed"), e.getString("scheme"), e.getString("poolpartyProjectId")
    )}


Map(fieldlist map {s => (s.fieldName,s )} : _*).values.toList

