package com.rfm.proyecto.utils;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rfm.proyecto.pojo.User;

import java.util.ArrayList;
import java.util.List;

public class MapsUtil {

  public MapsUtil() {
    super();
  }

  private static final String TAG = "CONNECTION FIREDATABASE";

  public static List<LatLng> getPositionsUser(final FirebaseUser firebaseUser) {
    final List<LatLng> positions = new ArrayList<>();
    final DatabaseReference POINT_USER_REFERENCE = FirebaseDatabase.getInstance().getReference("Users");

    POINT_USER_REFERENCE.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

        dataSnapshot.getValue(User.class);

        if (dataSnapshot.child(firebaseUser.getUid()).child("locations").getValue() != null) {
          positions.add((LatLng) dataSnapshot.child(firebaseUser.getUid()).child("locations").getValue());
        }

      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {
        Log.w(TAG, "connectionToFiredatabase:failure", databaseError.toException());
      }
    });

    return positions;
  }

  public static void setPositionsUser(final View view, final FirebaseUser firebaseUser, List<LatLng> allPositions, final String messageSnackbar) {
    final DatabaseReference POINTS_USER_REFERENCE = FirebaseDatabase.getInstance().getReference("Users");

    POINTS_USER_REFERENCE.child(firebaseUser.getUid()).child("Positions").setValue(allPositions).addOnCompleteListener(new OnCompleteListener<Void>() {
      @Override
      public void onComplete(@NonNull Task<Void> task) {
        Snackbar.make(view, messageSnackbar, Snackbar.LENGTH_LONG).show();
      }

    });
  }
}

