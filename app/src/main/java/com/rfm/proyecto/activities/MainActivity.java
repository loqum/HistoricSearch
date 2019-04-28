package com.rfm.proyecto.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rfm.proyecto.R;

public class MainActivity extends AppCompatActivity {

  private FirebaseUser firebaseUser;
  private Toolbar toolbar;
  private DrawerLayout drawerLayout;
  private ActionBarDrawerToggle actionBarDrawerToggle;
  private NavigationView navigationView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    initViews();
    setSupportActionBar(toolbar);

    FirebaseApp.initializeApp(this);

    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    if (firebaseUser == null) {
      updateUI();
    }

    actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);
    actionBarDrawerToggle.setDrawerIndicatorEnabled(true);

    drawerLayout.addDrawerListener(actionBarDrawerToggle);

    actionBarDrawerToggle.syncState();

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
      @Override
      public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
          case R.id.my_profile:
            Toast.makeText(MainActivity.this, "MyProfile", Toast.LENGTH_LONG).show();
          case R.id.settings:
            Toast.makeText(MainActivity.this, "Settings", Toast.LENGTH_LONG).show();
          case R.id.edit_profile:
            Toast.makeText(MainActivity.this, "EditProfile", Toast.LENGTH_LONG).show();
          case R.id.help:
            Toast.makeText(MainActivity.this, "Help", Toast.LENGTH_LONG).show();
          case R.id.about_us:
            Toast.makeText(MainActivity.this, "AboutUs", Toast.LENGTH_LONG).show();
        }
        return true;
      }
    });
  }

  private void updateUI() {
    Intent intent = new Intent(getApplication(), LoginActivity.class);
    startActivity(intent);
    finish();
  }

  private void initViews() {
    toolbar = findViewById(R.id.app_bar);
    drawerLayout = findViewById(R.id.drawerLayout);
    navigationView = findViewById(R.id.navigationView);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater menuInflater = getMenuInflater();

    menuInflater.inflate(R.menu.menu_resource, menu);
    return super.onCreateOptionsMenu(menu);
  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    switch (item.getItemId()) {

      case R.id.action_signout:
        FirebaseAuth.getInstance().signOut();
        Intent intentLogin = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intentLogin);
        finish();
        return true;

      default:
        return super.onOptionsItemSelected(item);

    }
  }
}

