package com.rfm.proyecto.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rfm.proyecto.pojo.Location;
import com.rfm.proyecto.pojo.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsUtil {

  public MapsUtil() {
    super();
  }

  private static final String TAG = "CONNECTION FIREDATABASE";

  /*public static List<Marker> getPositionsUser(final FirebaseUser firebaseUser) {
    final List<Marker> markers;
    final DatabaseReference pointsDatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("Positions").child("-Le1iZH9bsGlvWpTJKih");

    *//*ChildEventListener childEventListener = new ChildEventListener() {
      @Override
      public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        markers.add(marker);
      }

      @Override
      public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

      }

      @Override
      public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

      }

      @Override
      public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    };

    pointsDatabaseReference.addChildEventListener(childEventListener);*//*

    pointsDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        Marker marker;

        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
          final Map<String, Marker> mapMarkers = (HashMap<String, Marker>) postSnapshot.getValue();


        }

      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {
        Log.w(TAG, "connectionToFiredatabase:failure", databaseError.toException());
      }
    });

    return null;
  }*/


  public static void setPositionsUser(final View view, final FirebaseUser firebaseUser, Location location, final String messageSnackbar) {

    final DatabaseReference POINTS_USER_REFERENCE = FirebaseDatabase.getInstance().getReference("Users")
            .child(firebaseUser.getUid())
            .child("locations");

    String key = POINTS_USER_REFERENCE.push().getKey();

    POINTS_USER_REFERENCE.child(key).child("latitude").setValue(location.getLatitude());
    POINTS_USER_REFERENCE.child(key).child("longitude").setValue(location.getLongitude()).addOnCompleteListener(new OnCompleteListener<Void>() {
      @Override
      public void onComplete(@NonNull Task<Void> task) {
        Snackbar.make(view, messageSnackbar, Snackbar.LENGTH_LONG).show();
      }
    });




    /*POINTS_USER_REFERENCE.setValue(allMarkers).addOnCompleteListener(new OnCompleteListener<Void>() {
      @Override
      public void onComplete(@NonNull Task<Void> task) {
        Snackbar.make(view, messageSnackbar, Snackbar.LENGTH_LONG).show();
      }

    });*/
  }
}

