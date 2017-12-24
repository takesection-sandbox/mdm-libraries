package com.pigumer.mdm.security.oauth1

import java.net.URLEncoder
import java.nio.charset.StandardCharsets.UTF_8
import java.security.SecureRandom
import java.time.Instant
import java.util.Base64
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

import scala.util.Random

object OauthParams {
  val Realm = "realm"
  val OauthConsumerKey = "oauth_consumer_key"
  val OauthToken = "oauth_token"
  val OauthSignatureMethod = "oauth_signature_method"
  val OauthSignature = "oauth_signature"
  val OauthTimestamp = "oauth_timestamp"
  val OauthNonce = "oauth_nonce"
  val OauthVersion = "oauth_version"
}

trait OauthService {
  def createOauthenticatedHeader(urlWithoutParams: String,
                                 method: String,
                                 consumerKey: String,
                                 consumerSecret: String,
                                 accessToken: String,
                                 accessSecret: String): String
}

object DefaultOauthService extends OauthService {
  private val random = new Random(new SecureRandom())
  private val HmacSha1Algorithm = "HmacSHA1"
  private val urlEncodePattern = """\+|\*|%7E""".r

  private def urlEncode(s: String) = urlEncodePattern.replaceAllIn(
    URLEncoder.encode(s, "UTF-8"),
    m => m.group(0) match {
      case "+" => "%20"
      case "*" => "%2A"
      case "%7E" => "~"
    })

  private def createAuthorizationHeader(oauthParamsList: List[(String, String)]) =
    "OAuth " + oauthParamsList.map(t => s"""${urlEncode(t._1)}="${urlEncode(t._2)}"""").mkString(",")

  private def sign(urlWithoutParams: String,
                   consumerSecret: String,
                   accessSecret: String,
                   oauthParams: List[(String, String)]) = {
    val key = List(consumerSecret, accessSecret).map(urlEncode).mkString("&")
    val baseString = oauthParams.map(t ⇒ s"${t._1}=${t._2}").mkString("&")
    val base = List("GET", urlEncode(urlWithoutParams), urlEncode(baseString)).mkString("&")
    val keySpec = new SecretKeySpec(key.getBytes(UTF_8), HmacSha1Algorithm)
    val mac = Mac.getInstance(HmacSha1Algorithm)
    mac.init(keySpec)
    val bytesToSign = base.getBytes(UTF_8)
    val digest = mac.doFinal(bytesToSign)
    Base64.getEncoder.encodeToString(digest)
  }

  def createOauthenticatedHeader(urlWithoutParams: String,
                                 method: String,
                                 consumerKey: String,
                                 consumerSecret: String,
                                 accessToken: String,
                                 accessSecret: String): String = {
    val oauthRealm = "ADM"
    val oauthSignatureMethod = "HMAC-SHA1"
    val oauthTimestamp = Instant.now.getEpochSecond.toString
    val oauthNonce = random.alphanumeric.take(8).mkString
    val oauthVersion = "1.0"

    val oauthSignature = sign(
      urlWithoutParams,
      consumerSecret,
      accessSecret,
      List(
        OauthParams.OauthConsumerKey -> consumerKey,
        OauthParams.OauthNonce -> oauthNonce,
        OauthParams.OauthSignatureMethod -> oauthSignatureMethod,
        OauthParams.OauthTimestamp -> oauthTimestamp,
        OauthParams.OauthToken -> accessToken,
        OauthParams.OauthVersion -> oauthVersion
      )
    )
    createAuthorizationHeader(
      List(
        OauthParams.Realm -> oauthRealm,
        OauthParams.OauthConsumerKey -> consumerKey,
        OauthParams.OauthToken -> accessToken,
        OauthParams.OauthSignatureMethod -> oauthSignatureMethod,
        OauthParams.OauthSignature -> oauthSignature,
        OauthParams.OauthTimestamp -> oauthTimestamp,
        OauthParams.OauthNonce -> oauthNonce,
        OauthParams.OauthVersion -> oauthVersion
      )
    )
  }
}