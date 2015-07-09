import java.net.URL

import com.typesafe.config.ConfigFactory
import org.iadb.poolpartyconnector.dspaceutils.dspaceconnectorconfiguration.FieldSettings
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
(settinglist map { e =>
    FieldSettings(e.getString("fieldname"), e.getBoolean("treeBrowser"),
        e.getBoolean("multilanguage"), e.getStringList("languages").asScala.toList,
        e.getBoolean("closed"), e.getString("scheme"), e.getString("poolpartyProjectId")
    )
}).contains(FieldSettings("dc.subject", true, true, List("english, spanish"), false, "http://thesaurus.iadb.org/publicthesauri/IdBTopics", "1DCDFC5D-3876-0001-EEE6-BC9C1B8016CF")
)
case class Person(name:String, list: List[String])
List(Person("me", List("you", "I"))).contains(Person("me", List("you", "I")))

val field1 = FieldSettings("dc.subject", true, true, List("english, spanish"), false, "http://thesaurus.iadb.org/publicthesauri/IdBTopics", "1DCDFC5D-3876-0001-EEE6-BC9C1B8016CF")

val field2 = FieldSettings("dc.subject2", true, true, List("english, spanish"), false, "http://thesaurus.iadb.org/publicthesauri/IdBTopics", "1DCDFC5D-3876-0001-EEE6-BC9C1B8016CF")

val field3 = FieldSettings("dc.subject", true, true, List("english, spanish"), false, "http://thesaurus.iadb.org/publicthesauri/IdBTopics", "1DCDFC5D-3876-0001-EEE6-BC9C1B8016CF")


val list = List(field1, field1, field2)
list.contains(field3)