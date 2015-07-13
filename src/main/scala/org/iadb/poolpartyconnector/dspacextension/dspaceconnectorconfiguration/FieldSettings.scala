package org.iadb.poolpartyconnector.dspacextension.dspaceconnectorconfiguration

/**
 * Created by Daniel Maatari Okouya on 7/8/15.
 *
 * The settings of each metadata field in Dspace
 *
 */

case class FieldSettings(fieldName          : String,
                         hasTreeBrowser     : Boolean,
                         isMultilanguage    : Boolean,
                         languages          : List[String],
                         isClosed           : Boolean,
                         scheme             : String,
                         poolpartyProjectId : String )
