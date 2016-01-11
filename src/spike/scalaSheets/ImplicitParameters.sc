

trait FooTrait {
  implicit val value = "implicit in trait"
}
case class Foo(name: String)
object Foo extends FooTrait {
  implicit val x  = new Foo("an Implicit Food")
  implicit val xl = List[Foo](new Foo("Implicit List[Foo]'s Foo"))


  implicit val fooJsonSerializer = new JsonSerializer[Foo] {
    override def asJson(a: Foo): String = a.name
  }
}

/**Implicit resolution not from scope but from the Type of the parameter**/

//Implicit parameter simple
def printFoo(implicit fooVal: Foo) = fooVal.name
printFoo

def printFooList(implicit fooValList: List[Foo]) = fooValList.head.name
printFooList

//Implicitly resolution
implicitly[List[Foo]].head.name
printFooList(implicitly[List[Foo]])


//TypeClass
trait JsonSerializer[T] {
  def asJson(a: T): String
}
def toJson[T](a: T)(implicit o: JsonSerializer[T]) = o.asJson(a)



toJson(new Foo("Foo to Binarize"))


/**Implicit resolution not from the parameter, where the implicit comes from the trait**/

def takeimplitfromTrait(implicit what: String) = what.toString

import Foo._
takeimplitfromTrait


