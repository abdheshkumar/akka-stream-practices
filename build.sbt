name := "akka-stream-practices"

version := "1.0"

scalaVersion := "2.12.3"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % "2.5.8",
  "com.typesafe.akka" %% "akka-cluster" % "2.5.8",
  "com.typesafe.akka" %% "akka-cluster-sharding" % "2.5.8",
  "com.typesafe.akka" %% "akka-http" % "10.0.11",
  "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.8" % Test
)