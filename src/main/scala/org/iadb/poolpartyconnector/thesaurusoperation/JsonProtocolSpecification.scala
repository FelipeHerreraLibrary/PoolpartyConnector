package org.iadb.poolpartyconnector.thesaurusoperation

import spray.httpx.SprayJsonSupport
import spray.json.DefaultJsonProtocol

/**
 * Created by Daniel Maatari Okouya on 6/21/15.
 */
object JsonProtocolSpecification {



  case class Concept(prefLabel: String, uri: String)

  case class Uri(uri:String)

  case class LanguageLiteral(label: String, language: String)

  case class GenericConcept(resource: Uri, property: Uri, values: List[LanguageLiteral])

  case class SuggestFreeConcept(prefLabels: List[LanguageLiteral],
                                checkForDuplicates: Boolean,
                                broaderConcept: Option[List[Uri]],
                                relatedConcept: Option[List[Uri]],
                                definition: Option[List[LanguageLiteral]],
                                Note: Option[String],
                                score:Option[Double])


  //case class SuggestedFreeConceptResult(uri: String)


  object JsonProtocol extends DefaultJsonProtocol {

    implicit val ConceptFormat            = jsonFormat2(Concept)
    implicit val LanguageLiteralFormat    = jsonFormat2(LanguageLiteral)
    implicit val UriFormat                = jsonFormat1(Uri)
    implicit val SuggestFreeConceptFormat = jsonFormat7(SuggestFreeConcept)
    implicit val GenericConceptFormat     = jsonFormat3(GenericConcept)


  }

}
