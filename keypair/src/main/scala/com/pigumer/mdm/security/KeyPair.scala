package com.pigumer.mdm.security

import java.io.StringWriter
import java.security.{KeyPair ⇒ JSKeyPair, KeyPairGenerator}

import org.bouncycastle.openssl.jcajce.JcaMiscPEMGenerator
import org.bouncycastle.util.io.pem.PemWriter

class KeyPair(algorithm: String = "RSA", keySize: Int = 2048) {

  val keyPair: JSKeyPair = {
    val generator = KeyPairGenerator.getInstance(algorithm)
    generator.initialize(keySize)
    generator.genKeyPair()
  }

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
