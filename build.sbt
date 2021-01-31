name := "fluent-style-sbt-core"

version := "0.0.1"

sbtPlugin := true

libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.2" % Test
libraryDependencies ++= Seq("com.whisk" %% "docker-testkit-scalatest" % "0.9.9",
  "com.whisk" %% "docker-testkit-impl-docker-java" % "0.9.9")

val testDirs = List(/*"core-spec"*/ "docker-spec")
unmanagedSourceDirectories in Test ++= testDirs.map(dir => (baseDirectory in Test).value / dir)

val prjctTestDirs = List("core", "plugin", "publishing", "docker", "paths")
unmanagedSourceDirectories in Compile ++= prjctTestDirs.map(dir => (baseDirectory in Compile).value / dir)

organization := "org.logicovercode"

val cto = Developer("cto", "cto", "cto@logicovercode.org", url("https://github.com/logicovercode"))
developers := List(cto)

homepage := Some(url("https://github.com/logicovercode/FluentStyleSbtCorePlugin"))
scmInfo := Some(ScmInfo(url("https://github.com/logicovercode/FluentStyleSbtCorePlugin"), "git@github.com:logicovercode/FluentStyleSbtCorePlugin.git"))

licenses += ("MIT", url("https://opensource.org/licenses/MIT"))

//publishing related settings

crossPaths := false

publishMavenStyle := true

publishTo := Some(Opts.resolver.sonatypeStaging)

//below is not yet working as expected (exploring ...)
publishConfiguration := publishConfiguration.value.withOverwrite(true)

