package org.iadb.poolpartyconnector.dspacextension.dspacedataoperationadaptation.schememapping

import org.iadb.poolpartyconnector.dspacextension.dspaceconnectorconfiguration.DspacePoolPartyConnectorSettings
import org.iadb.poolpartyconnector.dspacextension.dspacedatamodeladaptation.DspaceMetadatum


trait SchemeMetadatumMappingService {

  def mappsMetadatumWithName(toString: String): Boolean


  def getMetadatumforScheme(uri: String): List[DspaceMetadatum]

}

/**
 * Created by Daniel Maatari Okouya on 7/15/15.
 */
case class SchemeMetadatumMappingServiceImpl(connectorSettings: DspacePoolPartyConnectorSettings) extends SchemeMetadatumMappingService {


 val fieldToschemeMap =  (connectorSettings.fieldsSettingsList map { fieldsettings =>

   (fieldsettings.fieldName, fieldsettings.scheme)

 }).toMap




  override def getMetadatumforScheme(uri: String): List[DspaceMetadatum] = {

    fieldToschemeMap.withFilter(p => p._2 == uri).flatMap(e => List(DspaceMetadatum(e._1))).toList

  }

  override def mappsMetadatumWithName(metadatumName: String): Boolean = {

    if (metadatumName == null || metadatumName.isEmpty) false
    else fieldToschemeMap.contains(metadatumName)

  }
}
