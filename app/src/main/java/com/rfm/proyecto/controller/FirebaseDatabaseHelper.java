package com.rfm.proyecto.controller;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rfm.proyecto.pojo.User;

public class FirebaseDatabaseHelper {

  private DatabaseReference databaseReference;

  public FirebaseDatabaseHelper(Context context) {
    super();
  }

  public void writeNewUser(User user) {

    databaseReference = FirebaseDatabase.getInstance().getReference();

    databaseReference.child("users").child(user.getId()).child("username").setValue(user.getUsername());
    databaseReference.child("users").child(user.getId()).child("email").setValue(user.getEmail());

  }

  public void updateUsername(String id, String newUsername) {
    databaseReference = FirebaseDatabase.getInstance().getReference();
    databaseReference.child("users").child(id).child("username").setValue(newUsername);
  }

}
