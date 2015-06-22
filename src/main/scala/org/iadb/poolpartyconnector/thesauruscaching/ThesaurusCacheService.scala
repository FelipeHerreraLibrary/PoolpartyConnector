package org.iadb.poolpartyconnector.thesauruscaching

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

  def getPrefLabelforConcept(uri: String, lang:String = "en") : String


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
      case _ => false


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


    val pipeline      = addCredentials(BasicHttpCredentials("superadmin", "poolparty")) ~> sendReceive

    val request       = Get(s"http://127.0.0.1:8086/PoolParty/api/thesaurus/1DCDFC5D-3876-0001-EEE6-BC9C1B8016CF/concept?concept=$uri&language=$lang")

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

      println(reponse.entity.data.asString)

      Some(reponse.entity.data.asString.parseJson.convertTo[Concept])

    }catch {

      case e:Throwable => None
      //TODO Add the possible throwable here: those of await result and log the error
    }

  }
}
