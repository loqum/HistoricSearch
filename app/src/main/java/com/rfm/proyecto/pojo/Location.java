package com.rfm.proyecto.pojo;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class Location {

  private double Latitude;
  private double Longitude;
  private List<LatLng> markers;

  public Location() {

  }

  public Location(double latitude, double longitude) {
    Latitude = latitude;
    Longitude = longitude;
  }

  public Location(List<LatLng> markers) {
    this.markers = markers;
  }

  public double getLatitude() {
    return Latitude;
  }

  public void setLatitude(double latitude) {
    Latitude = latitude;
  }

  public double getLongitude() {
    return Longitude;
  }

  public void setLongitude(double longitude) {
    Longitude = longitude;
  }

  public List<LatLng> getMarkers() {    return markers;
  }

  public void setMarkers(List<LatLng> markers) {
    this.markers = markers;
  }
}
