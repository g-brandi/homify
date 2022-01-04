package com.example.homify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androdocs.httprequest.HttpRequest;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WeatherActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    String CITY;
    String API = "7144058e5c94b539cf4543138ab547da";
    ImageView search;
    EditText etCity;
    TextView city,country,time,temp,forecast,humidity,min_temp,max_temp,sunrises,sunsets;

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

        etCity = (EditText) findViewById(R.id.Your_city);
        search = (ImageView) findViewById(R.id.search);
        // CALL ALL ANSWERS :
        city = (TextView) findViewById(R.id.city);
        country = (TextView) findViewById(R.id.country);
        time = (TextView) findViewById(R.id.time);
        temp = (TextView) findViewById(R.id.temp);
        forecast = (TextView) findViewById(R.id.forecast);
        humidity = (TextView) findViewById(R.id.humidity);
        min_temp = (TextView) findViewById(R.id.min_temp);
        max_temp = (TextView) findViewById(R.id.max_temp);
        sunrises = (TextView) findViewById(R.id.sunrises);
        sunsets = (TextView) findViewById(R.id.sunsets);

        // CLICK ON SEARCH BUTTON :
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CITY = etCity.getText().toString();
                new weatherTask().execute();
            }
        });
}

    class weatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... args) {
            String response = HttpRequest.excuteGet("https://api.openweathermap.org/data/2.5/weather?q=" + CITY + "&units=metric&appid=" + API);
            return response;
        }
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObj = new JSONObject(result);
                JSONObject main = jsonObj.getJSONObject("main");
                JSONObject weather = jsonObj.getJSONArray("weather").getJSONObject(0);
                JSONObject sys = jsonObj.getJSONObject("sys");
                // CALL VALUE IN API :
                String city_name = jsonObj.getString("name");
                String countryname = sys.getString("country");
                Long updatedAt = jsonObj.getLong("dt");
                String updatedAtText = "Last Updated at: " + new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault()).format(new Date(updatedAt * 1000));
                String temperature = main.getString("temp");
                String cast = weather.getString("description");
                String humi_dity = main.getString("humidity");
                String temp_min = main.getString("temp_min");
                String temp_max = main.getString("temp_max");
                Long rise = sys.getLong("sunrise");
                String sunrise = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date(rise * 1000));
                Long set = sys.getLong("sunset");
                String sunset = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date(set * 1000));
                // SET ALL VALUES IN TEXTBOX :
                city.setText(city_name);
                country.setText(countryname);
                time.setText(updatedAtText);
                temp.setText(temperature + "°C");
                forecast.setText(cast);
                humidity.setText(humi_dity);
                min_temp.setText(temp_min);
                max_temp.setText(temp_max);
                sunrises.setText(sunrise);
                sunsets.setText(sunset);
            } catch (Exception e) {
                Toast.makeText(WeatherActivity.this, "Error:" + e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
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