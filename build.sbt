import Dependencies._

val commonSettings = Seq(
  version := "0.0.1-SNAPSHOT",
  scalaVersion := "2.12.4",
  organization := "com.pigumer.mdm"
)

lazy val keyPair = (project in file("keypair"))
  .settings(commonSettings: _*)
  .settings(
    name := "keypair",
    libraryDependencies ++= Seq(
      bcpkix,
      awsLambdaJavaCore,
      awsJavaSdkS3,
      sprayJson
    )
  )
