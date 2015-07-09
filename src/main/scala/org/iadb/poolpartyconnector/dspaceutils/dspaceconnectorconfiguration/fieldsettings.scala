package org.iadb.poolpartyconnector.dspaceutils.dspaceconnectorconfiguration

/**
 * Created by Daniel Maatari Okouya on 7/8/15.
 */

case class FieldSettings(fieldName          : String,
                         hasTreeBrowser     : Boolean,
                         isMultilanguage    : Boolean,
                         languages          : List[String],
                         isClosed           : Boolean,
                         scheme             : String,
                         poolpartyProjectId : String )
