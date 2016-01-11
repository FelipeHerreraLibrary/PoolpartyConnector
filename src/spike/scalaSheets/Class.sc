//You can combine auxiliary constructor and case class companion object apply

case class testclass(name: String, host:String) {

  def this() = {
    this("", "")
  }
}

object testclass {

  def apply() = {
    new testclass()
  }
}

testclass("daniel", "okouya")
testclass()





