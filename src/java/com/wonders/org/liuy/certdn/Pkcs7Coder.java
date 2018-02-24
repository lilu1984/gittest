package com.wonders.org.liuy.certdn;

import java.io.IOException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import sun.security.pkcs.PKCS7;
import sun.security.pkcs.ParsingException;

public class Pkcs7Coder
{
  private PKCS7 pk7;
  public Pkcs7Coder(String pkcs7)
  {
    byte[] b = Base64.decode(pkcs7);
    try {
      this.pk7 = new PKCS7(b);
    } catch (ParsingException e) {
      e.printStackTrace();
    }
  }

  public X509Certificate getCert()
  {
    Certificate[] cert = this.pk7.getCertificates();
    X509Certificate x509 = (X509Certificate)cert[0];
    return x509;
  }

  public String getData()
  {
    byte[] b = (byte[])null;
    try {
      b = this.pk7.getContentInfo().getData();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return new String(b);
  }
}