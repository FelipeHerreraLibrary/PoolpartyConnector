package org.iadb.poolpartyconnector.poolpartyjsonprotocol

import spray.json.DefaultJsonProtocol

/**
 * Created by Daniel Maatari Okouya on 6/7/15.
 *
 * The Spray Json Protocol for PoolParty
 */
object PoolPartyJsonProtocol {



  case class ConceptScheme(title:String,
                           uri:String)

  case class Concept(project:String,
                     uri:String,
                     score: Double,
                     prefLabel: String,
                     id:String,
                     language:String,
                     conceptSchemes: List[ConceptScheme],
                     frequencyInDocument:Int)

  case class FreeTerm(textValue:String,
                      score:Int,
                      frequencyInDocument:Int)

  case class Document(concepts: Option[List[Concept]],
                      freeTerms: Option[List[FreeTerm]])

  case class Metadata()

  case class ConceptResults(metadata: Option[Metadata], document: Option[Document])

  object PoolPartyJsonProtocol extends DefaultJsonProtocol {
    implicit val conceptSchemeFormat  = jsonFormat2(ConceptScheme)
    implicit val conceptFormat        = jsonFormat8(Concept)
    implicit val freeTermformat       = jsonFormat3(FreeTerm)
    implicit val documentFormat       = jsonFormat2(Document)
    implicit val metadataFormat       = jsonFormat0(Metadata)
    implicit val conceptResultsFormat = jsonFormat2(ConceptResults)
  }

}
