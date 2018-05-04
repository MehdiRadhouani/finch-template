name := "Finch-Skeleton"

version := "0.1"

scalaVersion := "2.12.6"

libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.github.finagle" %% "finch-core" % "0.19.0",
  "com.github.finagle" %% "finch-circe" % "0.19.0",
  "io.circe" %% "circe-generic" % "0.9.0",
  "org.reactivemongo" %% "reactivemongo" % "0.13.0",
  "org.typelevel" %% "cats-core" % "1.0.1",
  "io.scalaland" %% "chimney" % "0.1.10",
  "com.typesafe" % "config" % "1.3.3",
  "org.scalatest" %% "scalatest" % "3.0.5" % Test,
  "com.github.finagle" %% "finch-test" % "0.19.0" % Test,
  "com.github.simplyscala" %% "scalatest-embedmongo" % "0.2.4" % Test,
  "org.mockito" % "mockito-core" % "2.18.3" % Test
)

coverageEnabled := true
coverageMinimum := 90
coverageHighlighting := true

commands += Command.command("validate") { state => "clean" :: "coverage" :: "test" :: "coverageReport" :: state }
commands += Command.command("fmt") { state => "scalafmt" :: state }