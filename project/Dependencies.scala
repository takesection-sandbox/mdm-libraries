import sbt._

object Dependencies {

  val AwsSDKVersion = "1.11.240"

  // https://mvnrepository.com/artifact/javax.mail/javax.mail-api
  val javaxmail = "javax.mail" % "mail" % "1.4.7"

  // https://mvnrepository.com/artifact/org.bouncycastle/bcpkix-jdk15on
  val bcpkix = "org.bouncycastle" % "bcpkix-jdk15on" % "1.58"
  // https://mvnrepository.com/artifact/org.bouncycastle/bcmail-jdk15on
  val bcmail = "org.bouncycastle" % "bcmail-jdk15on" % "1.58"
  // https://mvnrepository.com/artifact/org.bouncycastle/bcpg-jdk15on
  val bcpg   = "org.bouncycastle" % "bcpg-jdk15on" % "1.58"

  val specs2 = "org.specs2" %% "specs2-core" % "3.8.6" % Test

  val awsLambdaJavaCore = "com.amazonaws" % "aws-lambda-java-core" % "1.1.0"
  val awsJavaSdkS3 = "com.amazonaws" % "aws-java-sdk-s3" % AwsSDKVersion

  val sprayJson = "io.spray" %% "spray-json" % "1.3.3"

  // https://mvnrepository.com/artifact/org.scala-lang.modules/scala-xml
  val scalaXml = "org.scala-lang.modules" %% "scala-xml" % "1.0.6"

  // https://mvnrepository.com/artifact/org.apache.httpcomponents/httpclient
  val httpClient = "org.apache.httpcomponents" % "httpclient" % "4.5.4"
}