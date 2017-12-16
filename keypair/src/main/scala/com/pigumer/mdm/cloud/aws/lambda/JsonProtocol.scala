package com.pigumer.mdm.cloud.aws.lambda

import spray.json.DefaultJsonProtocol

case class KeyPairRequestJson(privateKeyFile: String, certificateFile: String)

object JsonProtocol extends DefaultJsonProtocol {
  implicit val keyPairRequestJsonFormat = jsonFormat(KeyPairRequestJson, "private_key_file", "certificate_file")
}