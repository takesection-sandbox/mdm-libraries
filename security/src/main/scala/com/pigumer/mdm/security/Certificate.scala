package com.pigumer.mdm.security

import java.io.{Reader, StringWriter}
import java.util.Date

import org.bouncycastle.asn1.oiw.OIWObjectIdentifiers
import org.bouncycastle.asn1.x500.X500Name
import org.bouncycastle.asn1.x509.{AlgorithmIdentifier, SubjectPublicKeyInfo}
import org.bouncycastle.cert.{X509CertificateHolder, X509v3CertificateBuilder}
import org.bouncycastle.crypto.util.PrivateKeyFactory
import org.bouncycastle.openssl.{PEMKeyPair, PEMParser}
import org.bouncycastle.openssl.jcajce.{JcaMiscPEMGenerator, JcaPEMKeyConverter}
import org.bouncycastle.operator.bc.BcRSAContentSignerBuilder
import org.bouncycastle.util.io.pem.PemWriter

trait X509Certificate {
  val x509Certificate: X509CertificateHolder
  def toPEMString: String = {
    val writer = new StringWriter
    try {
      val pemWriter = new PemWriter(writer)
      try {
        val pemObject = new JcaMiscPEMGenerator(x509Certificate)
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

object X509Certificate {
  def build(issuer: X500Name,
            serial: BigInt,
            notBefore: Date,
            notAfter: Date,
            subject: X500Name,
            keyPair: KeyPair): X509Certificate = {
    val builder = new X509v3CertificateBuilder(issuer,
      serial.bigInteger,
      notBefore,
      notAfter,
      subject,
      SubjectPublicKeyInfo.getInstance(keyPair.publicKey.getEncoded)
    )
    val sigAlgId = new AlgorithmIdentifier(OIWObjectIdentifiers.sha1WithRSA)
    val digAlgId = new AlgorithmIdentifier(OIWObjectIdentifiers.idSHA1)
    val privateKeyParam = PrivateKeyFactory.createKey(keyPair.privateKey.getEncoded)
    val contentSigner = new BcRSAContentSignerBuilder(sigAlgId, digAlgId).build(privateKeyParam)
    new X509Certificate {
      override val x509Certificate = builder.build(contentSigner)
    }
  }
  def parse(reader: Reader): X509Certificate = {
    val parser = new PEMParser(reader)
    val obj = parser.readObject
    new X509Certificate {
      override val x509Certificate = obj.asInstanceOf[X509CertificateHolder]
    }
  }
}
