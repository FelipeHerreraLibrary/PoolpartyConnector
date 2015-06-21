package org.iadb.poolpartyconnector.thesauruscaching

/**
 * Created by Daniel Maatari Okouya on 6/21/15.
 *
 * A CachedConcept
 */
 trait CachedConcept {
  def uri:String
  def authority:String
  def confidence: Int
}

/**
 *
 * @param uri
 * @param authority
 */
case class IndexCachedConcept(override val uri:String,
                              override val authority:String,
                              override val confidence: Int) extends CachedConcept

/**
 *
 * @param uri
 * @param authority
 */
case class InProgressCachedConcept(override val uri:String,
                                   override val authority:String,
                                   override val confidence: Int) extends CachedConcept