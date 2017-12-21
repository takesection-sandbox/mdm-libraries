package com.pigumer.mdm.cloud.aws.lambda

import java.io.{IOException, InputStream, OutputStream}
import java.nio.charset.StandardCharsets
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.time.{Instant, Period, ZoneId, ZonedDateTime}
import java.util.Date

import com.amazonaws.services.lambda.runtime.{Context, RequestStreamHandler}
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.pigumer.mdm.security.{KeyPair, SelfSignedCertificate}
import org.bouncycastle.asn1.x500.X500NameBuilder
import org.bouncycastle.asn1.x500.style.BCStyle
import spray.json._

import scala.util.Try

case class KeyPairRequestJson(privateKeyFile: String, certificateFile: String)

object JsonProtocol extends DefaultJsonProtocol {
  implicit val keyPairRequestJsonFormat = jsonFormat(KeyPairRequestJson, "private_key_file", "certificate_file")
}

class KeyPairGenerator extends RequestStreamHandler {

  lazy val bucketRegion = sys.env.getOrElse("BUCKET_REGION", "AWS_REGION")
  lazy val bucketName = sys.env("BUCKET_NAME")
  lazy val algorithm = sys.env.getOrElse("ALGORITHM", "RSA")
  lazy val keySize = sys.env.getOrElse("KEY_SIZE", "2048").toInt
  lazy val country = sys.env.getOrElse("C", "JP")
  lazy val organization = sys.env("ORGANIZATION")
  lazy val commonName = sys.env("COMMON_NAME")

  lazy val s3 = AmazonS3ClientBuilder.standard.withRegion(bucketRegion).build

  @throws(classOf[java.io.IOException])
  override def handleRequest(input: InputStream, output: OutputStream, context: Context) =
    (for {
      request ← Try {
        import JsonProtocol._
        JsonParser(new String(Stream.continually(input.read).takeWhile(_ != -1).map(_.toByte).toArray,
          StandardCharsets.UTF_8)).convertTo[KeyPairRequestJson]
      }
      keyPair ← Try(KeyPair.genKeyPair(algorithm, keySize))
      _ ← Try(s3.putObject(bucketName, request.privateKeyFile, keyPair.privateKeyToPEMString))
      _ ← Try {
        val now = ZonedDateTime.now(ZoneId.of("Etc/UTC"))
        val notBefore = new Date(now.toInstant.toEpochMilli)
        val notAfter = new Date(now.plus(1, ChronoUnit.YEARS).toInstant.toEpochMilli)
        val serial = now.format(DateTimeFormatter.ofPattern("uuuuMMddHHmmssSSS"))
        val builder: X500NameBuilder = new X500NameBuilder()
        builder.addRDN(BCStyle.C, country)
        builder.addRDN(BCStyle.O, organization)
        builder.addRDN(BCStyle.CN, commonName)
        val subject = builder.build()
        val certificate = SelfSignedCertificate.build(
          serial = BigInt(serial),
          notBefore = notBefore,
          notAfter = notAfter,
          subject = subject,
          keyPair = keyPair
        )
        s3.putObject(bucketName, request.certificateFile, certificate.toPEMString)
      }
    } yield ()).fold(
      t => throw new IOException(t),
      _ => ()
    )
}
