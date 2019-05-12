package com.rfm.proyecto.controller;

import android.content.Context;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class FirebaseDatabaseHelper {

  public FirebaseDatabaseHelper(Context context) {
    super();
  }

  public void updateUsername(FirebaseUser firebaseUser, String newUsername) {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    databaseReference.child("Users").child(firebaseUser.getUid()).child("username").setValue(newUsername);
  }

}


