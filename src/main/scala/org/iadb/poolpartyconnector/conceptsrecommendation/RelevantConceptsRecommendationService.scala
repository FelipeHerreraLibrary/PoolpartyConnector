package org.iadb.poolpartyconnector.conceptsrecommendation



import JsonProtocolSpecification.ConceptResults
import java.io.{InputStream}

import akka.actor.ActorSystem

import akka.util.Timeout
import org.iadb.poolpartyconnector.dspaceutils.ActorSystemSpringWrapperBean

import JsonProtocolSpecification.PoolPartyJsonProtocol._

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

case class RelevantConceptsRecommendationServicePoolPartyImpl(actorSystem: ActorSystem, name: String = "RelevantConceptsRecommendationService") extends RelevantConceptsRecommendationService {


  implicit private val requestTimeout = Timeout(800 seconds)
  implicit private val system         = actorSystem



  //TODO Exception Handling
  def this(systembean: ActorSystemSpringWrapperBean) = {

    this(systembean.getActorSystem)
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

    val payload       = MultipartFormData(Seq(BodyPart(tmpToClassify, "file", MediaTypes.`application/pdf`)))

    //val request     = Post("http://thesaurus.iadb.org/extractor/api/extract?projectId=1DCE1C5E-6393-0001-F9FD-20401160CAC0&language=en", payload)
    val request       = Post(s"http://127.0.0.1:8086/extractor/api/extract?projectId=1DCDFC5D-3876-0001-EEE6-BC9C1B8016CF&language=$lang", payload)

    val res           = pipeline(request)


    //logResponseWithTime(res)


    val conceptResults = getConceptResultsFromFutureHttpResponse(res)

    TemporaryCopyUtils.deleteTemporaryCopy(tmpToClassify)

    conceptResults

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

      val reponse = Await.result(res, Duration.Inf)

      reponse.entity.data.asString.parseJson.convertTo[ConceptResults]

    }catch {

      case e:Throwable => new ConceptResults(None, None)
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

        println(conceptResults.toString())

      }

      case Failure(e) => {
        println(s"Time of Execution " + (time() - initTime) + "\n\n\n" + e + "\n\n\n" + e.toString)
      }
    }
  }

}
