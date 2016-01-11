

val x: Int = 2
trait MyImplicits {
  implicit def stringToInt(t: String) = t.toInt
  implicit def intToString(t: Int) = t.toString
  implicit def doubletoInt(t: Double) = t.toInt
}

object impl extends MyImplicits

import impl._


val x1: Int = 2.3
val x2: String = 2
val x3: Int = "2"

implicit class RangeMaker (left: Int) {
  def ---> (right: Int) = left to right
}

//implicit def toRangeMaker(int:Int) = new RangeMaker(int)




1 ---> 2

