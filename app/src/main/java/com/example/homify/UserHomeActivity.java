package com.example.homify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class UserHomeActivity<OnClick> extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    RelativeLayout imageSveglia;
    RelativeLayout imageMeteo;
    RelativeLayout imageTemperatura;
    RelativeLayout imageUmidita;
    RelativeLayout imageGrafici;
    RelativeLayout imageImpostazioni;

    TextView txtTips;
    private String tip="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        //Hooks//
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        //BOTTONI
        imageSveglia = findViewById(R.id.layout_sveglia);
        imageMeteo = findViewById(R.id.layout_meteo);
        imageTemperatura = findViewById(R.id.layout_temperatura);
        imageUmidita = findViewById(R.id.layout_umidita);
        imageGrafici = findViewById(R.id.layout_grafici);
        imageImpostazioni = findViewById(R.id.layout_impostazioni);

        //BOTTONE SVEGLIA
        imageSveglia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // definisco le intenzioni
                Intent openSveglia = new Intent(UserHomeActivity.this, AlarmClockActivity.class);
                // passo all'attivazione dell'activity Pagina.java
                startActivity(openSveglia);
            }
        });

        //BOTTONE METEO
        imageMeteo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openMeteo = new Intent(UserHomeActivity.this, WeatherActivity.class);
                startActivity(openMeteo);
            }
        });

        //BOTTONE TEMPERATURA
        imageTemperatura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openTemperatura = new Intent(UserHomeActivity.this, Sensor1Activity.class);
                startActivity(openTemperatura);
            }
        });

        //BOTTONE UMIDITA
        imageUmidita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openUmidita = new Intent(UserHomeActivity.this, Sensor2Activity.class);
                startActivity(openUmidita);
            }
        });

        //BOTTONE GRAFICI
        imageGrafici.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openGrafici = new Intent(UserHomeActivity.this, GraphsActivity.class);
                startActivity(openGrafici);
            }
        });

        //BOTTONE IMPOSTAZIONI
        imageImpostazioni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openImpostazioni = new Intent(UserHomeActivity.this, SettingsActivity.class);
                startActivity(openImpostazioni);
            }
        });

        //SUGGERIMENTI
        txtTips=findViewById(R.id.txtTips);
        FirebaseDatabase.getInstance("https://homify-is07-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("data/"+ FirebaseAuth.getInstance().getCurrentUser().getUid().toString().trim())
                .orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren())
                {
                    int lastTemperature = data.getValue(DHT.class).getTemperature();
                    System.out.println(lastTemperature);
                    if (lastTemperature<20){
                        tip="Ci sono "+lastTemperature+" gradi, inizia a far freddo!";
                        if (lastTemperature<15){
                            tip="Ci sono "+lastTemperature+" gradi, vuoi accendere il riscaldamento?";
                            if (lastTemperature<10){
                                tip="Ci sono "+lastTemperature+" gradi, per favore riscaldati!";
                            }
                        }
                    }
                    if (lastTemperature>30){
                        tip="Ci sono "+lastTemperature+" gradi, ricordati di idratarti.";
                        if (lastTemperature>35){
                            tip="Ci sono "+lastTemperature+" gradi, fa molto caldo, vuoi accendere il ventilatore?";
                            if (lastTemperature>40){
                                tip="Ci sono "+lastTemperature+" gradi, hai fatto rifornimento di ghiaccio?";
                            }
                        }

                    }
                    if (lastTemperature>=20 && lastTemperature <=30){
                        tip="Ci sono ben "+lastTemperature+" gradi.";
                    }
                    txtTips.setText(tip);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Tool Bar//
        setSupportActionBar(toolbar);

        // Navigation Drawer Menu//


        //Nascondi elementi menu
        //Menu menu=navigationView.getMenu();
        //menu.findItem(R.id.nav_logout).setVisible(false);


        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_home);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_home:
                break;
            case R.id.nav_sveglia:
                Intent intentsveglia=new Intent(UserHomeActivity.this,AlarmClockActivity.class);
                startActivity(intentsveglia);
                break;
            case R.id.nav_temperatura:
                Intent intenttemperatura=new Intent(UserHomeActivity.this,Sensor1Activity.class);
                startActivity(intenttemperatura);
                break;
            case R.id.nav_meteo:
                Intent intentmeteo=new Intent(UserHomeActivity.this,WeatherActivity.class);
                startActivity(intentmeteo);
                break;
            case R.id.nav_umidita:
                Intent intentumidita=new Intent(UserHomeActivity.this,Sensor2Activity.class);
                startActivity(intentumidita);
                break;
            case R.id.nav_profile:
                Intent intentprofilo=new Intent(UserHomeActivity.this,ProfileActivity.class);
                startActivity(intentprofilo);
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(UserHomeActivity.this,MainActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_impostazioni:
                Intent intentimpostazioni=new Intent(UserHomeActivity.this,SettingsActivity.class);
                startActivity(intentimpostazioni);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}