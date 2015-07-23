package org.iadb.poolpartyconnector.conceptsrecommendation



import org.iadb.poolpartyconnector.conceptsrecommendation.JsonProtocolSpecification.{Concept, Document, ConceptResults}
import java.io.{InputStream}

import akka.actor.ActorSystem

import akka.util.Timeout

import JsonProtocolSpecification.PoolPartyJsonProtocol._
import org.iadb.poolpartyconnector.connectorconfiguration.CorpusScoringSettings
import org.iadb.poolpartyconnector.dspacextension.dspaceconnectorconfiguration.DspacePoolPartyConnectorSettings
import org.iadb.poolpartyconnector.dspacextension.springadaptation.ActorSystemSpringWrapperBean

import org.iadb.poolpartyconnector.utils.TemporaryCopyUtils
import spray.client.pipelining._
import spray.http._

import scala.concurrent.{ExecutionContext, Future, Await}
import scala.concurrent.duration._
import scala.util.{Failure, Success}

import spray.json._




/**
 * Created by Daniel Maatari Okouya on 6/6/15.
 */
trait RelevantConceptsRecommendationService {

  def recommendMetadata(inputstream: InputStream, lang: String) : ConceptResults

}


/**
 * Created by Daniel Maatari Okouya on 6/2/15.
 */

case class RelevantConceptsRecommendationServicePoolPartyImpl(actorSystem: ActorSystem, connectorSettings: DspacePoolPartyConnectorSettings, name: String = "RelevantConceptsRecommendationService") extends RelevantConceptsRecommendationService {


  implicit private val requestTimeout = Timeout(800 seconds)
  implicit private val system         = actorSystem

  private val extratorapiEndpoint     = connectorSettings.poolpartyServerSettings.extratorapiEndpoint
  private val coreProjectId           = connectorSettings.poolpartyServerSettings.coreProjectId
  private val corpusSettings          = connectorSettings.poolpartyServerSettings.coprusSettings
  private val numConceptsPool         = connectorSettings.poolpartyServerSettings.maxConceptsExtractionPool
  private val numTermsPool            = connectorSettings.poolpartyServerSettings.maxTermsExtractionPool
  private val fieldSettingsList       = connectorSettings.fieldsSettingsList

  //TODO Exception Handling
  def this(systembean: ActorSystemSpringWrapperBean, connectorSettings: DspacePoolPartyConnectorSettings) = {

    this(systembean.getActorSystem, connectorSettings)
  }




  /**
   *
   * @param inputstream
   * @return
   */

  def recommendMetadata(inputstream: InputStream, lang: String = "en"): ConceptResults = {

    println("Start Recommanding Process ...")

    import system.dispatcher

    val tmpToClassify = TemporaryCopyUtils.getemporaryCopy(inputstream)

    val pipeline      = addCredentials(BasicHttpCredentials("superadmin", "poolparty")) ~> sendReceive

    val payload       = MultipartFormData(Seq(BodyPart(tmpToClassify, "file"/*,MediaTypes.`application/pdf`*/)))

    val corpus        = getCorpus(lang, corpusSettings)

    val request       = Post(s"$extratorapiEndpoint?projectId=$coreProjectId&language=$lang&corpusScoring=$corpus&numberOfConcepts=$numConceptsPool&numberOfTerms=$numTermsPool", payload)

    val res           = pipeline(request)


    //logResponseWithTime(res)

    val conceptResults = getConceptResultsFromFutureHttpResponse(res)

    TemporaryCopyUtils.deleteTemporaryCopy(tmpToClassify)

    filterResultsWithExtractionSettings(conceptResults)


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
  private def getConceptResultsFromFutureHttpResponse(res: Future[HttpResponse]): ConceptResults = {

    try {

      val response = Await.result(res, Duration.Inf)


      response.status match {
        case StatusCodes.OK => {if (response.entity.isEmpty) new ConceptResults(None, None)  else response.entity.data.asString.parseJson.convertTo[ConceptResults]}
        case _ => {println("\n\nThe error response is: " + response.entity.asString + "\n\n" ) ; new ConceptResults(None, None)}
      }

    }catch {

      case e:Throwable => {println(e.toString) ; new ConceptResults(None, None)}
      //TODO Add the possible throwable here: those of await result and log the error
    }

  }


  /**
   * Install Loggin callback on the future http response
   * log the time
   * @param res
   */
  private def logResponseWithTime(res: Future[HttpResponse]) (implicit ec: ExecutionContext): Unit = {


    val initTime      = System.currentTimeMillis()
    def time()        = System.currentTimeMillis()


    res.onComplete {

      case Success(e) => {

        println(s"Time of Execution on is : " + (time() - initTime) + "\n\n\n" + e + "\n\n\n" + e.entity.data.asString.parseJson.prettyPrint)

        val conceptResults = e.entity.data.asString.parseJson.convertTo[ConceptResults]

      }

      case Failure(e) => {
        println(s"Time of Execution " + (time() - initTime) + "\n\n\n" + e + "\n\n\n" + e.toString)
      }
    }
  }


  def filterResultsWithExtractionSettings(conceptResults: ConceptResults): ConceptResults = {

    import system.dispatcher

    val mylist: Iterable[Future[List[Concept]]] = for (fieldSettings <- fieldSettingsList if fieldSettings.maxConceptsExtraction > 0; doc <- conceptResults.document; concepts <- doc.concepts) yield {

       Future {
         //println("in the future")
         //println(concepts.toString())
        // println(fieldSettings.toString())

         val schemeFiltered = concepts.withFilter(e => e.conceptSchemes.exists(x => x.uri == fieldSettings.scheme)).flatMap(e=> List(e))

         //println("List is filtered")

         //println(schemeFiltered.toString())

          schemeFiltered.size match {
           case e if e > fieldSettings.maxConceptsExtraction => schemeFiltered.take(fieldSettings.maxConceptsExtraction)
           case _ => schemeFiltered
         }
       }

     }

    val res = Await.result(Future.sequence(mylist), Duration.Inf).flatten.toList

    val distinctRes = res.distinct

    conceptResults.document match {
      case Some(e) => conceptResults.copy(document = Some(conceptResults.document.get.copy(concepts = Some(distinctRes))))
      case None => conceptResults
    }

  }

  private def getCorpus(lang:String, corpusSettings: CorpusScoringSettings) = {

    lang match {
      case "en" => corpusSettings.corpusEN
      case "es" => corpusSettings.corpusES
      case "fr" => corpusSettings.corpusFR
      case "pt" => corpusSettings.corpusPT
      case _ => corpusSettings.corpusEN
    }

  }
}


