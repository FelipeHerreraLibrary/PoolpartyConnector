package org.iadb.poolpartyconnector.changepropagation

import akka.actor.{Actor, ActorLogging, Props}
import org.dspace.core.Context
import org.iadb.poolpartyconnector.changepropagation.ChangePropagationActor.ACK
import spray.caching.{Cache}


import scala.util.{Failure, Success, Try}

object ChangeWriterActor {

   def props(cache: Cache[String], dWriterService: DspaceChangeWriterService) = Props(new ChangeWriterActor(cache, dWriterService))
}

/**
  * Created by Daniel Maatari Okouya on 8/8/16.
  */
class ChangeWriterActor(cache: Cache[String], dWriterService: DspaceChangeWriterService) extends Actor with ActorLogging {




  override def receive: Receive = {
    case m: ConceptMergePropagation => {
      clearCacheFromConcept(m)
      dWriterService.mergeItems(m,log)
      log.info(s"\nPropagated update for: ${m.toString}\n")
      sender() ! ACK
    }
    case s: ConceptUpdatePropagation => {

      clearCacheFromConcept(s)
      dWriterService.updateItemsModificationDate(s,log)
      log.info(s"\nPropagated update for:  ${s.toString}\n")
      sender() ! ACK
    }
    case e => log.info(s"weird message received: $e")
  }


  private def clearCacheFromConcept(c: ConceptUpdatePropagation) = {
    cache.remove(c.subjectOfChangeURI + "en")
    cache.remove(c.subjectOfChangeURI + "es")
    cache.remove(c.subjectOfChangeURI + "fr")
    cache.remove(c.subjectOfChangeURI + "pt")
  }

  private def clearCacheFromConcept(c: ConceptMergePropagation) = {
    cache.remove(c.subjectOfChangeURI + "en")
    cache.remove(c.subjectOfChangeURI + "es")
    cache.remove(c.subjectOfChangeURI + "fr")
    cache.remove(c.subjectOfChangeURI + "pt")

    cache.remove(c.mergingConceptURI + "en")
    cache.remove(c.mergingConceptURI + "es")
    cache.remove(c.mergingConceptURI + "fr")
    cache.remove(c.mergingConceptURI + "pt")
  }
}
