name := "david/ActorThreadingTest"

version := "1.0"

scalaVersion := "2.11.6"

val akkaStackVersion = "2.3.9"

libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.0.13",
  "com.typesafe.akka" %% "akka-actor" % akkaStackVersion,
  "com.typesafe.akka" %% "akka-contrib" % akkaStackVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaStackVersion
)
