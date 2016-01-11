

/*trait Maat[T] {
  def maatize[T](s1:T, s2:T): T
}

object Maat {

  implicit object MaatString extends Maat[String] {

    override def maatize(s1: String, s2: String): String = s1 + s2
  }
}*/


trait Maat[T] {
  def maatize(s1: T, s2: T): T
}

object Maat {
  implicit object MaatString extends Maat[String] {
    def maatize(s1: String, s2: String): String = s1 + s2
  }
}





def maatize[T: Maat](s1: T, s2: T)  = {
  implicitly[Maat[T]].maatize(s1, s2)
}

maatize("hello", " you")

implicit def toInt2(s: String):Int = s.toInt


val myint: Int = 2 * "2"