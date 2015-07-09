name := "PoolpartyConnector"

organization := "org.iadb"

version := "0.0.3-SNAPSHOT"

scalaVersion := "2.11.6"




resolvers += "spray repo" at "http://repo.spray.io"
resolvers += "Local Maven" at Path.userHome.asFile.toURI.toURL + ".m2/repository"

scalacOptions in ThisBuild ++= Seq("-language:postfixOps", "-feature")

//configuration in ThisBuild += IntegrationTest


libraryDependencies ++=
  Seq("io.spray" %% "spray-client" % "1.3.3",
      "io.spray" %%  "spray-json" % "1.3.2",
      "com.typesafe.akka" %% "akka-actor" % "2.3.11",
      "org.dspace" % "dspace-api" % "1.8.2",
      "com.softwaremill.macwire" %% "macros" % "1.0.5",
      "com.softwaremill.macwire" %% "runtime" % "1.0.5",
      "com.typesafe" % "config" % "1.3.0",
      "org.scalamock" %% "scalamock-scalatest-support" % "3.2" % "test",
      "javax.servlet" % "javax.servlet-api" % "3.0.1" % "test",
      "com.oracle" % "ojdbc6" % "11.2.0.2.0" % "test",
      "org.scalatest" %% "scalatest" % "2.2.4" % "test")

