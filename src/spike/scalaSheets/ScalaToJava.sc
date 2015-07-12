import org.iadb.poolpartyconnector.dspaceutils.dspaceconnectorconfiguration.FieldSettings
import org.iadb.poolpartyconnector.scalajavaconversion.ScalatoJava


import scala.collection.JavaConverters._


def  asJava[A,B](map: Map[A,B]): java.util.Map[A,B] = {

  map.asJava
}

def  asJava[A](list: List[A]) = {list.asJavaCollection}


var map: Map[String, FieldSettings] = null

ScalatoJava.asJava(map)