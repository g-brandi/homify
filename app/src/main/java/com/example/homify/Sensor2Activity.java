package com.example.homify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Sensor2Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Button btnRefresh;
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://homify-is07-default-rtdb.europe-west1.firebasedatabase.app/");
    DatabaseReference dataReference = database.getReference("data/"+ FirebaseAuth.getInstance().getCurrentUser().getUid().toString().trim());

    private LineChart mChart;
    private ArrayList<Entry> yValues;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("OnStart() avviato");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor2);

        ///////////////////////////////////////////////////////////////////////////
        //Hooks//
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        toolbar=findViewById(R.id.toolbar);

        //Tool Bar//
        setSupportActionBar(toolbar);

        //Navigation Drawer Menu//
        //Menu menu=navigationView.getMenu();
        //menu.findItem(R.id.nav_logout).setVisible(false);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_umidita);

        ////////////////////////////////////////////////////////////////////////////

        btnRefresh=findViewById(R.id.btnSensor2Refresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshGraph();
            }
        });

        mChart = (LineChart) findViewById(R.id.linechart2);
        mChart.setBackgroundColor(Color.WHITE);
        mChart.getDescription().setEnabled(false);
        mChart.setDrawGridBackground(false);
        mChart.getAxisRight().setDrawGridLines(false);
        mChart.getAxisLeft().setDrawGridLines(false);
        mChart.getXAxis().setDrawGridLines(false);
        mChart.setScaleMinima(3f,1f);

        firstSettings();


    }

    private void refreshGraph() {
    }

    private void firstSettings(){
        yValues=new ArrayList<>();
        dataReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // ciclo for per prendere il database e creare gli oggetti nella lista
                for (DataSnapshot snapshotDHT : snapshot.getChildren()){
                    DHT dht = snapshotDHT.getValue(DHT.class);
                    if (dht != null)
                        yValues.add(new Entry(dht.setTimeAsSeconds(),dht.getHumidity()));
                }
                // uscito dal for ha preso tutti i figli

                //dati per il grafico
                LineDataSet setHumidity=new LineDataSet(yValues, "Umidità registrata");
                setHumidity.setDrawFilled(true);
                if (Utils.getSDKInt() >= 18) {
                    // fill drawable only supported on api level 18 and above
                    Drawable drawable = ContextCompat.getDrawable(Sensor2Activity.this, R.drawable.chart_background_gradient);
                    setHumidity.setFillDrawable(drawable);
                }
                else {
                    setHumidity.setFillColor(Color.parseColor("#FF9700"));
                }

                setHumidity.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
                setHumidity.setCubicIntensity(0.2f);
                setHumidity.setValueTextSize(0f);
                setHumidity.setColor(Color.MAGENTA);
                setHumidity.setDrawVerticalHighlightIndicator(false);
                setHumidity.setLineWidth(3f);
                setHumidity.setDrawCircles(false);
                setHumidity.setDrawValues(false);

                ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                dataSets.add(setHumidity);
                LineData data = new LineData(dataSets);
                mChart.setData(data);
                mChart.getLegend().setEnabled(false);
                mChart.setDrawMarkers(false);
                mChart.moveViewToX(data.getXMax());
                mChart.getXAxis().setValueFormatter(new LineChartXAxisValueFormatter());
                mChart.invalidate();
                System.out.println("Primo caricamento");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
        Intent intentsveglia = new Intent(Sensor2Activity.this, AlarmClockActivity.class);
        Intent intentmeteo = new Intent(Sensor2Activity.this, WeatherActivity.class);
        Intent intenttemperatura = new Intent(Sensor2Activity.this, Sensor1Activity.class);
        Intent intentUmidita = new Intent(Sensor2Activity.this, Sensor2Activity.class);
        Intent intentHome = new Intent(Sensor2Activity.this, UserHomeActivity.class);
        Intent intentProfilo = new Intent(Sensor2Activity.this, ProfileActivity.class);
        Intent intentLogOut = new Intent(Sensor2Activity.this, MainActivity.class);
        Intent intentImpostazioni = new Intent(Sensor2Activity.this, SettingsActivity.class);
        Intent intentGrafici = new Intent(Sensor2Activity.this, GraphsActivity.class);

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