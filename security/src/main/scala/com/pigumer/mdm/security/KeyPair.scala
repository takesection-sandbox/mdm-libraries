package com.pigumer.mdm.security

import java.io.{Reader, StringWriter}
import java.security.{KeyPairGenerator, PublicKey ⇒ JSPublicKey, KeyPair ⇒ JSKeyPair, PrivateKey ⇒ JSPrivateKey}

import org.bouncycastle.openssl.{PEMKeyPair, PEMParser}
import org.bouncycastle.openssl.jcajce.{JcaMiscPEMGenerator, JcaPEMKeyConverter}
import org.bouncycastle.util.io.pem.PemWriter

trait PrivateKey {

  val privateKey: JSPrivateKey

  def privateKeyToPEMString = {
    val writer = new StringWriter
    try {
      val pemWriter = new PemWriter(writer)
      try {
        val pemObject = new JcaMiscPEMGenerator(privateKey)
        pemWriter.writeObject(pemObject)
      }
      finally {
        pemWriter.close
      }
      writer.toString
    }
    finally {
      writer.close
    }
  }
}

trait PublicKey {
  val publicKey: JSPublicKey
}

trait KeyPair extends PublicKey with PrivateKey

object KeyPair {

  def apply(keyPair: JSKeyPair): KeyPair =
    new KeyPair {
      override val publicKey = keyPair.getPublic
      override val privateKey = keyPair.getPrivate
    }

  def genKeyPair(algorithm: String = "RSA", keySize: Int = 2048): KeyPair = {
    val generator = KeyPairGenerator.getInstance(algorithm)
    generator.initialize(keySize)
    val keyPair = generator.genKeyPair()
    new KeyPair {
      override val publicKey = keyPair.getPublic
      override val privateKey = keyPair.getPrivate
    }
  }
}

object PrivateKey {

  def parse(reader: Reader): PrivateKey = {
    val parser = new PEMParser(reader)
    val obj = parser.readObject.asInstanceOf[PEMKeyPair]
    new PrivateKey {
      override val privateKey = new JcaPEMKeyConverter().getPrivateKey(obj.getPrivateKeyInfo)
    }
  }
}