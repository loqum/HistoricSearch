package com.rfm.proyecto.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rfm.proyecto.R;
import com.rfm.proyecto.pojo.Location;
import com.rfm.proyecto.pojo.User;
import com.rfm.proyecto.utils.Alerts;
import com.rfm.proyecto.utils.MapsUtil;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

  private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 0;
  private static final String TAG = "MapsActivity";

  private GoogleMap mMap;
  private FirebaseUser firebaseUser;
  private NavigationView navigationView;
  private Intent mainActivityIntent;
  private Intent mapsActivityIntent;
  private Intent profileActivityIntent;
  private DatabaseReference MARKERS_DATABASE_REFERENCE;
  private User user;
  private Location location;
  private List<Location> markers;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_maps);
    initViews();
    initObjects();

    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    MARKERS_DATABASE_REFERENCE = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("userLocations");

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


    navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
      @Override
      public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {

          case R.id.home_item:
            startActivity(mainActivityIntent);
            finish();
            break;

          case R.id.map_item:
            startActivity(mapsActivityIntent);
            finish();
            break;

          case R.id.my_profile_item:
            startActivity(profileActivityIntent);
            finish();
            break;

          case R.id.help_item:
            Toast.makeText(MapsActivity.this, "Help", Toast.LENGTH_SHORT).show();
            break;

          case R.id.about_us_item:
            Alerts.alertDialog(MapsActivity.this, getDrawable(R.drawable.icon_about_item), getString(R.string.about_us), getString(R.string.author).concat("\n"), getString(R.string.github), getString(R.string.accept));
            break;
        }
        return true;
      }
    });


  }

  @Override
  public void onMapReady(GoogleMap googleMap) {
    mMap = googleMap;
    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
      mMap.setMyLocationEnabled(true);

    } else {
      ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_FINE_LOCATION);
    }

    UiSettings uiSettings = mMap.getUiSettings();
    uiSettings.setZoomControlsEnabled(true);
    uiSettings.setMyLocationButtonEnabled(true);

    LatLng barcelona = new LatLng(41.3887901, 2.1589899);
    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(barcelona, Float.parseFloat(getApplication().getString(R.string.ZOOMLEVEL12))));

    setLocationMarker();

    mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

      @Override
      public void onMapLongClick(LatLng point) {
        mMap.addMarker(new MarkerOptions().position(point).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        Marker marker = mMap.addMarker(new MarkerOptions().position(point).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

        List<LatLng> latLngs = new ArrayList<>();
        latLngs.add(marker.getPosition());

        LatLng latLng = marker.getPosition();
        location.setLatitude(latLng.latitude);
        location.setLongitude(latLng.longitude);
        location.setMarkers(latLngs);

        MapsUtil.setPositionsUser(navigationView, firebaseUser, location, "Posición guardada!");
      }

    });

    MARKERS_DATABASE_REFERENCE.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

        for (DataSnapshot next : dataSnapshot.getChildren()) {
          String key = next.getKey();

          location = next.getValue(Location.class);

          LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

          mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {
        Log.d(TAG, "Cancelled.");
      }
    });

  }

  public void onClickButtonSatellite(View view) {
    if (mMap.getMapType() != GoogleMap.MAP_TYPE_SATELLITE) {
      mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    }
  }

  public void onClickButtonMap(View view) {
    if (mMap.getMapType() != GoogleMap.MAP_TYPE_NORMAL) {
      mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }
  }

  private void initViews() {
    navigationView = findViewById(R.id.navigationView);
  }

  private void initObjects() {
    user = new User();
    location = new Location();
    markers = new ArrayList<>();
    mainActivityIntent = new Intent(getApplication(), MainActivity.class);
    mapsActivityIntent = new Intent(getApplication(), MapsActivity.class);
    profileActivityIntent = new Intent(getApplication(), ProfileActivity.class);
  }

  private void setLocationMarker() {
    Intent intent = getIntent();
    Bundle bundle = intent.getExtras();

    if (bundle != null) {
      LatLng latLng = (LatLng) bundle.get("MARKER");
      String name = (String) bundle.get("NAME");

      mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
      mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title(name));
      mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, Float.parseFloat(getApplication().getString(R.string.ZOOMLEVEL14))));
    }
  }

}
