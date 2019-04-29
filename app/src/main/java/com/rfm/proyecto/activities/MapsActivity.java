package com.rfm.proyecto.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rfm.proyecto.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

  private GoogleMap mMap;
  private NavigationView navigationView;
  private Intent mainActivityIntent;
  private Intent mapsActivityIntent;
  private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_maps);
    GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();

    int status = googleApiAvailability.isGooglePlayServicesAvailable(getApplicationContext());

    if (status == ConnectionResult.SUCCESS) {
      SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
              .findFragmentById(R.id.map);
      mapFragment.getMapAsync(this);

    } else {
      Dialog dialog = googleApiAvailability.getErrorDialog(this, status, 10);
      dialog.show();
    }

    initViews();

    mainActivityIntent = new Intent(getApplication(), MainActivity.class);
    mapsActivityIntent = new Intent(getApplication(), MapsActivity.class);

    navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
      @Override
      public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
          case R.id.home:
            startActivity(mainActivityIntent);
            finish();
            break;
          case R.id.settings:
            startActivity(mapsActivityIntent);
            break;
          case R.id.help:
            Toast.makeText(MapsActivity.this, "Help", Toast.LENGTH_SHORT).show();
            break;
          case R.id.about_us:
            Toast.makeText(MapsActivity.this, "AboutUs", Toast.LENGTH_SHORT).show();
            break;
        }
        return true;
      }
    });

  }

  @Override
  public void onMapReady(GoogleMap googleMap) {
    mMap = googleMap;

    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
      mMap.setMyLocationEnabled(true);

    } else {
      ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_FINE_LOCATION);
    }

    UiSettings uiSettings = mMap.getUiSettings();
    uiSettings.setZoomControlsEnabled(true);
    uiSettings.setMyLocationButtonEnabled(true);

    // Add a marker in Barcelona and move the camera
    LatLng barcelona = new LatLng(41.3887901, 2.1589899);
    mMap.addMarker(new MarkerOptions().position(barcelona).title("Marker in Barcelona"));
    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(barcelona, Float.parseFloat(getApplication().getString(R.string.ZOOMLEVEL12))));
  }

  private void initViews() {
    navigationView = findViewById(R.id.navigationView);
  }


}