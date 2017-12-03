package com.pigumer.mdm.security

import java.io.StringWriter
import java.security.{KeyPairGenerator, KeyPair => JSKeyPair}

import org.bouncycastle.openssl.jcajce.JcaMiscPEMGenerator
import org.bouncycastle.util.io.pem.PemWriter

class KeyPair(keyPair: JSKeyPair) {

  val publicKey = keyPair.getPublic
  val privateKey = keyPair.getPrivate

  def privateKeyToPEMString = {
    val writer = new StringWriter
    try {
      val pemWriter = new PemWriter(writer)
      try {
        val pemObject = new JcaMiscPEMGenerator(keyPair.getPrivate)
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

  def publicKeyToPEMString = {
    val writer = new StringWriter
    try {
      val pemWriter = new PemWriter(writer)
      try {
        val pemObject = new JcaMiscPEMGenerator(keyPair.getPublic)
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

object KeyPair {
  def genKeyPair(algorithm: String = "RSA", keySize: Int = 2048): KeyPair =
    new KeyPair(
      {
        val generator = KeyPairGenerator.getInstance(algorithm)
        generator.initialize(keySize)
        generator.genKeyPair()
      }
    )
}