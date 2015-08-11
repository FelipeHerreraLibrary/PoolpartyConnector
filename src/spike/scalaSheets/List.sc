val mylist = List(1,2,3,4,5,6)

2::mylist
if (mylist.size > 3) {

  mylist.dropRight(mylist.size - 3)
}


mylist.foldRight (List[Int]()) ((a:Int, b:List[Int]) => a::b)


case class Person(name: String, id: Int)

val theList = List(Person("xxx", 2),Person("yyy", 1), Person("zzz", 3), Person("aaa", 4), Person("eee", 0))


val toZip = List("rene", "daniel", "okongua", "ikiya", "ibamba")



theList.zip(toZip).map(e => Person(e._2, e._1.id))



import scala.collection.JavaConverters._

List(1,2,3,4).asJava



List[Int]() map {e => e + 2 }
