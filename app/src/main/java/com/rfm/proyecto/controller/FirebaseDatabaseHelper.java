package com.rfm.proyecto.controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rfm.proyecto.R;
import com.rfm.proyecto.pojo.User;

public class FirebaseDatabaseHelper {

  private DatabaseReference databaseReference;

  public FirebaseDatabaseHelper(Context context) {
    super();
  }

  public void writeNewUser(User user, final Snackbar snackbar, String message) {

    databaseReference = FirebaseDatabase.getInstance().getReference();

    databaseReference.child("users").child(user.getId()).child("username").setValue(user.getUsername());
    databaseReference.child("users").child(user.getId()).child("email").setValue(user.getEmail());

  }

  public void updateUsername(String id, String newUsername) {
    databaseReference = FirebaseDatabase.getInstance().getReference();
    databaseReference.child("users").child(id).child("username").setValue(newUsername);
  }

}
