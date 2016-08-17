object EventDesc extends Enumeration {
  type EventDesc = Value
  val PrefAdded = Value("PreferedLabelAdded")
  val PrefChanged = Value("PreferedLabelChanged")
}


EventDesc.PrefAdded.toString == "PreferedLabelAdded"


EventDesc.withName()