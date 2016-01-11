case class DspaceMetadatum(ns: Option[String], elt: Option[String], qual: Option[String]) {


  //Not acceptable
  /*def this(metadatumName: String) = {

    this(Some(""), Some(""), None)

    metadatumName.split('.') match {

      case Array(e1:String,e2:String, _*) => this(Some(e1), Some(e2), None)

      //case 3 => this(Some(parts{0}), Some(parts{1}), Some(parts{2}))

      case _ => this(None, None, None)

    }

  }*/


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

    val parts = metadatumName.trim.split('.')

    parts.size match {

      case 2 => new DspaceMetadatum(Some(parts{0}), Some(parts{1}), None)

      case 3 => new DspaceMetadatum(Some(parts{0}), Some(parts{1}), Some(parts{2}))

      case _ => new DspaceMetadatum(None, None, None)

    }

  }

}

DspaceMetadatum(Some("dc"), Some("subject"), None)

"dc. ".trim.split('.')

//new DspaceMetadatum("")