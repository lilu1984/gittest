package com.wonders.org.liuy.certdn;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;

public class TummbPrintUtils
{
  public static String getThumbprint(X509Certificate cert, String thumAlg, String delimiter)
  {
    if (cert == null) {
      return null;
    }

    if ((thumAlg == null) || (thumAlg.length() == 0)) {
      return null;
    }

    String thumbPrint = "";
    try {
      MessageDigest md = MessageDigest.getInstance(thumAlg);
      byte[] rawDigest = md.digest(cert.getEncoded());
      thumbPrint = getHex(rawDigest, delimiter);
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (CertificateEncodingException e) {
      e.printStackTrace();
    }
    return thumbPrint;
  }

  public static String getThumbprint(X509Certificate cert)
  {
    return getThumbprint(cert, "sha1", null);
  }

  public static String getThumbprint(X509Certificate cert, String delimiter)
  {
    return getThumbprint(cert, "sha1", delimiter);
  }

  public static String getSerialNumber(X509Certificate cert)
  {
    byte[] b = cert.getSerialNumber().toByteArray();
    return getHex(b, "");
  }

  private static String getHex(byte[] buf, String delimiter)
  {
    String result = "";

    if (buf == null) {
      return "";
    }

    String defaultDelimiter = "";
    if ((delimiter != null) && (delimiter.length() > 0)) {
      defaultDelimiter = delimiter;
    }

    for (int i = 0; i < buf.length; ++i) {
      if (i > 0) {
        result = result + defaultDelimiter;
      }

      short sValue = buf[i];
      int iValue = 0;
      iValue += sValue;
      String converted = Integer.toHexString(iValue);

      if (converted.length() > 2) {
        converted = converted.substring(converted.length() - 2);
      }
      else if (converted.length() < 2) {
        converted = "0" + converted;
      }

      result = result + converted.toUpperCase();
    }
    return result;
  }
}