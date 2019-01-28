package com.themadphysicist;

public class Subscriber {
  private String id, email, datetimeSubbed, deleted_at;
  public Subscriber() {}
  public Subscriber(String id, String email, String datetimeSubbed, String deleted_at) {
    this.id = id;
    this.email = email;
    this.datetimeSubbed = datetimeSubbed;
    this.deleted_at = deleted_at;
  }
  public String getId() {
    return id;
  }
  public String getEmail() {
    return email;
  }
  public String getDatetimeSubbed() {
    return datetimeSubbed;
  }
  public String getDeleted_at() {
    return deleted_at;
  }
  public void setId(String id) {
    this.id = id;
  }
  public void setEmail(String email) {
    this.email = email;
  }
  public void setDatetimeSubbed(String datetimeSubbed) {
    this.datetimeSubbed = datetimeSubbed;
  }
  public void setDeleted_at(String deleted_at) {
    this.deleted_at = deleted_at;
  }
}
