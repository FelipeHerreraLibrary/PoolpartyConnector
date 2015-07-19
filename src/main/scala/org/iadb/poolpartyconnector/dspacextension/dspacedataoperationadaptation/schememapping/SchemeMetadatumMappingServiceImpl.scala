package org.iadb.poolpartyconnector.dspacextension.dspacedataoperationadaptation.schememapping

import org.iadb.poolpartyconnector.dspacextension.dspaceconnectorconfiguration.DspacePoolPartyConnectorSettings
import org.iadb.poolpartyconnector.dspacextension.dspacedatamodeladaptation.DspaceMetadatum


trait SchemeMetadatumMappingService {

  def getFieldsforScheme(uri: String): List[DspaceMetadatum]

}

/**
 * Created by Daniel Maatari Okouya on 7/15/15.
 */
case class SchemeMetadatumMappingServiceImpl(connectorSettings: DspacePoolPartyConnectorSettings) extends SchemeMetadatumMappingService {


 val fieldToschemeMap =  (connectorSettings.fieldsSettingsList map { fieldsettings =>

   (fieldsettings.fieldName, fieldsettings.scheme)

 }).toMap




  override def getFieldsforScheme(uri: String): List[DspaceMetadatum] = {

    fieldToschemeMap.withFilter(p => p._2 == uri).flatMap(e => List(DspaceMetadatum(e._1))).toList

  }
}
