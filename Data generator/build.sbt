name := "data-generator"

scalaVersion := "2.11.7"

version := "1.0"

scalaVersion := "2.11.6"

resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  "com.typesafe.akka" % "akka-slf4j_2.11" % "2.3.11" exclude("org.slf4j", "slf4j-api"),
  "ch.qos.logback" % "logback-classic" % "1.1.3" exclude("org.slf4j", "slf4j-api"),
  "com.typesafe.akka" %% "akka-testkit" % "2.3.7" % "test",
  "org.scalatest" % "scalatest_2.11" % "2.2.1" % "test",
  "com.typesafe.slick" %% "slick" % "3.0.0" exclude("org.slf4j", "slf4j-api"),
  "org.postgresql" % "postgresql" % "9.3-1100-jdbc4",
  "com.github.tototoshi" %% "slick-joda-mapper" % "2.0.0",
  "joda-time" % "joda-time" % "2.4",
  "it.justwrote" %% "scala-faker" % "0.3",
  "com.zaxxer" % "HikariCP-java6" % "2.3.2",
  "org.slf4j" % "slf4j-api" % "1.7.7",
  "com.h2database" % "h2" % "1.4.187",
  "com.typesafe.slick" %% "slick-codegen" % "3.0.3"
)
