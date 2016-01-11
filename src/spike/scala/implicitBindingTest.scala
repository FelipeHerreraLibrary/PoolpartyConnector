package test

object Test {



  def testSamePackage() = {print(x)}

  def testWildcardImport() = {import ImportWildcar._;  print(x)}


  def testExplicitImport() = {import Explicit.x ;import ImportWildcar._;  print(x)}

  def testInlineDefinition() = {val x = "Inline def"; import Explicit.x ;import ImportWildcar._;  print(x)}

  def main(args : Array[String]) : Unit = {
    testSamePackage()
    testWildcardImport()
    testExplicitImport()
    testInlineDefinition()
  }

}



