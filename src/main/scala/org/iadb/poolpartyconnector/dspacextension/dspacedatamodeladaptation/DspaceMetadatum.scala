package org.iadb.poolpartyconnector.dspacextension.dspacedatamodeladaptation

/**
 * Created by Daniel Maatari Okouya on 7/15/15.
 */
case class DspaceMetadatum(ns: Option[String], elt: Option[String], qual: Option[String]) {


  override def toString = {

    this match {
      case DspaceMetadatum(Some(e), Some(a), None) => e + "." + a
      case DspaceMetadatum(Some(e), Some(a), Some(c)) => e + "." + a + "." + c
      case DspaceMetadatum(None, None, None) => ""
      case _ => ""
    }
  }

}



object DspaceMetadatum {


  def apply(metadatumName: String) = {

    val parts = metadatumName.split('.')

    parts.size match {

      case 2 => new DspaceMetadatum(Some(parts{0}), Some(parts{1}), None)

      case 3 => new DspaceMetadatum(Some(parts{0}), Some(parts{1}), Some(parts{2}))

      case _ => new DspaceMetadatum(None, None, None)

    }

  }

}
