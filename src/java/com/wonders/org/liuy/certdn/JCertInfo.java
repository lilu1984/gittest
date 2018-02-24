package com.wonders.org.liuy.certdn;

import java.security.cert.X509Certificate;

public class JCertInfo extends BaseDN
{
  private X509Certificate x509;

  private JCertInfo()
  {
  }

  public JCertInfo(X509Certificate x509)
  {
    this.x509 = x509;
    setCetInfo();
  }

  private void setCetInfo() {
    setSerial(TummbPrintUtils.getSerialNumber(this.x509));
    setSubject(this.x509.getSubjectDN().getName());
    setFingerprint(TummbPrintUtils.getThumbprint(this.x509));
  }
}