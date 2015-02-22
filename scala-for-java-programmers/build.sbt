name := "sbtTest"

version := "1.0"

scalaVersion := "2.11.5"

resolvers ++= Seq("snapshots", "releases").map(Resolver.sonatypeRepo)

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2-core" % "2.4.15" % "test",
  "org.scalatest" % "scalatest_2.11" % "2.2.1" % "test",
  "com.github.nscala-time" %% "nscala-time" % "1.8.0"
)
