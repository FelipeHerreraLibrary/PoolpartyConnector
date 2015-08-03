package org.iadb.poolpartyconnector.thesaurusoperation

import akka.actor.ActorSystem
import akka.util.Timeout
import org.iadb.poolpartyconnector.dspacextension.dspaceconnectorconfiguration.DspacePoolPartyConnectorSettings
import org.iadb.poolpartyconnector.dspacextension.springadaptation.ActorSystemSpringWrapperBean
import spray.json._
import org.iadb.poolpartyconnector.thesaurusoperation.JsonProtocolSpecification.{Uri, SuggestFreeConcept, LanguageLiteral, Concept}
import JsonProtocolSpecification.JsonProtocol._

import spray.client.pipelining._
import spray.http.{StatusCodes, HttpResponse, BasicHttpCredentials}
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

/**
 *
 * Created by Daniel Maatari Okouya on 6/19/15.
 *
 * A Service That Cache an External Thesarus
 *
 */
trait ThesaurusCacheService {

  def createSuggestedFreeConcept(suggestedPrefLabel: String, lang: String, scheme: String, b: Boolean): String


  /**
   *  Find a Concept Prefered Label in the supplied Language otherwise its the Default Language
   *
   * @param uri
   * @param lang
   * @return
   */

  def getConceptPrefLabelWithDefaultLangfallback(uri: String, lang: String): String


  /**
   *  Find a Concept Prefered Label in the supplied Language
   *
   * @param uri
   * @param lang
   * @return
   */
  def getPrefLabelforConcept(uri: String, lang:String = "en") : String


  /**
   * Check if a specific Scheme is part of the ThesarusCacheService
   *
   * @param scheme
   * @return
   */
  def isSchemeInCache(scheme: Option[String]): Boolean
}









/**
 * An Implementation of the CacheService for the PoolParty Thesaurus Server
 *
 */
case class ThesaurusCacheServicePoolPartyImpl(actorSystem: ActorSystem, connectorSettings: DspacePoolPartyConnectorSettings, name: String = "ThesaurusCacheServicePoolPartyImpl") extends  ThesaurusCacheService {

  implicit private val requestTimeout = Timeout(800 seconds)
  implicit private val system         = actorSystem

  private val thesaurusapiEndpoint    = connectorSettings.poolpartyServerSettings.thesaurusapiEndpoint
  private val coreProjectId           = connectorSettings.poolpartyServerSettings.coreProjectId
  private val coreThesaurusUri        = connectorSettings.poolpartyServerSettings.coreThesaurusUri
  private val jelProjectId            = connectorSettings.poolpartyServerSettings.jelProjectId
  private val jelThesaurusUri         = connectorSettings.poolpartyServerSettings.jelThesaurusUri
  //TODO support JelCode and Series
  var schemeList: List[String] =  List("http://thesaurus.iadb.org/publicthesauri/IdBTopics",
                                       "http://thesaurus.iadb.org/publicthesauri/IdBDepartments",
                                       "http://thesaurus.iadb.org/publicthesauri/IdBInstitutions",
                                       "http://thesaurus.iadb.org/publicthesauri/IdBCountries",
                                       "http://thesaurus.iadb.org/publicthesauri/IdBAuthors")


  def this(systemBean: ActorSystemSpringWrapperBean, connectorSettings: DspacePoolPartyConnectorSettings) = {

    this(systemBean.getActorSystem, connectorSettings)
  }

  /**
   *
   * Check if a scheme is being Cached
   *
   * @param scheme The scheme we want to know if it is in the cache
   * @return
   */
  override def isSchemeInCache(scheme: Option[String]): Boolean = {

    scheme match {

      case None => false
      case Some(e) => schemeList.contains(e)
    }

  }

  /**
   *  Find a Concept Prefered Label in the supplied Language otherwise its the Default Language
   *
   * @param uri
   * @param lang
   * @return
   */

  def getConceptPrefLabelWithDefaultLangfallback(uri: String, lang: String) = {

    getPrefLabelforConcept(uri, lang) match {

      case e if e.isEmpty => getPrefLabelforConcept(uri, "en")
      case e => e

    }

  }

  /**
   *
   * @param uri
   * @param lang
   * @return
   */
  override def getPrefLabelforConcept(uri: String, lang:String = "en"): String = {

    import system.dispatcher

    val alang = getlangOrdefault(lang)

    val pipeline      = addCredentials(BasicHttpCredentials("superadmin", "poolparty")) ~> sendReceive


    val request       = uri match {
      case e if e.startsWith(coreThesaurusUri) => Get(s"$thesaurusapiEndpoint/$coreProjectId/concept?concept=$uri&language=$alang")
      case e if e.startsWith(jelThesaurusUri)  => Get(s"$thesaurusapiEndpoint/$jelProjectId/concept?concept=$uri&language=$alang")
    }

    val res           = pipeline(request)

    getConceptFromFutureHttpResponse(res) match {
      case Some(e) => e.prefLabel
      case _ => ""
    }

  }



  override def createSuggestedFreeConcept(suggestedPrefLabel: String, lang: String, scheme: String, b: Boolean): String = {


    import system.dispatcher

    import spray.httpx.marshalling._
    import spray.httpx.SprayJsonSupport._

    val pipeline      = addCredentials(BasicHttpCredentials("superadmin", "poolparty")) ~> sendReceive


    val label              = LanguageLiteral(suggestedPrefLabel, lang)
    val suggestion         = SuggestFreeConcept(List(label), b, Some(List(Uri(scheme))), None, None,None, None)


    val request            = Post(s"$thesaurusapiEndpoint/$coreProjectId/suggestFreeConcept", marshal(suggestion))

    val res                = pipeline(request)

    getSuggestedFromFutureHttpResponse(res) match {

      case None => ""
      case Some(e) => e

    }
  }


  private def getSuggestedFromFutureHttpResponse(res: Future[HttpResponse]): Option[String] = {

    try {

      val response = Await.result(res, Duration.Inf)

      response.status match {

        case StatusCodes.OK => {

          if (response.entity.isEmpty)

            None

          else {

            println("\n\n## " + response.entity.data.asString + "\n\n##")

            Some(response.entity.data.asString);
          }
        }

        case _ => None

    }
  }catch {

    case e:Throwable => {println(s"\n\n***\n\nError: \n$e\n\n***\n\n"); None}
    //TODO Add the possible throwable here: those of await result and log the error
  }

  }



  /**
   *  Await for the future Http response to arrive
   *  Get the response or the error if any
   *  Return the response as a ConceptResult Object
   *  The Object is empty if there was an error
   *
   * @param res
   * @return
   */
  private def getConceptFromFutureHttpResponse(res: Future[HttpResponse]): Option[Concept] = {

    try {

      val response = Await.result(res, Duration.Inf)

      response.status match {

        case StatusCodes.OK => {

                                if (response.entity.isEmpty)

                                  None

                                else {

                                  println("\n\n## " + response.entity.data.asString + "\n\n##")

                                  Some(response.entity.data.asString.parseJson.convertTo[Concept]);
                                }
        }

        case _ => None
      }

    }catch {

      case e:Throwable => {println(s"\n\n***\n\nError: \n$e\n\n***\n\n"); None}
      //TODO Add the possible throwable here: those of await result and log the error
    }

  }

  private def getlangOrdefault(lang: String): String = {

    lang match {
      case "en" => "en"
      case "es" => "es"
      case "fr" => "fr"
      case "pt" => "pt"
      case _ => "en"
    }
  }


}
