name := """SocialNetwork"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.8"

//libraryDependencies += guice
//libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test

libraryDependencies ++= Seq(
  guice,
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % Test,
  jdbc,
  "mysql" % "mysql-connector-java" % "8.0.28",
  "com.typesafe.play" %% "play-slick" % "5.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "5.0.0" % Test,

  "com.typesafe.play" %% "play-json-joda" % "2.9.2"
)

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "3.3.3",
  "com.github.tototoshi" %% "slick-joda-mapper" % "2.5.0",
  "joda-time" % "joda-time" % "2.10.14",
  "org.joda" % "joda-convert" % "2.2.2"
)



// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"
// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"
