import sbt._

object Dependencies {

  val AwsSDKVersion = "1.11.240"

  // https://mvnrepository.com/artifact/org.bouncycastle/bcpkix-jdk15on
  val bcpkix = "org.bouncycastle" % "bcpkix-jdk15on" % "1.56"

  // https://mvnrepository.com/artifact/org.specs2/specs2_2.11
  val specs2 = "org.specs2" % "specs2_2.11" % "3.7" % Test

  val awsLambdaJavaCore = "com.amazonaws" % "aws-lambda-java-core" % "1.1.0"
  val awsJavaSdkS3 = "com.amazonaws" % "aws-java-sdk-s3" % AwsSDKVersion

  val sprayJson = "io.spray" %% "spray-json" % "1.3.3"
}