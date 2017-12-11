package com.pigumer.mdm.security

import java.io.InputStream
import javax.mail.Session
import javax.mail.internet.{MimeBodyPart, MimeMessage}

import org.bouncycastle.cms.jcajce.JceKeyTransEnvelopedRecipient
import org.bouncycastle.mail.smime.{SMIMEEnveloped, SMIMEUtil}

import scala.collection.JavaConverters._

trait SMimeMessage {
  def decrypt(inputStream: InputStream, key: PrivateKey): MimeBodyPart = {
    val session = Session.getDefaultInstance(System.getProperties, null)
    val message = new MimeMessage(session, inputStream)
    val enveloped = new SMIMEEnveloped(message)
    val recipients = enveloped.getRecipientInfos
    val recipient = recipients.getRecipients.asScala.head
    val keyTransRecipient = new JceKeyTransEnvelopedRecipient(key.privateKey)
    SMIMEUtil.toMimeBodyPart(recipient.getContent(keyTransRecipient))
  }
}
