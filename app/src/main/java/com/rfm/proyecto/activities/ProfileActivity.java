package com.rfm.proyecto.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rfm.proyecto.R;
import com.rfm.proyecto.pojo.User;
import com.rfm.proyecto.utils.Alerts;

public class ProfileActivity extends AppCompatActivity {

  private static final String TAG = "AccessBD";

  private FirebaseUser firebaseUser;
  private Toolbar toolbar;
  private DrawerLayout drawerLayout;
  private ActionBarDrawerToggle actionBarDrawerToggle;
  private NavigationView navigationView;
  private Intent mainActivityIntent;
  private Intent mapsActivityIntent;

  private TextView textViewUsername;
  private TextView textViewEmail;
  private TextView textViewTelephone;

  private ProgressBar progressBarProfile;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile);
    initViews();
    setSupportActionBar(toolbar);

    FirebaseApp.initializeApp(this);

    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    if (firebaseUser == null) {
      toLoginActivity();
    }

    getDataCurrentUser();

    mainActivityIntent = new Intent(getApplication(), MainActivity.class);
    mapsActivityIntent = new Intent(getApplication(), MapsActivity.class);

    actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);
    actionBarDrawerToggle.setDrawerIndicatorEnabled(true);

    drawerLayout.addDrawerListener(actionBarDrawerToggle);

    actionBarDrawerToggle.syncState();

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
          case R.id.help_item:
            Toast.makeText(ProfileActivity.this, "Help", Toast.LENGTH_SHORT).show();
            break;
          case R.id.about_us_item:
            Alerts.alertDialogAbout(ProfileActivity.this,
                    getDrawable(R.drawable.icon_about_item),
                    getString(R.string.about_us),
                    getString(R.string.author).concat("\n"),
                    getString(R.string.github),
                    getString(R.string.accept));
            break;
        }
        return true;
      }
    });
  }

  private void toLoginActivity() {
    Intent intent = new Intent(getApplication(), LoginActivity.class);
    startActivity(intent);
    finish();
  }

  private void initViews() {
    toolbar = findViewById(R.id.app_bar);
    drawerLayout = findViewById(R.id.activity_profile);
    navigationView = findViewById(R.id.navigationView);
    textViewEmail = findViewById(R.id.textViewEmail);
    textViewUsername = findViewById(R.id.textViewUsername);
    textViewTelephone = findViewById(R.id.textViewTelephone);
    progressBarProfile = findViewById(R.id.progressBarProfile);
    progressBarProfile.setVisibility(View.GONE);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater menuInflater = getMenuInflater();

    menuInflater.inflate(R.menu.menu_resource, menu);
    return super.onCreateOptionsMenu(menu);
  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.action_signout) {
      FirebaseAuth.getInstance().signOut();
      Intent intentLogin = new Intent(getApplicationContext(), LoginActivity.class);
      startActivity(intentLogin);
      finish();
      return true;

    } else {
      return actionBarDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

  }

  private void getDataCurrentUser() {

    final DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Users");

    progressBarProfile.setVisibility(View.VISIBLE);

    userReference.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        progressBarProfile.setVisibility(View.GONE);

        //dataSnapshot.getValue(User.class);

        textViewUsername.setText(dataSnapshot.child(firebaseUser.getUid()).child("username").getValue().toString());
        textViewEmail.setText(dataSnapshot.child(firebaseUser.getUid()).child("email").getValue().toString());
        textViewTelephone.setText(dataSnapshot.child(firebaseUser.getUid()).child("telephone").getValue().toString());
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {
        Log.w(TAG, "createUserWithEmail:failure", databaseError.toException());
      }
    });


  }
}