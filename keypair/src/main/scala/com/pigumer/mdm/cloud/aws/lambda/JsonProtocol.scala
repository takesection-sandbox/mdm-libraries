package com.pigumer.mdm.cloud.aws.lambda

import spray.json.DefaultJsonProtocol

case class KeyPairRequestJson(privateKeyFile: String, publicKeyFile: String)

object JsonProtocol extends DefaultJsonProtocol {

  implicit val keyPairRequestJsonFormat = jsonFormat(KeyPairRequestJson, "private_key_file", "public_key_file")
}