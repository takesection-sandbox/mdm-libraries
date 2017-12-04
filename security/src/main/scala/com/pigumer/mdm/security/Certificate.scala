package com.pigumer.mdm.security

import java.io.StringWriter
import java.util.Date

import org.bouncycastle.asn1.oiw.OIWObjectIdentifiers
import org.bouncycastle.asn1.x500.X500Name
import org.bouncycastle.asn1.x509.AlgorithmIdentifier
import org.bouncycastle.cert.X509v3CertificateBuilder
import org.bouncycastle.crypto.util.{PrivateKeyFactory, PublicKeyFactory, SubjectPublicKeyInfoFactory}
import org.bouncycastle.openssl.jcajce.JcaMiscPEMGenerator
import org.bouncycastle.operator.bc.BcRSAContentSignerBuilder
import org.bouncycastle.util.io.pem.PemWriter

class Certificate(issuer: X500Name,
                  serial: BigInt,
                  notBefore: Date,
                  notAfter: Date,
                  subject: X500Name,
                  keyPair: KeyPair) {

  private val x509Certificate = {
    val builder = new X509v3CertificateBuilder(issuer,
      serial.bigInteger,
      notBefore,
      notAfter,
      subject,
      SubjectPublicKeyInfoFactory.createSubjectPublicKeyInfo(PublicKeyFactory.createKey(keyPair.publicKey.getEncoded))
    )
    val sigAlgId = new AlgorithmIdentifier(OIWObjectIdentifiers.sha1WithRSA)
    val digAlgId = new AlgorithmIdentifier(OIWObjectIdentifiers.idSHA1)
    val privateKeyParam = PrivateKeyFactory.createKey(keyPair.privateKey.getEncoded)
    val contentSigner = new BcRSAContentSignerBuilder(sigAlgId, digAlgId).build(privateKeyParam)
    builder.build(contentSigner)
  }

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
