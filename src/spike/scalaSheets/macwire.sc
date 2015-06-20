
case class Person(name: String)

case class People()

trait amodule {

  import com.softwaremill.macwire._

  lazy val aPeople = wire[People]
  lazy val getPerson = (name:String) => {wire[Person]}

}

object mymodule extends amodule

mymodule.getPerson("daniel").name

mymodule.getPerson("rene").name