package com.rfm.proyecto.pojo;

public class User {

  private String id;
  private String username;
  private String password;
  private String email;
  private String telephone;
  private String location;

  public User() {

  }

  public User(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public User(String username, String telephone, String email) {
    this.username = username;
    this.telephone = telephone;
    this.email = email;
  }

  public User(String username, String password, String email, String location) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.location = location;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTelephone() {
    return telephone;
  }

  public void setTelephone(String telephone) {
    this.telephone = telephone;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }
}
