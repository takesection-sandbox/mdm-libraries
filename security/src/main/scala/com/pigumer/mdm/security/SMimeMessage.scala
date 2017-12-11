package com.pigumer.mdm.security

import java.io.InputStream
import javax.mail.Session
import javax.mail.internet.{MimeBodyPart, MimeMessage}

import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter
import org.bouncycastle.cms.jcajce.{JceKeyTransEnvelopedRecipient, JceKeyTransRecipientId}
import org.bouncycastle.mail.smime.{SMIMEEnveloped, SMIMEUtil}

trait SMimeMessage {
  def decrypt(inputStream: InputStream, certificate: X509Certificate, key: PrivateKey): MimeBodyPart = {
    val session = Session.getDefaultInstance(System.getProperties, null)
    val message = new MimeMessage(session, inputStream)
    val enveloped = new SMIMEEnveloped(message)
    val recipientId = new JceKeyTransRecipientId(
      new JcaX509CertificateConverter().getCertificate(certificate.x509Certificate))
    val recipients = enveloped.getRecipientInfos
    val recipient = recipients.get(recipientId)
    val keyTransRecipient = new JceKeyTransEnvelopedRecipient(key.privateKey)
    SMIMEUtil.toMimeBodyPart(recipient.getContent(keyTransRecipient))
  }
}
