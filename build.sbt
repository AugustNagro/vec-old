lazy val root = project
  .in(file("."))
  .settings(
    name := "vec",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := "3.3.0",
    libraryDependencies += "org.scalameta" %% "munit" % "0.7.29" % Test
  )
