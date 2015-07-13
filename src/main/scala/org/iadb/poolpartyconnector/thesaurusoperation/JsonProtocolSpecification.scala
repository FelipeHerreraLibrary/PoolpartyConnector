package org.iadb.poolpartyconnector.thesaurusoperation

import spray.json.DefaultJsonProtocol

/**
 * Created by Daniel Maatari Okouya on 6/21/15.
 */
object JsonProtocolSpecification {


  case class Concept(prefLabel: String, uri: String)


  object JsonProtocol extends DefaultJsonProtocol {

    implicit val ConceptFormat  = jsonFormat2(Concept)

  }

}
