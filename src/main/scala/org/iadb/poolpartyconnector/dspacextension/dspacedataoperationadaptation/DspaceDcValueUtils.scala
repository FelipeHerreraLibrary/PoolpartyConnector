package org.iadb.poolpartyconnector.dspacextension.dspacedataoperationadaptation

import org.dspace.content.DCValue
//import org.iadb.poolpartyconnector.thesaurusoperation.CachedConcept

/**
 * Created by Daniel Maatari Okouya on 6/21/15.
 */
object DspaceDcValueUtils {

  /**
   * Helper method to build a DCValue quickly
   *
   * @param element
   * @param qualifier
   * @param value
   * @param language
   * @param schema
   * @param authority
   * @param conf
   * @return
   */
  def getDCValue(element: String, qualifier: String, value: String, language: String, schema: String, authority:String, conf:Int): DCValue = {

    val dcValue = new DCValue

    dcValue.element = element
    dcValue.qualifier = qualifier
    dcValue.value = value
    dcValue.language = language
    dcValue.schema = schema
    dcValue.authority = authority
    dcValue.confidence =conf

    dcValue
  }
}
