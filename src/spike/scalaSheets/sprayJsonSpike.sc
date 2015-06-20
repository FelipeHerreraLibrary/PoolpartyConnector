
import org.iadb.poolpartyconnector.contentclassification._
import org.iadb.poolpartyconnector.utils.JsonUtils.{Concept, ConceptScheme, PoolPartyJsonProtocol}

import PoolPartyJsonProtocol._

import spray.json._



val conceptschemeSource =
  """{ "title": "IdBTopics",
     "uri": "http://thesaurus.iadb.org/publicthesauri/IdBTopics"
     }"""

val conceptschemeSourceAst = conceptschemeSource.parseJson

val scheme = conceptschemeSourceAst.convertTo[ConceptScheme]


val conceptSource = """{"project": "1DCDFC5D-3876-0001-EEE6-BC9C1B8016CF",
                       "uri": "http://thesaurus.iadb.org/publicthesauri/35942080495154845",
                       "score": 100.0,"prefLabel": "Health",
                       "id": "1DCDFC5D-3876-0001-EEE6-BC9C1B8016CF:http://thesaurus.iadb.org/publicthesauri/35942080495154845@en",
                       "language": "en",
                       "conceptSchemes": [{
                              "title": "IdBTopics",
                              "uri": "http://thesaurus.iadb.org/publicthesauri/IdBTopics"
                              }],
                       "frequencyInDocument": 1467}
                    """

conceptSource.parseJson.prettyPrint
val concept = conceptSource.parseJson.convertTo[Concept]
/*val source = """{ "some": "JSON source" }"""
val jsonAst = source.parseJson
val json = jsonAst.prettyPrint
val myJsonAst = List(1, 2, 3).toJson.prettyPrint
case class Person(name: String)
object MyJsonProtocol extends DefaultJsonProtocol {
  implicit val PersonFormat = jsonFormat1(Person)
}
import MyJsonProtocol._
Person("daniel").toJson.prettyPrint*/

