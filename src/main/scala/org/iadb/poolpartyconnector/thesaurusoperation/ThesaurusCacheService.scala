package org.iadb.poolpartyconnector.thesaurusoperation


import javax.swing.plaf.BorderUIResource.EmptyBorderUIResource

import spray.caching._
import akka.actor.ActorSystem
import akka.util.Timeout
import org.iadb.poolpartyconnector.dspacextension.dspaceconnectorconfiguration.DspacePoolPartyConnectorSettings
import org.iadb.poolpartyconnector.dspacextension.springadaptation.{ActorSystemSpringWrapperBean, ExpiringLruCacheSpringWrapperBean}
import spray.json._
import org.iadb.poolpartyconnector.thesaurusoperation.JsonProtocolSpecification.{Concept, GenericConcept, LanguageLiteral, SuggestFreeConcept, Uri}
import JsonProtocolSpecification.JsonProtocol._
import spray.client.pipelining._
import spray.http._

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

  def getauthorRepecId(authorUri: String): String


  def createSuggestedFreeConceptsConcurrent(suggestedPrefLabels: List[LanguageLiteral], scheme: String, checkDuplicates: Boolean): List[String]


  def createSuggestedFreeConcepts(suggestedPrefLabel: List[LanguageLiteral], scheme: String, checkDuplicates: Boolean): List[String]


  def getConceptPrefLabelWithDefaultLangfallback(uri: String, lang: String): String

  def getConceptPrefLabelWithDefaultLangfallbackFuture(uri: String, lang: String): Future[String]

  def getPrefLabelforConcept(uri: String, lang:String = "en") : String


  def getPrefLabelforConceptFuture(uri: String, lang:String = "en"): Future[String]


  def getPrefLabelsforConcepts(uris: List[String], lang:String = "en"): List[String]


  def getIdbDocWebTopic(conceptUri: String): List[String]

  def getAllLangPrefLabels(conceptUri: String): List[LanguageLiteral]

  def getIndexableLabels(conceptUri: String): List[LanguageLiteral]

  def getBroaderLabels(conceptUri: String): List[LanguageLiteral]

  def getRelatedLabels(conceptUri: String): List[LanguageLiteral]

  def getAltLabels(conceptUri: String): List[LanguageLiteral]

}




/**
 * An Implementation of the CacheService for the PoolParty Thesaurus Server
 *
 */
case class ThesaurusCacheServicePoolPartyImpl(actorSystem: ActorSystem,
                                              connectorSettings: DspacePoolPartyConnectorSettings,
                                              thesaurusSparqlConsumer: ThesaurusSparqlConsumer,
                                              cache: Cache[String]) extends  ThesaurusCacheService {

  implicit private val requestTimeout = Timeout(60 seconds)
  implicit private val system         = actorSystem

  private val thesaurusapiEndpoint    = connectorSettings.poolpartyServerSettings.thesaurusapiEndpoint
  private val coreProjectId           = connectorSettings.poolpartyServerSettings.coreProjectId
  private val coreThesaurusUri        = connectorSettings.poolpartyServerSettings.coreThesaurusUri
  private val jelProjectId            = connectorSettings.poolpartyServerSettings.jelProjectId
  private val jelThesaurusUri         = connectorSettings.poolpartyServerSettings.jelThesaurusUri
  private val apirootEndpoint         = connectorSettings.poolpartyServerSettings.apirootEndpoint
  private val fieldsettingsMap        = connectorSettings.fieldsSettingsMap

  //TODO: Move it to the configuration file
  private val sparqlEndpoint          = connectorSettings.poolpartyServerSettings.sparqlEndpoint





  def this(systemBean: ActorSystemSpringWrapperBean, connectorSettings: DspacePoolPartyConnectorSettings, thesaurusSparqlConsumer: ThesaurusSparqlConsumer, cacheBean: ExpiringLruCacheSpringWrapperBean) = {

    this(systemBean.system, connectorSettings, thesaurusSparqlConsumer, cacheBean.expiringLruCache)
  }

  /*def this(systemBean: ActorSystemSpringWrapperBean, connectorSettings: DspacePoolPartyConnectorSettings) = {

    this(systemBean.getActorSystem, connectorSettings, null)
  }*/


  /*private def lruCache[T](maxCapacity: Int = 2000, initialCapacity: Int = 2000, timeToLive: Duration = Duration.Inf, timeToIdle: Duration = Duration.Inf) = {

    new ExpiringLruCache[T](maxCapacity, initialCapacity, timeToLive, timeToIdle)

  }*/




  /**
   *  Find a Concept Prefered Label in the supplied Language otherwise its the Default Language
   *
   * @param uri
   * @param lang
   * @return
   */

  def getConceptPrefLabelWithDefaultLangfallback(uri: String, lang: String): String = {

    Await.result(getConceptPrefLabelWithDefaultLangfallbackFuture(uri, lang), Duration.Inf)

  }


  /**
    *  Find a Concept Prefered Label in the supplied Language otherwise its the Default Language
    *  Returns a Future
    *
    * @param uri
    * @param lang
    * @return
    */

  def getConceptPrefLabelWithDefaultLangfallbackFuture(uri: String, lang: String):Future[String] = {
    //TODO Optimize make a double call each time
    import system.dispatcher

    lang match {
      case e if e == "en" => getPrefLabelforConceptFuture(uri, lang)
      case _ => {
        val expected = getPrefLabelforConceptFuture(uri, lang)
        val fallback = getPrefLabelforConceptFuture(uri, "en")
        expected withFilter(!_.isEmpty) recoverWith { case _ => fallback }
      }
    }

  }


  /**
    * Get the Preferred Labels of a list of concepts with fallBacks
    *
    * @param uris
    * @param lang
    * @return
    */
  override def getPrefLabelsforConcepts(uris: List[String], lang:String = "en"): List[String] = {

    import system.dispatcher

    val alang = getlangOrdefault(lang)

    //val pipeline      = addCredentials(BasicHttpCredentials("superadmin", "poolparty")) ~> sendReceive

    val futurePrefLabels = uris map { uri =>

      getConceptPrefLabelWithDefaultLangfallbackFuture(uri: String, alang: String)

    }
    Await.result(Future.sequence(futurePrefLabels), Duration.Inf)
  }

  /**
   * Get the preferred label of a concept
    *
    * @param uri
   * @param lang
   * @return
   */
  override def getPrefLabelforConcept(uri: String, lang:String = "en"): String = {

    import system.dispatcher

    //val initTime      = System.currentTimeMillis()
    //def time()        = System.currentTimeMillis()

    val myres = Await.result(getPrefLabelforConceptFuture(uri, lang), Duration.Inf)

    //println( "\n\n\nThe fetching time: " + (time()-initTime) + "\n\n\n")
    myres

  }


  /**
    * Returns a future[String] that will eventually contain the label
    *
    * TODO Not saving empty value
    * TODO If we get from the external call an empty value because the system was unavailble then we should retreive the local value else we fail
    *
    * @param uri
    * @param lang
    * @return
    */
  override def getPrefLabelforConceptFuture(uri: String, lang:String = "en"): Future[String] = {

    import system.dispatcher

    cache (uri + lang) {

      val alang         = getlangOrdefault(lang)
      val pipeline      = addCredentials(BasicHttpCredentials("superadmin", "poolparty")) ~> sendReceive

      val request       = uri match {
        case e if e.startsWith(coreThesaurusUri) => Get(s"$thesaurusapiEndpoint/$coreProjectId/getPropertyValues?resource=$uri&property=http://www.w3.org/2004/02/skos/core%23prefLabel")
        case e if e.startsWith(jelThesaurusUri)  => Get(s"$thesaurusapiEndpoint/$jelProjectId/getPropertyValues?resource=$uri&property=http://www.w3.org/2004/02/skos/core%23prefLabel")
      }

      val res           = pipeline(request)

      getFutureConceptFromFutureHttpResponse(res, request, lang)

    } recoverWith {case _ => Future.successful("") }
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
  private def getFutureConceptFromFutureHttpResponse(res: Future[HttpResponse], originalRequest: HttpRequest, lang:String = "en"): Future[String] = {

    import system.dispatcher

    res withFilter(_.status == StatusCodes.OK) map {

      case e if e.entity.isEmpty => {
        println("\n\n## " + s"The Request ${originalRequest.uri} returned an Empty Result"  + "\n\n##")
        ""
      }

      case e => {
        //println("\n\n## " + e.entity.data.asString + "\n\n##")
        e.entity.data.asString.parseJson.convertTo[GenericConcept].values.filter(_.language == lang) match {
          case e if e.isEmpty => {println("\n\n## " + s"The Request ${originalRequest.uri} returned an Empty Result"  + "\n\n##"); ""}
          case e => e.head.label
        }
      }
    } recoverWith {
      case t: Throwable =>  {
        println(s"\n\n## " + s"The Request ${originalRequest.uri} failed with Throwable: ${t}" + "\n\n##")
        Future.failed(t)
      }
    }

  }



  /*override def getPrefLabelsforConcepts(uris: List[String], lang:String = "en"): List[String] = {

    import system.dispatcher

    val alang = getlangOrdefault(lang)

    val pipeline      = addCredentials(BasicHttpCredentials("superadmin", "poolparty")) ~> sendReceive

    val concepts = uris map { uri =>

      val request = uri match {
        case e if e.startsWith(coreThesaurusUri) => Get(s"$thesaurusapiEndpoint/$coreProjectId/concept?concept=$uri&language=$alang")
        case e if e.startsWith(jelThesaurusUri)  => Get(s"$thesaurusapiEndpoint/$jelProjectId/concept?concept=$uri&language=$alang")
      }
      pipeline(request) // Need a recover with here: filter and recover
    }



    val fallbacks = concepts.zip(uris) map { e => e._1.collect {

      case response if ((response.status == StatusCodes.OK) && response.entity.isEmpty) => {
        val request = e._2 match {
          case e if e.startsWith(coreThesaurusUri) => Get(s"$thesaurusapiEndpoint/$coreProjectId/concept?concept=$e&language=en")
          case e if e.startsWith(jelThesaurusUri) => Get(s"$thesaurusapiEndpoint/$jelProjectId/concept?concept=$e&language=en")
        }
        pipeline(request)
      }
      case response => Future.successful{response}
    }
    }

    val euh = fallbacks map (e => e.flatMap(e => e))

    getConceptsFromFutureHttpResponses(Future.sequence(euh)) match {

      case None => List[String]()
      case Some(e) => e map {a => a match {case None => ""; case Some(concept) => concept.prefLabel}}

    }

  }*/




  /*private def getConceptsFromFutureHttpResponses(ress: Future[List[HttpResponse]]): Option[List[Option[Concept]]] = {

    try {

      val responses = Await.result(ress, Duration.Inf)

      val results = responses map { response =>

        response.status match {

          case StatusCodes.OK => {

            if (response.entity.isEmpty)

              None

            else {

              println("\n\n## " + response.entity.data.asString + "\n\n##")

              Some(response.entity.data.asString.parseJson.convertTo[Concept]);
            }
          }

          case _ => {println("##\n\nThere was an error. Response: " + response.toString + "\n\n##"); None}
        }

      }

      Some(results)
    }
    catch {
      case e:Throwable => {println(s"\n\n***\n\nError: \n$e\n\n***\n\n"); None}
    }


  }
*/

  /**
   * Non-concurent, slow methods: create one label after receiving the confirmation that the preceding has been created.
   *
   * @param suggestedPrefLabels
   * @param scheme
   * @param checkDuplicates
   * @return
   */
  override def createSuggestedFreeConcepts(suggestedPrefLabels: List[LanguageLiteral], scheme: String, checkDuplicates: Boolean): List[String] = {


    import system.dispatcher

    import spray.httpx.marshalling._
    import spray.httpx.SprayJsonSupport._

    val pipeline      = addCredentials(BasicHttpCredentials("superadmin", "poolparty")) ~> sendReceive

    val suggestedRes  = suggestedPrefLabels map { suggestedPrefLabel =>


      val suggestion  = if (scheme == fieldsettingsMap("dc.contributor.author").scheme)

        SuggestFreeConcept(
          List(suggestedPrefLabel, suggestedPrefLabel.copy(language = "es"),
          suggestedPrefLabel.copy(language = "fr"),
          suggestedPrefLabel.copy(language = "pt")),
          checkDuplicates, Some(List(Uri(scheme))), None, None, None, None)

      else

        SuggestFreeConcept(List(suggestedPrefLabel), checkDuplicates, Some(List(Uri(scheme))), None, None, None, None)

      val request       = Post(s"$thesaurusapiEndpoint/$coreProjectId/suggestFreeConcept", marshal(suggestion))
      val res           = pipeline(request)

      getSuggestedFromFutureHttpResponse(res) match {

        case None => ""
        case Some(e) => e

      }

    }

    pipeline(Post(s"$apirootEndpoint/PoolParty/api/projects/${coreProjectId}/snapshot"))

    suggestedRes

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
    }
    catch {

      case e:Throwable => {println(s"\n\n***\n\nError: \n$e\n\n***\n\n"); None}
      //TODO Add the possible throwable here: those of await result and log the error
    }

  }


  /**
   * Create Multiple Suggested FreeConcept
    *
    * @param suggestedPrefLabels
   * @param scheme
   * @param checkDuplicates
   * @return
   */
  override def createSuggestedFreeConceptsConcurrent(suggestedPrefLabels: List[LanguageLiteral], scheme: String, checkDuplicates: Boolean): List[String] = {


    import system.dispatcher

    import spray.httpx.marshalling._
    import spray.httpx.SprayJsonSupport._

    val pipeline      = addCredentials(BasicHttpCredentials("superadmin", "poolparty")) ~> sendReceive



    val ress          =  suggestedPrefLabels map  { e =>

                              val suggestion    = SuggestFreeConcept(List(e), checkDuplicates, Some(List(Uri(scheme))), None, None, None, None)
                              val request       = Post(s"$thesaurusapiEndpoint/$coreProjectId/suggestFreeConcept", marshal(suggestion))
                              pipeline(request)

                         }


    getSuggestedsFromFutureHttpResponse(Future.sequence(ress)) match {

      case None    => List[String]()
      case Some(e) => e
    }



  }


  private def getSuggestedsFromFutureHttpResponse(ress: Future[List[HttpResponse]]): Option[List[String]] = {


    try {

      val responses = Await.result(ress, Duration.Inf)

      val results = responses map { response =>

        response.status match {

          case StatusCodes.OK => {

            if (response.entity.isEmpty)
              ""
            else {

              println("##\n\n " + response.entity.data.asString + "\n\n##")

              response.entity.data.asString
            }
          }
          case _ => {println("##\n\nThere was an error. Response: " + response.toString + "\n\n##");""}
        }

      }

      Some(results)

    }
    catch {

      case e:Throwable => {println(s"\n\n***\n\nError: \n$e\n\n***\n\n"); None}

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


  def getIdbDocWebTopic(conceptUri: String): List[String] = {

    thesaurusSparqlConsumer.getEca(sparqlEndpoint, conceptUri)

  }

  /**
    * Fetch All the Semantic search label
    *
    * @param conceptUri
    * @return
    */
  def getIndexableLabels(conceptUri: String): List[LanguageLiteral] = {

    thesaurusSparqlConsumer.getConceptIndexableLabels(sparqlEndpoint, conceptUri)
  }

  def getAllLangPrefLabels(conceptUri: String): List[LanguageLiteral] = {

    thesaurusSparqlConsumer.getConceptAllLangPrefLabels(sparqlEndpoint, conceptUri)

  }

  def getBroaderLabels(conceptUri: String): List[LanguageLiteral] = {

    thesaurusSparqlConsumer.getAllLangBroaderLabels(sparqlEndpoint, conceptUri)

  }

  def getNarrowerLabels(conceptUri: String): List[LanguageLiteral] = {

    thesaurusSparqlConsumer.getAllLangNarrowerLabels(sparqlEndpoint, conceptUri)

  }

  def getRelatedLabels(conceptUri: String): List[LanguageLiteral] = {

    thesaurusSparqlConsumer.getAllLangRelatedLabels(sparqlEndpoint, conceptUri)

  }

  def getAltLabels(conceptUri: String): List[LanguageLiteral] = {

    thesaurusSparqlConsumer.getAllLangAltLabels(sparqlEndpoint, conceptUri)

  }

  override def getauthorRepecId(authorUri: String): String = {

    thesaurusSparqlConsumer.getRepecId(sparqlEndpoint, authorUri) match {
      case head::Nil => head
      case _ => ""
    }

  }
}
