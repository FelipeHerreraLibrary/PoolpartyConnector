import org.iadb.poolpartyconnector.thesaurusoperation.JsonProtocolSpecification
import JsonProtocolSpecification._

import JsonProtocol._
import spray.json._
import spray.httpx.unmarshalling._
import spray.httpx.marshalling._
import spray.http._
import HttpCharsets._
import MediaTypes._

import spray.httpx.SprayJsonSupport._


val label = LanguageLiteral("Authority", "en")

val suggested = SuggestFreeConcept(List(label), true, None, None, None,None, None)

marshal(suggested).toString