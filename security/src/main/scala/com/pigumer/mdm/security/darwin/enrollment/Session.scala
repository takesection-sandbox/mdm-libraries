package com.pigumer.mdm.security.darwin.enrollment

import spray.json._

case class Session(authSessionToken: String)

object SessionJsonProtocol extends DefaultJsonProtocol {
  implicit val sessionJsonFormat = jsonFormat(Session, "auth_session_token")
}
