import org.typelevel.sbt.tpolecat.VerboseMode

ThisBuild / organization := "io.github.sergeiionin"
ThisBuild / scalaVersion := "3.4.0"

// This disables fatal-warnings for local development. To enable it in CI set the `SBT_TPOLECAT_CI` environment variable in your pipeline.
// See https://github.com/typelevel/sbt-tpolecat/?tab=readme-ov-file#modes
ThisBuild / tpolecatDefaultOptionsMode := VerboseMode

lazy val root = (project in file(".")).settings(
  name := "websockets-tapir-example",
  libraryDependencies ++=
    Dependencies.catsDependencies ++
    Dependencies.circeDependencies ++
    Dependencies.tapirDependencies ++
    Dependencies.http4s ++
    Dependencies.miscDependencies
)
