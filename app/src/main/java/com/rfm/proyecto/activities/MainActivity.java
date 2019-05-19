package com.rfm.proyecto.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rfm.proyecto.R;
import com.rfm.proyecto.pojo.HistoricLocation;
import com.rfm.proyecto.utils.Alerts;
import com.rfm.proyecto.utils.LangUtils;
import com.rfm.proyecto.utils.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = "MainActivity";

  private Toolbar toolbar;
  private DrawerLayout drawerLayout;
  private ActionBarDrawerToggle actionBarDrawerToggle;
  private NavigationView navigationView;
  private Intent mainActivityIntent;
  private Intent mapsActivityIntent;
  private Intent profileActivityIntent;
  private List<String> names = new ArrayList<>();
  private List<String> imageUrls = new ArrayList<>();
  private List<String> descriptionLocations = new ArrayList<>();
  private List<String> infos = new ArrayList<>();
  private List<Double> latitudes = new ArrayList<>();
  private List<Double> longitudes = new ArrayList<>();
  private HistoricLocation historicLocation;
  private CircleImageView imageLocation;
  private TextView titleLocation;
  private TextView textLocation;
  private Button buttonBack;
  private ImageButton buttonViewOnMap;
  private AlertDialog dialog = null;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    FirebaseApp.initializeApp(this);
    LangUtils.getLocale(this);
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    if (firebaseUser == null) {
      toLoginActivity();
    }

    initViews();
    setSupportActionBar(toolbar);
    initImageBitmaps();
    initBuilder();

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
            Alerts.alertDialog(MainActivity.this,
                    getDrawable(R.drawable.icon_help_item),
                    getString(R.string.help),
                    getString(R.string.message_one_help),
                    getString(R.string.message_two_help),
                    getString(R.string.accept));
            break;

          case R.id.about_us_item:
            Alerts.alertDialog(MainActivity.this,
                    getDrawable(R.drawable.icon_about_item),
                    getString(R.string.about_us),
                    getString(R.string.author).concat("\n"),
                    getString(R.string.github),
                    getString(R.string.accept));
            break;

          case R.id.lang_es_item:
            LangUtils.setLocale(MainActivity.this, "es");
            recreate();
            break;

          case R.id.lang_ca_item:
            LangUtils.setLocale(MainActivity.this, "ca");
            recreate();
            break;

          case R.id.lang_en_item:
            LangUtils.setLocale(MainActivity.this, "en");
            recreate();
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
    drawerLayout = findViewById(R.id.activityMain);
    navigationView = findViewById(R.id.navigationView);

  }

  private void initObjects(View view) {
    imageLocation = view.findViewById(R.id.imageLocation);
    textLocation = view.findViewById(R.id.textLocation);
    titleLocation = view.findViewById(R.id.titleLocation);
    buttonBack = view.findViewById(R.id.buttonBack);
    buttonViewOnMap = view.findViewById(R.id.buttonViewOnMap);
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

    DatabaseReference DATABASE_REFERENCE = FirebaseDatabase.getInstance().getReference("HistoricLocations");

    DATABASE_REFERENCE.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        for (DataSnapshot next : dataSnapshot.getChildren()) {

          historicLocation = next.getValue(HistoricLocation.class);

          if (historicLocation != null) {
            descriptionLocations.add(historicLocation.getDescriptionLocation());
            infos.add(historicLocation.getInfo());
            names.add(historicLocation.getName());
            imageUrls.add(historicLocation.getUrl());
            latitudes.add(historicLocation.getLatitude());
            longitudes.add(historicLocation.getLongitude());
          }

          initRecyclerView();

          Log.d("TAG", "DATABASE_REFERENCE:addListenerForSingleValueEvent");

        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {
        Log.d("TAG", "DATABASE_REFERENCE:ERROR" + databaseError.getMessage());
      }
    });

  }

  private void initRecyclerView() {
    Log.d(TAG, "initRecyclerView: init RecyclerView.");
    RecyclerView recyclerView = findViewById(R.id.recycler_view);
    RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(this, names, imageUrls, descriptionLocations);
    recyclerView.setAdapter(recyclerViewAdapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setHasFixedSize(true);
  }

  @Override
  public boolean onContextItemSelected(MenuItem item) {
    int clickedItemPosition = item.getOrder();

    switch (item.getItemId()) {

      case 121:

        switch (clickedItemPosition) {
          case 0:
            Glide.with(this).asBitmap().load(imageUrls.get(0)).into(imageLocation);
            titleLocation.setText(names.get(0));
            textLocation.setText(infos.get(0));
            dialog.show();

            buttonBack.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                dialog.dismiss();
              }
            });

            buttonViewOnMap.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                final LatLng BAETULO = new LatLng(latitudes.get(0), longitudes.get(0));
                startActivity(mapsActivityIntent
                        .putExtra("MARKER", BAETULO)
                        .putExtra("NAME", names.get(0)));
              }
            });

            break;

          case 1:

            Glide.with(this).asBitmap().load(imageUrls.get(1)).into(imageLocation);
            titleLocation.setText(names.get(1));
            textLocation.setText(infos.get(1));
            dialog.show();

            buttonBack.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                dialog.dismiss();
              }
            });

            buttonViewOnMap.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                final LatLng BAETULO = new LatLng(latitudes.get(1), longitudes.get(1));
                startActivity(mapsActivityIntent
                        .putExtra("MARKER", BAETULO)
                        .putExtra("NAME", names.get(1)));
              }
            });

            break;

          case 2:

            Glide.with(this).asBitmap().load(imageUrls.get(2)).into(imageLocation);
            titleLocation.setText(names.get(2));
            textLocation.setText(infos.get(2));
            dialog.show();

            buttonBack.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                dialog.dismiss();
              }
            });

            buttonViewOnMap.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                final LatLng BAETULO = new LatLng(latitudes.get(2), longitudes.get(2));
                startActivity(mapsActivityIntent
                        .putExtra("MARKER", BAETULO)
                        .putExtra("NAME", names.get(2)));
              }
            });

            break;

          case 3:

            Glide.with(this).asBitmap().load(imageUrls.get(3)).into(imageLocation);
            titleLocation.setText(names.get(3));
            textLocation.setText(infos.get(3));
            dialog.show();

            buttonBack.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                dialog.dismiss();
              }
            });

            buttonViewOnMap.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                final LatLng BAETULO = new LatLng(latitudes.get(3), longitudes.get(3));
                startActivity(mapsActivityIntent
                        .putExtra("MARKER", BAETULO)
                        .putExtra("NAME", names.get(3)));
              }
            });

            break;

          case 4:

            Glide.with(this).asBitmap().load(imageUrls.get(4)).into(imageLocation);
            titleLocation.setText(names.get(4));
            textLocation.setText(infos.get(4));
            dialog.show();

            buttonBack.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                dialog.dismiss();
              }
            });

            buttonViewOnMap.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                final LatLng BAETULO = new LatLng(latitudes.get(4), longitudes.get(4));
                startActivity(mapsActivityIntent
                        .putExtra("MARKER", BAETULO)
                        .putExtra("NAME", names.get(4)));
              }
            });

            break;

        }

        Log.d(TAG, "onContextItemSelected: click on item");
        return true;

      case 122:

        switch (clickedItemPosition) {
          case 0:
            final LatLng BAETULO = new LatLng(latitudes.get(0), longitudes.get(0));
            startActivity(mapsActivityIntent
                    .putExtra("MARKER", BAETULO)
                    .putExtra("NAME", names.get(0)));
            break;

          case 1:
            final LatLng TERMAS_ROMANAS = new LatLng(latitudes.get(1), longitudes.get(1));
            startActivity(mapsActivityIntent
                    .putExtra("MARKER", TERMAS_ROMANAS)
                    .putExtra("NAME", names.get(1)));
            break;

          case 2:
            final LatLng VILLA_ROMANA = new LatLng(latitudes.get(2), longitudes.get(2));
            startActivity(mapsActivityIntent
                    .putExtra("MARKER", VILLA_ROMANA)
                    .putExtra("NAME", names.get(2)));
            break;

          case 3:
            final LatLng TEMPLO_AUGUSTO = new LatLng(latitudes.get(3), longitudes.get(3));
            startActivity(mapsActivityIntent
                    .putExtra("MARKER", TEMPLO_AUGUSTO)
                    .putExtra("NAME", names.get(3)));
            break;

          case 4:
            final LatLng CUEVAS_TOLL = new LatLng(latitudes.get(4), longitudes.get(4));
            startActivity(mapsActivityIntent
                    .putExtra("MARKER", CUEVAS_TOLL)
                    .putExtra("NAME", names.get(4)));
            break;
        }

        return true;

      default:
        return super.onContextItemSelected(item);
    }

  }

  private void initBuilder() {
    View view = getLayoutInflater().inflate(R.layout.dialog_location, null);
    final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
    initObjects(view);
    builder.setView(view);
    dialog = builder.create();
  }

}

