package com.rfm.proyecto.pojo;

public class HistoricLocation {

  private String name;
  private String descriptionLocation;
  private String url;
  private String info;
  private double latitude;
  private double longitude;

  public HistoricLocation() {
  }

  public HistoricLocation(String name, String descriptionLocation, String url, String info, double latitude, double longitude) {
    this.name = name;
    this.descriptionLocation = descriptionLocation;
    this.url = url;
    this.info = info;
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescriptionLocation() {
    return descriptionLocation;
  }

  public void setDescriptionLocation(String descriptionLocation) {
    this.descriptionLocation = descriptionLocation;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getInfo() {
    return info;
  }

  public void setInfo(String info) {
    this.info = info;
  }
}
