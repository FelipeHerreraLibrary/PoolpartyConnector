
import org.iadb.poolpartyconnector.conceptsrecommendation.JsonProtocolSpecification
import JsonProtocolSpecification._

import JsonProtocolSpecification.PoolPartyJsonProtocol._


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
val sparqlquery = """PREFIX skos:<http://www.w3.org/2004/02/skos/core#> SELECT ?eca {<http://thesaurus.iadb.org/publicthesauri/118176996326225017829154> skos:broaderTransitive ?via. ?via skos:topConceptOf <http://thesaurus.iadb.org/publicthesauri/IdBTopics>.?via <http://thesaurus.iadb.org/idbdoc/eca> ?eca } LIMIT 100""".stripMargin


val request =
  s"""
     {
      "query": "$sparqlquery",
      "content-type": "application/json"
     }
  """.stripMargin.parseJson.prettyPrint