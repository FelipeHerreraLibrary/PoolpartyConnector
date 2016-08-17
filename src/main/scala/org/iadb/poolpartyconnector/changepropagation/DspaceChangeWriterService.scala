package org.iadb.poolpartyconnector.changepropagation

import akka.event.LoggingAdapter
import com.softwaremill.tagging._
import org.dspace.content.{DCDate, Item}
import org.dspace.core.Context
import org.iadb.poolpartyconnector.dspacextension.dspacedataoperationadaptation.DspaceDcValueUtils
import org.iadb.poolpartyconnector.dspacextension.dspacedataoperationadaptation.schememapping.SchemeMetadatumMappingService
import org.iadb.poolpartyconnector.thesaurusoperation.ThesaurusSparqlConsumer

import scala.util.Try

/**
  * Created by Daniel Maatari Okouya on 8/13/16.
  */
trait DspaceChangeWriterService {

  def updateItemsModificationDate(c: ConceptUpdatePropagation, loggingAdapter: LoggingAdapter) : Try[Unit]

  def mergeItems(c:ConceptMergePropagation, loggingAdapter: LoggingAdapter) : Try[Unit]

}

case class DspaceChangeWriterServiceImpl (schemeMetadatumMappingService: SchemeMetadatumMappingService,
  thesaurusSparqlConsumer: ThesaurusSparqlConsumer,
  SparqlEndPoint: String @@ EndPoint,
  context: Option[Context]) extends DspaceChangeWriterService {



  override def updateItemsModificationDate(c: ConceptUpdatePropagation, log: LoggingAdapter): Try[Unit] = Try {

    context.fold() { ctx =>

      thesaurusSparqlConsumer.getScheme(SparqlEndPoint, c.subjectOfChangeURI) match {
        case Nil        =>
        case head::_ => {
          schemeMetadatumMappingService.getMetadatumforScheme(head) foreach { metadatum =>
            val res = Item.findByAuthorityValue(ctx, metadatum.ns.get, metadatum.elt.get, metadatum.qual.getOrElse(null), c.subjectOfChangeURI)
            while (res.hasNext) {
              val item = res.next()
              log.info(s"\nFound item - name: ${item.getName} - handle: ${item.getHandle}\n")
              item.clearMetadata("dc", "date", "modified", Item.ANY)
              item.addMetadata("dc", "date", "modified", null, DCDate.getCurrent.toString)
              item.update()
              log.info(s"\n updated item - name: ${item.getName} - handle: ${item.getHandle} by updating its Date\n")
            }
            ctx.commit()
          }
        }
      }
    }
  }


  /**
    * MergeUpdate: Find All Item associated to the Subject of Change ConeptURI
    * and then replace the Subject of Change  URI by the Merging Concept URI.
    * @param c
    * @param log
    * @return
    */
  override def mergeItems(c: ConceptMergePropagation, log: LoggingAdapter): Try[Unit] = Try {


    context.fold() { ctx =>

      thesaurusSparqlConsumer.getScheme(SparqlEndPoint, c.mergingConceptURI) match {
        case Nil        =>
        case head::_ => {

          schemeMetadatumMappingService.getMetadatumforScheme(head) foreach { metadatum =>

            val res = Item.findByAuthorityValue(ctx, metadatum.ns.get, metadatum.elt.get, metadatum.qual.getOrElse(null), c.subjectOfChangeURI)

            while (res.hasNext) {

              val item = res.next()

              log.info(s"\nFound item - name: ${item.getName} - handle: ${item.getHandle}\n")

              val metadata = item.getMetadata(metadatum.toString).toList
              item.clearMetadata(metadatum.ns.get, metadatum.elt.get, metadatum.qual.getOrElse(null), Item.ANY)

              metadata.map {
                case e if e.authority == c.subjectOfChangeURI => DspaceDcValueUtils.getDCValue(e.element, e.qualifier, c.mergingConceptURI, e.language, e.schema, c.mergingConceptURI, e.confidence)
                case f => f
              }.foreach(e => item.addMetadata(e.schema, e.element, e.qualifier, e.language, e.value, e.authority, e.confidence))

              item.clearMetadata("dc", "date", "modified", Item.ANY)
              item.addMetadata("dc", "date", "modified", null, DCDate.getCurrent.toString)
              item.update()

              log.info(s"\n updated item - name: ${item.getName} - handle: ${item.getHandle} by replacing reference to ${c.subjectOfChangeURI} to reference to ${c.mergingConceptURI}\n")
            }

            ctx.commit()

          }

        }
      }
    }
  }




}
