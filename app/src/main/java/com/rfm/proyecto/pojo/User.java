package com.rfm.proyecto.pojo;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.List;

public class User {

  private String id;
  private String username;
  private String password;
  private String email;
  private String telephone;
  private List<Marker> locations;

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

  public User(String username, String password, String email, List<Marker> locations) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.locations = locations;
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

  public List<Marker> getLocation() {
    return locations;
  }

  public void setLocation(List<Marker> locations) {
    this.locations = locations;
  }
}
