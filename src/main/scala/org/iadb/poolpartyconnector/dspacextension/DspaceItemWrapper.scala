package org.iadb.poolpartyconnector.dspacextension

import org.dspace.content.Item



trait DspaceItemWrapper {


  def addMetadata(schema: String, element: String, qualifier: String, lang: String, value: String, authority: String, confidence: Int): Unit

  def item: Item

  def update: Unit

}


/**
 * Created by Daniel Maatari Okouya on 6/14/15.
 *
 * A class that wraps a dspace item. Very useful to unit test in our domain.
 *
 * We can mock it and verify our interaction with item by using the wrapper.
 *
 * It also allow us to remain independant of the particular Dspace API i.e. the implementation
 *
 * It helps in testing: We can mock this class as it is a type that we own.
 *
 */
case class DspaceItemWrapperImpl(item: Item)  extends DspaceItemWrapper{


  /**
   *
   * Wrapper around the equivalent Dspace Item method
   *
   * @param schema
   * @param element
   * @param qualifier
   * @param lang
   * @param value
   * @param authority
   * @param confidence
   */
  def addMetadata(schema: String, element: String, qualifier: String, lang: String, value: String, authority: String, confidence: Int): Unit = {


    item.addMetadata(schema,element,qualifier,lang,value,authority,confidence)

  }

  def update: Unit = item.update()
}


