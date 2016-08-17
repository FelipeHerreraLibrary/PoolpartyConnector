name := "PoolpartyConnector"

organization := "org.iadb"

version := "0.0.3-SNAPSHOT"

scalaVersion := "2.11.8"


fork := true

parallelExecution in Test := false


resolvers += "spray repo" at "http://repo.spray.io"
resolvers += "Local Maven" at Path.userHome.asFile.toURI.toURL + ".m2/repository"

scalacOptions in ThisBuild ++= Seq("-language:postfixOps", "-feature")
scalacOptions ++= Seq("-feature")



libraryDependencies ++= {

      val akkaVersion = "2.4.8"

      Seq(  "io.spray" %% "spray-client"  % "1.3.3",
            "io.spray" %% "spray-json"    % "1.3.2",
            "io.spray" %% "spray-caching" % "1.3.3",

            "com.typesafe.akka" %% "akka-actor"        % akkaVersion,
            "com.typesafe.akka" %%  "akka-persistence" % akkaVersion,
            "com.typesafe.akka" %% "akka-testkit"      % akkaVersion % "test",
            "com.typesafe.akka" %% "akka-slf4j"        % akkaVersion,
            //"com.typesafe.akka" %% "akka-http-core" % akkaVersion,
            //"com.typesafe.akka" %% "akka-http-experimental" % akkaVersion,
            //"com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaVersion,

            "org.iq80.leveldb"            % "leveldb"          % "0.7",
            "org.fusesource.leveldbjni"   % "leveldbjni-all"   % "1.8",

            //"ch.qos.logback"    %  "logback-classic" % "1.1.3",

             "org.slf4j" % "slf4j-log4j12" % "1.7.12",


            "org.w3" %% "banana-sesame" % "0.8.2-SNAPSHOT",
            "org.scala-lang.modules" % "scala-java8-compat_2.11" % "0.7.0",
            "org.dspace" % "dspace-api" % "1.8.2" exclude("org.slf4j", "slf4j-api") exclude("log4j", "log4j"),

            "com.softwaremill.macwire" %% "macros" % "2.2.3" % "provided",
            "com.softwaremill.macwire" %% "util" % "2.2.3",


            "com.typesafe" % "config" % "1.3.0",

            "org.scalamock" %% "scalamock-scalatest-support" % "3.2" % "test",
            "org.scalatest" %% "scalatest" % "2.2.6" % "test"
      )
}


//dependencyOverrides += "xml-apis" % "xml-apis" % "1.3.02"

//dependencyOverrides += "xerces" % "xercesImpl" % "2.8.1"



dependencyOverrides += "org.scala-lang" % "scala-reflect" % "2.11.8"

dependencyOverrides += "org.scala-lang.modules" %% "scala-xml" % "1.0.4"

dependencyOverrides += "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4"
