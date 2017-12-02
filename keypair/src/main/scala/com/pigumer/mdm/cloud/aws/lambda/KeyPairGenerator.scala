package com.pigumer.mdm.cloud.aws.lambda

import java.io.{InputStream, OutputStream}
import java.nio.charset.StandardCharsets

import com.amazonaws.services.lambda.runtime.{Context, RequestStreamHandler}
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.pigumer.mdm.keypair.KeyPair
import spray.json._

class KeyPairGenerator extends RequestStreamHandler {

  lazy val bucketRegion = sys.env.getOrElse("BUCKET_REGION", "AWS_REGION")
  lazy val bucketName = sys.env("BUCKET_NAME")
  lazy val algorithm = sys.env.getOrElse("ALGORITHM", "RSA")
  lazy val keySize = sys.env.getOrElse("KEY_SIZE", "2048").toInt

  lazy val s3 = AmazonS3ClientBuilder.standard.withRegion(bucketRegion).build

  @throws(classOf[java.io.IOException])
  override def handleRequest(input: InputStream, output: OutputStream, context: Context) = {
    import JsonProtocol._

    val request = JsonParser(new String(Stream.continually(input.read).takeWhile(_ != -1).map(_.toByte).toArray,
      StandardCharsets.UTF_8)).convertTo[KeyPairRequestJson]
    val keyPair = new KeyPair(algorithm, keySize)
    s3.putObject(bucketName, request.privateKeyFile, keyPair.privateKeyToPEMString)
    s3.putObject(bucketName, request.publicKeyFile, keyPair.publicKeyToPEMString)
  }
}
