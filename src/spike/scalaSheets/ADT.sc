sealed trait aType0

sealed trait aType1  extends aType0 {
  val aval: String
}


case class aClass(aval: String) extends aType1(aval)



val aObject = aClass("hello")


aObject match {

  case aClass(e) =>
}


