scalaVersion := "2.10.3"

val scalazVersion = "7.0.5"

val slickVersion = "2.0.0"

libraryDependencies ++= Seq(
  "org.scalaz" %% "scalaz-core" % scalazVersion,
  "org.scalaz" %% "scalaz-effect" % scalazVersion,
  "org.scalaz" %% "scalaz-typelevel" % scalazVersion,
  "org.scalaz" %% "scalaz-scalacheck-binding" % scalazVersion % "test",
  "org.specs2" %% "specs2" % "2.3.8" % "test",
  "org.typelevel" %% "scalaz-contrib-210" % "0.1.5",
  "com.typesafe.slick" %% "slick" % slickVersion,
  "mysql" % "mysql-connector-java" % "5.1.26",
  "com.jsuereth" %% "scala-arm" % "1.3"
)

scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature", "-language:higherKinds", "-language:implicitConversions")

initialCommands in console := "import scalaz._, Scalaz._"

initialCommands in console in Test := "import scalaz._, Scalaz._, scalacheck.ScalazProperties._, scalacheck.ScalazArbitrary._,scalacheck.ScalaCheckBinding._"

