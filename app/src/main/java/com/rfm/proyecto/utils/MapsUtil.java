package com.rfm.proyecto.utils;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rfm.proyecto.pojo.Location;

public class MapsUtil {

  public MapsUtil() {
    super();
  }

  private static final String TAG = "MapsUtil";

  public static void setPositionsUser(final View view, final FirebaseUser firebaseUser, Location location, final String messageSnackbar) {

    final DatabaseReference POINTS_USER_REFERENCE = FirebaseDatabase.getInstance().getReference("Users")
            .child(firebaseUser.getUid())
            .child("userLocations");

    String key = POINTS_USER_REFERENCE.push().getKey();

    POINTS_USER_REFERENCE.child(key).child("latitude").setValue(location.getLatitude());
    POINTS_USER_REFERENCE.child(key).child("longitude").setValue(location.getLongitude()).addOnCompleteListener(new OnCompleteListener<Void>() {
      @Override
      public void onComplete(@NonNull Task<Void> task) {
        Snackbar.make(view, messageSnackbar, Snackbar.LENGTH_LONG).show();
      }
    });

  }
}

