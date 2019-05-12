package com.rfm.proyecto.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rfm.proyecto.R;
import com.rfm.proyecto.utils.Alerts;
import com.rfm.proyecto.utils.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = "MainActivity";

  private FirebaseUser firebaseUser;
  private Toolbar toolbar;
  private DrawerLayout drawerLayout;
  private ActionBarDrawerToggle actionBarDrawerToggle;
  private NavigationView navigationView;
  private RecyclerView recyclerView;
  private RecyclerViewAdapter recyclerViewAdapter;
  private Intent mainActivityIntent;
  private Intent mapsActivityIntent;
  private Intent profileActivityIntent;
  private List<String> names = new ArrayList<>();
  private List<String> imageUrls = new ArrayList<>();


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    FirebaseApp.initializeApp(this);

    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    if (firebaseUser == null) {
      toLoginActivity();
    }

    initViews();
    setSupportActionBar(toolbar);
    initImageBitmaps();

    mainActivityIntent = new Intent(getApplication(), MainActivity.class);
    mapsActivityIntent = new Intent(getApplication(), MapsActivity.class);
    profileActivityIntent = new Intent(getApplication(), ProfileActivity.class);

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
            break;

          case R.id.my_profile_item:
            startActivity(profileActivityIntent);
            break;

          case R.id.help_item:
            Toast.makeText(MainActivity.this, "Help", Toast.LENGTH_SHORT).show();
            break;

          case R.id.about_us_item:
            Alerts.alertDialogAbout(MainActivity.this,
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
    drawerLayout = findViewById(R.id.activity_main);
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

  private void initImageBitmaps() {
    Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

    imageUrls.add("http://enbarcelona.com/public/images_web/enbarcelona/a5810dbb8a9d0023778e8a3042847fc5.jpg");
    names.add("Baetulo");

    imageUrls.add("http://turisme.elbaixllobregat.cat/sites/default/files/styles/slide_dp/public/webform/termes%20romanes.jpg?itok=3PfP5sZu");
    names.add("Termas romanas de Sant Boi");

    imageUrls.add("https://upload.wikimedia.org/wikipedia/commons/thumb/3/34/Catalonia_Matar%C3%B3_Vil%C2%B7laRomanaDeCanLlauder_VistaGeneral.JPG/1200px-Catalonia_Matar%C3%B3_Vil%C2%B7laRomanaDeCanLlauder_VistaGeneral.JPG");
    names.add("Villa romana de Can Llauder");

    imageUrls.add("https://upload.wikimedia.org/wikipedia/commons/thumb/3/3c/Columnas_Templo_de_Augusto_Barcelona.jpg/273px-Columnas_Templo_de_Augusto_Barcelona.jpg");
    names.add("Templo de Augusto de Barcelona");

    initRecyclerView();
  }

  private void initRecyclerView() {
    Log.d(TAG, "initRecyclerView: init RecyclerView.");
    recyclerView = findViewById(R.id.recycler_view);
    recyclerViewAdapter = new RecyclerViewAdapter(this, names, imageUrls);
    recyclerView.setAdapter(recyclerViewAdapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
  }

}

