package com.pigumer.mdm.security.darwin

class CertificateSigningRequest {
  val csr = ""
  val chain = ""
  val signature = ""
  val plist =
    <plist version="1.0">
      <dict>
        <key>PushCertRequestCSR</key>
        <string>
          { csr }
        </string>
        <key>PushCertCertificateChain</key>
        <string>
          { chain }
        </string>
        <key>PushCertSignature</key>
        <string>
          { signature }
        </string>
      </dict>
    </plist>

}
