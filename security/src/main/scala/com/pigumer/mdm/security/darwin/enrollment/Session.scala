package com.pigumer.mdm.security.darwin.enrollment

import java.nio.charset.StandardCharsets

import com.pigumer.mdm.security.oauth1.DefaultOauthService
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import spray.json._

case class AuthSessionToken(authSessionToken: String)

object SessionJsonProtocol extends DefaultJsonProtocol {
  implicit val sessionJsonFormat = jsonFormat(AuthSessionToken, "auth_session_token")
}

trait Session {
  def session(url: String,
              consumerKey: String,
              consumerSecret: String,
              accessToken: String,
              accessSecret: String)
             (implicit client: HttpClient): Either[HttpResponse, AuthSessionToken] = {
    val oauthHeader = DefaultOauthService.createOauthenticatedHeader(
      url,
      "GET",
      consumerKey,
      consumerSecret,
      accessToken,
      accessSecret
    )
    val request = new HttpGet(url)
    request.setHeader("Authorization", oauthHeader)
    val res = client.execute(request)
    res.getStatusLine.getStatusCode match {
      case 200 ⇒
        val body = res.getEntity.getContent
        try {
          import SessionJsonProtocol._
          Right(
            JsonParser(
              new String(
                Stream.continually(body.read).takeWhile(_ != -1).map(_.toByte).toArray,
                StandardCharsets.UTF_8)
            ).convertTo[AuthSessionToken]
          )
        } finally {
          body.close()
        }
      case _ ⇒
        Left(res)
    }
  }
}