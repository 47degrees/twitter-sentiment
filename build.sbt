lazy val caseClassyV = "0.4.0"

lazy val commonSettings: Seq[Def.Setting[_]] = Seq(
  libraryDependencies ++= Seq(
    "org.typelevel" %% "cats-core" % "1.0.1",
    "com.google.cloud" % "google-cloud-pubsub" % "0.32.0-beta",
    "com.47deg" %% "classy-core" % caseClassyV,
    "com.47deg" %% "classy-config-typesafe" % caseClassyV,
    "com.47deg" %% "classy-generic" % caseClassyV,
    "com.whisk" %% "docker-testkit-scalatest" % "0.9.6" % IntegrationTest,
    "com.whisk" %% "docker-testkit-impl-spotify" % "0.9.6" % IntegrationTest,
  )
)

lazy val reader = (project in file("reader"))
  .settings(
    inThisBuild(List(
      organization := "47deg",
      scalaVersion := "2.12.4"
    )),
    name := "reader"
  )
  .settings(moduleName := "reader")
  .settings(commonSettings)
  .settings(libraryDependencies ++= Seq(
    "com.danielasfregola" %% "twitter4s" % "5.3"
  ))

lazy val storer = (project in file("storer"))
  .configs(IntegrationTest)
  .settings(
    inThisBuild(List(
      organization := "47deg",
      scalaVersion := "2.12.4"
    )),
    name := "storer"
  )
  .settings(commonSettings,
    Defaults.itSettings)
  .settings(libraryDependencies ++= Seq(
    "io.getquill" %% "quill-cassandra" % "2.3.2")
  )
