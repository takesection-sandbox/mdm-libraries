package com.pigumer.mdm.security.darwin.enrollment

import spray.json._

case class AccountUrlLimit(default: Int, maximum: Int)
case class AccountUrl(uri: String, httpMethod: String, limit: Option[AccountUrlLimit])
case class Account(serverName: String,
                   serverUuid: String,
                   adminId: String,
                   orgName: String,
                   orgEmail: String,
                   orgPhone: String,
                   orgAddress: String,
                   urls: Seq[AccountUrl],
                   orgType: String,
                   orgVersion: String,
                   orgId: String,
                   orgIdHash: String)

object AccountJsonProtocol extends DefaultJsonProtocol {
  implicit val accountUrlLimitJsonFormat = jsonFormat(
    AccountUrlLimit,
    "default",
    "maximum")
  implicit val accountUrlJsonFormat = jsonFormat(
    AccountUrl,
    "uri",
    "http_method",
    "limit")
  implicit val accountJsonFormat = jsonFormat(
    Account,
    "server_name",
    "server_uuid",
    "admin_id",
    "org_name",
    "org_email",
    "org_phone",
    "org_address",
    "urls",
    "org_type",
    "org_version",
    "org_id",
    "org_id_hash"
  )
}