package org.iadb.poolpartyconnector.utils

/**
 * Created by Daniel Maatari Okouya on 6/24/15.
 */
object ConceptUriChecker {

  def isValidConceptUri(concepturi: String): Boolean = {

    concepturi match {

      case null => false
      case ""   => false
      case e if e.startsWith("http://thesaurus.iadb.org") => true
      case _ => false
    }
  }
}
