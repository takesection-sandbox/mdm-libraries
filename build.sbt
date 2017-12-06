import Dependencies._

val commonSettings = Seq(
  version := "0.0.1-SNAPSHOT",
  scalaVersion := "2.12.4",
  organization := "com.pigumer.mdm"
)

lazy val security = (project in file("security"))
  .settings(commonSettings: _*)
  .settings(
    name := "security",
    libraryDependencies ++= Seq(
      bcpkix,
      bcmail,
      javaxmail,
      scalaXml,
      specs2
    )
  )

lazy val keyPair = (project in file("keypair"))
  .dependsOn(security)
  .settings(commonSettings: _*)
  .settings(
    name := "keypair",
    libraryDependencies ++= Seq(
      awsLambdaJavaCore,
      awsJavaSdkS3,
      sprayJson
    )
  )

lazy val certificateRequest = (project in file("certificaterequest"))
  .dependsOn(security)
  .settings(commonSettings: _*)
  .settings(
    name := "certificaterequest",
    libraryDependencies ++= Seq(
      awsLambdaJavaCore,
      awsJavaSdkS3,
      sprayJson
    )
  )