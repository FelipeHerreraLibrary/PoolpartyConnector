name := "PoolpartyConnector"

organization := "org.iadb"

version := "0.0.3-SNAPSHOT"

scalaVersion := "2.11.7"




resolvers += "spray repo" at "http://repo.spray.io"
resolvers += "Local Maven" at Path.userHome.asFile.toURI.toURL + ".m2/repository"

scalacOptions in ThisBuild ++= Seq("-language:postfixOps", "-feature")
scalacOptions ++= Seq("-feature")



libraryDependencies ++=
  Seq("io.spray" %% "spray-client" % "1.3.3",
      "io.spray" %%  "spray-json" % "1.3.2",
      "io.spray" %% "spray-caching" % "1.3.3",
      "com.typesafe.akka" %% "akka-actor" % "2.3.11",
      "org.w3" %% "banana-sesame" % "0.8.2-SNAPSHOT",
      "org.scala-lang.modules" % "scala-java8-compat_2.11" % "0.7.0",
      "org.dspace" % "dspace-api" % "1.8.2" exclude("org.slf4j", "slf4j-api") exclude("log4j","log4j"),
      "org.slf4j" %  "slf4j-log4j12" % "1.7.12",
      "com.softwaremill.macwire" %% "macros" % "1.0.5",
      "com.softwaremill.macwire" %% "runtime" % "1.0.5",
      "com.typesafe" % "config" % "1.3.0",
      "org.scalamock" %% "scalamock-scalatest-support" % "3.2" % "test",
      "org.scalatest" %% "scalatest" % "2.2.4" % "test")


//dependencyOverrides += "xml-apis" % "xml-apis" % "1.3.02"

//dependencyOverrides += "xerces" % "xercesImpl" % "2.8.1"



dependencyOverrides += "org.scala-lang" % "scala-reflect" % "2.11.7"

dependencyOverrides += "org.scala-lang.modules" %% "scala-xml" % "1.0.4"

dependencyOverrides += "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4"
