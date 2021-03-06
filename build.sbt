name := "kafka-streams-scala-playground"

version := "0.1"

organization := "com.github.medhosni"

scalaVersion := "2.12.6"

lazy val json4SVer = "3.6.0-M4"  // todo update to latest when next ver comes out (need at least this for JavaTimeSerializers)
lazy val kafkaVer = "2.4.0"
lazy val scalatestVer = "3.0.5"

// Always fork the jvm (test and run)
fork := true

// Allow CTRL-C to cancel running tasks without exiting SBT CLI.
cancelable in Global := true
resolvers += Resolver.mavenLocal
resolvers += Resolver.DefaultMavenRepository
libraryDependencies ++= Seq(

  // Kafka streams
  "org.apache.kafka" % "kafka-streams" % kafkaVer,
  // scala wrapper for kafka streams DSL:
//  "org.apache.kafka" % "kafka-streams-scala" % kafkaVer,
  "com.lightbend" %% "kafka-streams-scala" % "0.2.1",


  // For JSON parsing (see https://github.com/json4s/json4s)
  "org.json4s" %%  "json4s-jackson" % json4SVer,
  "org.json4s" %%  "json4s-ext" % json4SVer,

  // config
  "com.github.pureconfig" %% "pureconfig" % "0.9.1",

  // logging
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
  
  // For testing:
  "org.apache.kafka" % "kafka-streams-test-utils" % kafkaVer % "test",
  "com.madewithtea" %% "mockedstreams" % "1.6.0" % "test",
  "org.scalatest" %% "scalatest" % scalatestVer % "test"
  //"org.scalactic" %% "scalactic" % scalatestVer % "test",
)

// Print full stack traces in tests:
testOptions in Test += Tests.Argument("-oF")

// Assembly stuff (for fat jar)
mainClass in assembly := Some("Main")
assemblyJarName in assembly := "kafka-streams-scala-playground.jar"

// Some stuff to import in the console
initialCommands in console := """

  // project stuff
  import com.github.medhosni.kafkastreamsscalaplayground._
"""

