name := "twitter-with-play"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  "org.twitter4j" % "twitter4j-stream" % "3.0.5"
)     

play.Project.playScalaSettings
