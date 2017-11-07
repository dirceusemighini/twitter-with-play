scalaVersion := "2.11.6"

name := "twitter-with-play"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
//  jdbc,
//  anorm,
//  cache,
  guice,
  "org.twitter4j" % "twitter4j-stream" % "3.0.5"
  , ws
)
routesGenerator := InjectedRoutesGenerator

lazy val root = (project in file(".")).enablePlugins(PlayScala)

