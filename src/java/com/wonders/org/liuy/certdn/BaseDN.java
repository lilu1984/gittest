package com.wonders.org.liuy.certdn;

public abstract class BaseDN
{
  private String fingerprint;
  private String serial;
  private String before;
  private String after;
  private String subject;

  public String getSubject()
  {
    return this.subject; }

  public void setSubject(String subject) {
    this.subject = subject; }

  public String getAfter() {
    return this.after; }

  public String getBefore() {
    return this.before; }

  public String getFingerprint() {
    return this.fingerprint; }

  public String getSerial() {
    return this.serial; }

  public void setAfter(String after) {
    this.after = after; }

  public void setBefore(String before) {
    this.before = before; }

  public void setFingerprint(String fingerprint) {
    this.fingerprint = fingerprint; }

  public void setSerial(String serial) {
    this.serial = serial;
  }
}