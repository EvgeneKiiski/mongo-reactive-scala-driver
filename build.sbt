
resolvers += "Artima Maven Repository" at "http://repo.artima.com/releases"

logBuffered in Test := false

autoCompilerPlugins := true

addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.0")

lazy val compilerOptions = Seq(
  "-encoding", "utf8",
  "-Xfatal-warnings",
  "-deprecation",
  "-unchecked",
  "-opt:l:inline",
  "-opt-inline-from:pbson.**",
  "-Ypartial-unification",
  "-language:implicitConversions",
  "-language:higherKinds",
  "-language:existentials",
  "-language:postfixOps",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Xfuture",
  "-Ywarn-unused-import",
)

lazy val commonSettings = Seq(
  organization := "ru.twistedlogic",
  organizationName := "Twistedlogic",
  organizationHomepage := Some(new URL("http://twistedlogic.ru/")),
  version := "0.0.1",
  crossScalaVersions := Seq("2.11.12", "2.12.9", "2.13.0"),
  licenses := Seq("MIT License" -> url("http://opensource.org/licenses/mit-license.php")),
  scalacOptions ++= {
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, minor)) if minor == 11 =>
        compilerOptions.flatMap {
          case "-opt:l:inline" => Nil
          case other => Seq(other)
        }
      case Some((2, minor)) if minor == 12 => compilerOptions
      case Some((2, minor)) if minor >= 13 =>
        compilerOptions.flatMap {
          case "-Ywarn-unused-import" => Seq("-Ywarn-unused:imports")
          case "-Ypartial-unification" => Nil
          case "-Xfuture" => Nil
          case other => Seq(other)
        }
      case _ => Nil
    }
  }
)


lazy val root = (project in file("."))
  .settings(
    commonSettings,
    name := "mongo-reactive-scala-driver",
    homepage := Some(url("https://evgenekiiski.github.io/mongo-reactive-scala-driver/")),
    description := "Mongo reactive scala driver",
    libraryDependencies ++= Seq(
      "org.mongodb" % "mongodb-driver-reactivestreams" % "1.12.0",
      "org.typelevel" %% "cats-effect" % "2.0.0-RC2",
      "ch.qos.logback" % "logback-classic" % "1.2.3" % Test,
      "ch.qos.logback" % "logback-core" % "1.2.3" % Test,
      "net.logstash.logback" % "logstash-logback-encoder" % "5.1" % Test,
      "org.scalactic" %% "scalactic" % "3.0.8" % Test,
      "org.scalatest" %% "scalatest" % "3.0.8" % Test,
      "org.scalacheck" %% "scalacheck" % "1.14.0" % Test,
      "de.flapdoodle.embed" % "de.flapdoodle.embed.mongo" % "2.2.0" % Test
    ),
    scmInfo := Some(
      ScmInfo(
        url("https://github.com/EvgeneKiiski/mongo-reactive-scala-driver"),
        "scm:git:git@github.com:EvgeneKiiski/mongo-reactive-scala-driver.git"
      )
    ),
    publishTo := Some(Resolver.file("file", new File("repository")))
  )