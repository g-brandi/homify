package com.example.homify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    // dichiarazione variabili per Bluetooth
//    public UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
//    BluetoothAdapter mBluetoothAdapter=null;
//    BluetoothSocket mmSocket=null;
//    BluetoothDevice mmDevice=null;
//    OutputStream outStream;
//    private ToggleButton tgbBluetooth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Hooks//
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        toolbar=findViewById(R.id.toolbar);

        //Tool Bar//
        setSupportActionBar(toolbar);

        // Navigation Drawer Menu//
        Menu menu=navigationView.getMenu();

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_impostazioni);

        // Bluetooth
//        tgbBluetooth = findViewById(R.id.tgbBluetooth);
//        // evento: tap sul togglebutton per la connessione del bluetooth
//        tgbBluetooth.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (tgbBluetooth.isChecked()) { // controlla che sia attivo il toogle button
//                    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//                    if (mBluetoothAdapter != null) {
//                        //control that bluetooth is enabled
//                        if (mBluetoothAdapter.isEnabled()) {
//                            mmDevice = mBluetoothAdapter.getRemoteDevice("MAC"); // TODO: MAC address del bluetooth di arduino da inserire
//                            try {
//                                //bluetooth connection
//                                mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
//                                mmSocket.connect();
//                                outStream = mmSocket.getOutputStream();
//                                Toast.makeText(UserHomeActivity.this, "ON", Toast.LENGTH_SHORT).show();
//                            } catch (IOException e) {
//                                tgbBluetooth.setChecked(false);
//                                Log.i("Bluetooth",e.toString());
//                                try {
//                                    //try to close bluetooth connection
//                                    mmSocket.close();
//                                } catch (IOException ceXC) {
//                                }
//                                Toast.makeText(UserHomeActivity.this, "Bluetooth isn't connect", Toast.LENGTH_SHORT).show();
//                            }
//                        } else {
//                            Toast.makeText(UserHomeActivity.this, "Bluetooth isn't enabled", Toast.LENGTH_LONG).show();
//                            tgbBluetooth.setChecked(false);
//                        }
//                    } //close mBluetoothAdapter!=null
//                } else {
//                    try {
//                        //try to close socket connections
//                        outStream.close();
//                        mmSocket.close();
//                    } catch (IOException ceXC) {}
//                }
//            }
//        });
    }

    // funzione per scrivere nella output del bluetooth
//    private void outMessage(String message) {
//        if (outStream == null) {
//            return;
//        }
//        byte[] msgBuffer = message.getBytes();
//        try{
//            outStream.write(msgBuffer);
//        } catch (IOException e){
//            Toast.makeText(UserHomeActivity.this, "Messaggio non Inviato", Toast.LENGTH_SHORT).show();
//        }
//    }


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
        Intent intentsveglia = new Intent(SettingsActivity.this, AlarmClockActivity.class);
        Intent intentmeteo = new Intent(SettingsActivity.this, WeatherActivity.class);
        Intent intenttemperatura = new Intent(SettingsActivity.this, Sensor1Activity.class);
        Intent intentUmidita = new Intent(SettingsActivity.this, Sensor2Activity.class);
        Intent intentHome = new Intent(SettingsActivity.this, UserHomeActivity.class);
        Intent intentProfilo = new Intent(SettingsActivity.this, ProfileActivity.class);
        Intent intentLogOut = new Intent(SettingsActivity.this, MainActivity.class);
        Intent intentImpostazioni = new Intent(SettingsActivity.this, SettingsActivity.class);
        Intent intentGrafici = new Intent(SettingsActivity.this, GraphsActivity.class);

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