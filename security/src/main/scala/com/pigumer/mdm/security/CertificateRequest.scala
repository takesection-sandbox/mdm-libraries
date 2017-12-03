package com.pigumer.mdm.security

import java.io.StringWriter

import org.bouncycastle.asn1.oiw.OIWObjectIdentifiers
import org.bouncycastle.asn1.x500.X500Name
import org.bouncycastle.asn1.x509.AlgorithmIdentifier
import org.bouncycastle.crypto.util.{PrivateKeyFactory, PublicKeyFactory, SubjectPublicKeyInfoFactory}
import org.bouncycastle.openssl.jcajce.JcaMiscPEMGenerator
import org.bouncycastle.operator.bc.BcRSAContentSignerBuilder
import org.bouncycastle.pkcs.{PKCS10CertificationRequest, PKCS10CertificationRequestBuilder}
import org.bouncycastle.util.io.pem.PemWriter

class CertificateRequest(subject: X500Name, keyPair: KeyPair) {

  val certificateRequest: PKCS10CertificationRequest = {
    val publicKeyParam = PublicKeyFactory.createKey(keyPair.publicKey.getEncoded)
    val subjectPublicKeyInfo = SubjectPublicKeyInfoFactory.createSubjectPublicKeyInfo(publicKeyParam)
    val builder = new PKCS10CertificationRequestBuilder(subject, subjectPublicKeyInfo)
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
        val pemObject = new JcaMiscPEMGenerator(certificateRequest)
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
