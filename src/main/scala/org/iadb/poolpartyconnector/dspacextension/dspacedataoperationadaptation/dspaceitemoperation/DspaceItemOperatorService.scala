package org.iadb.poolpartyconnector.dspacextension.dspacedataoperationadaptation.dspaceitemoperation

import org.dspace.content.Item
import org.dspace.content.authority.Choices
import org.iadb.poolpartyconnector.conceptsrecommendation.JsonProtocolSpecification.{Concept, ConceptResults, FreeTerm}
import org.iadb.poolpartyconnector.dspacextension.dspacedatamodeladaptation.{DspaceMetadatum, DspaceItemWrapper}
import org.iadb.poolpartyconnector.dspacextension.dspacedataoperationadaptation.schememapping.SchemeMetadatumMappingService

/**
 * Created by Daniel Maatari Okouya on 6/6/15.
 */


case class DspaceItemOperatorService(schemeMappingService: SchemeMetadatumMappingService) {



  /**
   *
   * Take an Item and a List of recommended metadata and returned the item updated with this metadata
   *
   * @param itemWrapper
   * @param poolpartyconceptresult
   * @return
   */
  def updateItemMetadataWithRecommendedConcepts(itemWrapper : DspaceItemWrapper, poolpartyconceptresult: ConceptResults, lang: String = "en"): Item = {

    poolpartyconceptresult.document foreach { doc =>
      doc.concepts foreach { conceptList =>
        //val list = if (conceptList.size < 12) conceptList else conceptList.dropRight(conceptList.size - 12)
        conceptList foreach { concept =>
          val dsmetadata = getConceptInDspaceScheme(concept, Choices.CF_UNCERTAIN)
          //(schema: String, element: String, qualifier: String, lang: String, value: String, authority: String, confidence: Int)
          itemWrapper.addMetadata(dsmetadata._1, dsmetadata._2, dsmetadata._3, dsmetadata._4, dsmetadata._5, dsmetadata._6, dsmetadata._7)
        }
      }

      doc.freeTerms foreach { freeTermList =>
        freeTermList foreach { freeTerm =>
          val dsmetadata = getFreeTermInDspaceScheme(freeTerm, -3, lang)
          itemWrapper.addMetadata(dsmetadata._1, dsmetadata._2, dsmetadata._3, dsmetadata._4, dsmetadata._5, dsmetadata._6, dsmetadata._7)
        }
      }

    }

    itemWrapper.update
    itemWrapper.item
  }


  /**
   *
   * Convert a PoolParty concept in a form aligned with Dspace representation
   *
   * @param concept A PoolParty Concept
   * @return
   */
  private def getConceptInDspaceScheme(concept: Concept, confidence: Int) : Tuple7[String, String, String, String, String, String, Int] = {



    val dspaceMetadatum = schemeMappingService.getFieldsforScheme(concept.conceptSchemes.head.uri).head

    (dspaceMetadatum.ns.get, dspaceMetadatum.elt.get, dspaceMetadatum.qual.getOrElse(null), concept.language, concept.uri, concept.uri, confidence)
    //(schema: String, element: String, qualifier: String, lang: String, value: String, authority: String, confidence: Int)
  }


  /**
   *
   * Convert a PoolParty freeTerm in a form aligned with Dspace representation
   *
   * @param freeTerm A PoolParty Concept
   * @return
   */
  private def getFreeTermInDspaceScheme(freeTerm: FreeTerm, confidence: Int, lang: String = "en") : Tuple7[String, String, String, String, String, String, Int] = {

    //(schema: String, element: String, qualifier: String, lang: String, value: String, authority: String, confidence: Int)
    ("dc", "subject", null, lang, freeTerm.textValue, "suggest", confidence)

  }

}
