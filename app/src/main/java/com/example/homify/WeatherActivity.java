package com.example.homify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class WeatherActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        //Hooks//
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        toolbar=findViewById(R.id.toolbar);

        //Tool Bar//
        setSupportActionBar(toolbar);

        // Navigation Drawer Menu//
        //Menu menu=navigationView.getMenu();
        //menu.findItem(R.id.nav_logout).setVisible(false);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_meteo);
    }


    @Override
    public void onBackPressed() {

        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
        super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Intent intentsveglia = new Intent(WeatherActivity.this, AlarmClockActivity.class);
        Intent intentmeteo = new Intent(WeatherActivity.this, WeatherActivity.class);
        Intent intenttemperatura = new Intent(WeatherActivity.this, Sensor1Activity.class);
        Intent intentUmidita = new Intent(WeatherActivity.this, Sensor2Activity.class);
        Intent intentHome = new Intent(WeatherActivity.this, UserHomeActivity.class);
        Intent intentProfilo = new Intent(WeatherActivity.this, ProfileActivity.class);
        Intent intentLogOut = new Intent(WeatherActivity.this, MainActivity.class);
        Intent intentImpostazioni = new Intent(WeatherActivity.this, SettingsActivity.class);
        Intent intentGrafici = new Intent(WeatherActivity.this, GraphsActivity.class);

        if (menuItem.getItemId() == R.id.nav_home) {
            startActivity(intentHome);
        } else if (menuItem.getItemId() == R.id.nav_sveglia) {
            startActivity(intentsveglia);
        } else if (menuItem.getItemId() == R.id.nav_meteo) {
            startActivity(intentmeteo);
        } else if (menuItem.getItemId() == R.id.nav_temperatura) {
            startActivity(intenttemperatura);
        } else if (menuItem.getItemId() == R.id.nav_umidita) {
            startActivity(intentUmidita);
        } else if (menuItem.getItemId() == R.id.nav_profile) {
            startActivity(intentProfilo);
        } else if (menuItem.getItemId() == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(intentLogOut);
        } else if (menuItem.getItemId() == R.id.nav_impostazioni) {
            startActivity(intentImpostazioni);
        } else if (menuItem.getItemId() == R.id.nav_grafici) {
            startActivity(intentGrafici);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}