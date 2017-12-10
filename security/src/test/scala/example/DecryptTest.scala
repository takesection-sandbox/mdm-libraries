package example

import java.io.InputStreamReader
import javax.mail.Session
import javax.mail.internet.MimeMessage

import com.pigumer.mdm.security.PrivateKey
import org.bouncycastle.cms.jcajce.JceKeyTransEnvelopedRecipient
import org.bouncycastle.mail.smime.{SMIMEEnveloped, SMIMEUtil}

import scala.collection.JavaConverters._

object DecryptTest extends App {

  val smime = "smime.p7m"
  val privateKeyFile = "private.pem"

  def key = {
    val is = Thread.currentThread.getContextClassLoader.getResourceAsStream(privateKeyFile)
    val reader = new InputStreamReader(is)
    PrivateKey.parse(reader)
  }

  def token = {
    val is = Thread.currentThread.getContextClassLoader.getResourceAsStream(smime)
    val session = Session.getDefaultInstance(System.getProperties, null)
    val msg = new MimeMessage(session, is)
    val m = new SMIMEEnveloped(msg)
    val recipients = m.getRecipientInfos
    val recipient = recipients.getRecipients.asScala.head
    val r = new JceKeyTransEnvelopedRecipient(key.privateKey)
    SMIMEUtil.toMimeBodyPart(recipient.getContent(r))
  }

  val t = token.getContent
  println(t)
}
