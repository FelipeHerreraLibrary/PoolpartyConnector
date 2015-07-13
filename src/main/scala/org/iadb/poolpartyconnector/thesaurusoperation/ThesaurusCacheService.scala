package org.iadb.poolpartyconnector.thesaurusoperation

import akka.actor.ActorSystem
import akka.util.Timeout
import org.iadb.poolpartyconnector.dspaceutils.ActorSystemSpringWrapperBean
import spray.json._
import JsonProtocolSpecification.Concept
import JsonProtocolSpecification.JsonProtocol._

import spray.client.pipelining._
import spray.http.{HttpResponse, BasicHttpCredentials}
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
case class ThesaurusCacheServicePoolPartyImpl(actorSystem: ActorSystem, name: String = "ThesaurusCacheServicePoolPartyImpl") extends  ThesaurusCacheService {

  implicit private val requestTimeout = Timeout(800 seconds)
  implicit private val system         = actorSystem

  //TODO support JelCode and Series
  var schemeList: List[String] =  List("http://thesaurus.iadb.org/publicthesauri/IdBTopics",
                                       "http://thesaurus.iadb.org/publicthesauri/IdBDepartments",
                                       "http://thesaurus.iadb.org/publicthesauri/IdBInstitutions",
                                       "http://thesaurus.iadb.org/publicthesauri/IdBCountries",
                                       "http://thesaurus.iadb.org/publicthesauri/IdBAuthors")


  def this(systembean: ActorSystemSpringWrapperBean) = {
    this(systembean.getActorSystem)
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

    val request       = Get(s"http://127.0.0.1:8086/PoolParty/api/thesaurus/1DCDFC5D-3876-0001-EEE6-BC9C1B8016CF/concept?concept=$uri&language=$alang")

    val res           = pipeline(request)

    getConceptFromFutureHttpResponse(res) match {
      case Some(e) => e.prefLabel
      case _ => ""
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

      val reponse = Await.result(res, Duration.Inf)

      println("\n\n## " + reponse.entity.data.asString + "\n\n##")

      Some(reponse.entity.data.asString.parseJson.convertTo[Concept])

    }catch {

      case e:Throwable => {

        println(s"\n\n***\n\nError: \n$e\n\n***\n\n")

        None
      }

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
