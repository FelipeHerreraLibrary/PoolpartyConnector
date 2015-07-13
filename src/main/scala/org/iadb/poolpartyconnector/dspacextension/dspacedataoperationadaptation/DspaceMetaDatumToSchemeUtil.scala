package org.iadb.poolpartyconnector.dspacextension.dspacedataoperationadaptation

/**
 * Created by Daniel Maatari Okouya on 6/19/15.
 *
 * A Utility class to convert between Dspace MetaDatum and SKOS Scheme model of representation
 */

object DspaceMetaDatumToSchemeUtil {


  /**
   * A Utility method that convert a Dspace metadatum into a corresponding Skos Scheme
   *
   * @param s A string representing a dspace MetaDatum i.e. metadatum.toString()
   * @return
   */
  def asScheme(s: String): Option[String] = {

    s match {
      case null => None
      case "dc.subject" => Some("http://thesaurus.iadb.org/publicthesauri/IdBTopics")
      case "dc.contributor.author" => Some("http://thesaurus.iadb.org/publicthesauri/IdBAuthors")
      case "dc.contributor.institution" => Some("http://thesaurus.iadb.org/publicthesauri/IdBInstitutions")
      case "dc.coverage.placename" => Some("http://thesaurus.iadb.org/publicthesauri/IdBCountries")
      case "iadb.department" => Some("http://thesaurus.iadb.org/publicthesauri/IdBDepartments")
      case _ => None
    }
  }



}
