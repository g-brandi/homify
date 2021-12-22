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

public class GraphsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Button btnRefresh;
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://homify-is07-default-rtdb.europe-west1.firebasedatabase.app/");
    DatabaseReference dataReference = database.getReference("data/"+ FirebaseAuth.getInstance().getCurrentUser().getUid().toString().trim());

    private LineChart mChartH;
    private ArrayList<Entry> yValuesH;
    private LineChart mChartT;
    private ArrayList<Entry> yValuesT;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphs);

        ///////////////////////////////////////////////////////////////////////////////////
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

        navigationView.setCheckedItem(R.id.nav_grafici);


        /////////////////////////////////////////////////////////////////////////////

        btnRefresh=findViewById(R.id.btnGraphRefresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshGraph();
            }
        });

        mChartH = (LineChart) findViewById(R.id.linechartH);
        mChartH.setBackgroundColor(Color.WHITE);
        mChartH.getDescription().setEnabled(false);
        mChartH.setDrawGridBackground(false);
        mChartH.getAxisRight().setDrawGridLines(false);
        mChartH.getAxisLeft().setDrawGridLines(false);
        mChartH.getXAxis().setDrawGridLines(false);
        mChartH.setScaleMinima(3f,1f);

        mChartT = (LineChart) findViewById(R.id.linechartT);
        mChartT.setBackgroundColor(Color.WHITE);
        mChartT.getDescription().setEnabled(false);
        mChartT.setDrawGridBackground(false);
        mChartT.getAxisRight().setDrawGridLines(false);
        mChartT.getAxisLeft().setDrawGridLines(false);
        mChartT.getXAxis().setDrawGridLines(false);
        mChartT.setScaleMinima(3f,1f);

        firstSettingsHumidity();
        firstSettingsTemperature();



    }

    private void refreshGraph() {
    }

    private void firstSettingsHumidity(){
        yValuesH=new ArrayList<>();
        dataReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // ciclo for per prendere il database e creare gli oggetti nella lista
                for (DataSnapshot snapshotDHT : snapshot.getChildren()){
                    DHT dht = snapshotDHT.getValue(DHT.class);
                    if (dht != null)
                        yValuesH.add(new Entry(dht.setTimeAsSeconds(),dht.getHumidity()));
                }
                // uscito dal for ha preso tutti i figli

                //dati per il grafico
                LineDataSet setHumidity=new LineDataSet(yValuesH, "Umidità registrata");
                setHumidity.setDrawFilled(true);
                if (Utils.getSDKInt() >= 18) {
                    // fill drawable only supported on api level 18 and above
                    Drawable drawable = ContextCompat.getDrawable(GraphsActivity.this, R.drawable.chart_background_gradient);
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
                mChartH.setData(data);
                mChartH.getLegend().setEnabled(false);
                mChartH.setDrawMarkers(false);
                mChartH.moveViewToX(data.getXMax());
                mChartH.getXAxis().setValueFormatter(new LineChartXAxisValueFormatter());
                mChartH.invalidate();
                System.out.println("Primo caricamento H");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void firstSettingsTemperature(){
        yValuesT=new ArrayList<>();
        dataReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // ciclo for per prendere il database e creare gli oggetti nella lista
                for (DataSnapshot snapshotDHT : snapshot.getChildren()){
                    DHT dht = snapshotDHT.getValue(DHT.class);
                    if (dht != null)
                        yValuesT.add(new Entry(dht.setTimeAsSeconds(),dht.getTemperature()));
                }
                // uscito dal for ha preso tutti i figli

                //dati per il grafico
                LineDataSet setTemperature=new LineDataSet(yValuesT, "Temperatura registrata");
                setTemperature.setDrawFilled(true);
                if (Utils.getSDKInt() >= 18) {
                    // fill drawable only supported on api level 18 and above
                    Drawable drawable = ContextCompat.getDrawable(GraphsActivity.this, R.drawable.chart_background_gradient);
                    setTemperature.setFillDrawable(drawable);
                }
                else {
                    setTemperature.setFillColor(Color.parseColor("#FF9700"));
                }
                setTemperature.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
                setTemperature.setCubicIntensity(0.2f);
                setTemperature.setValueTextSize(0f);
                setTemperature.setColor(Color.MAGENTA);
                setTemperature.setDrawVerticalHighlightIndicator(false);
                setTemperature.setLineWidth(3f);
                setTemperature.setDrawCircles(false);
                setTemperature.setDrawValues(false);
                ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                dataSets.add(setTemperature);
                LineData data = new LineData(dataSets);
                mChartT.setData(data);
                mChartT.getLegend().setEnabled(false);
                mChartT.setDrawMarkers(false);
                mChartT.moveViewToX(data.getXMax());
                mChartT.getXAxis().setValueFormatter(new LineChartXAxisValueFormatter());
                mChartT.invalidate();
                System.out.println("Primo caricamento T");


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
        Intent intentsveglia = new Intent(GraphsActivity.this, AlarmClockActivity.class);
        Intent intentmeteo = new Intent(GraphsActivity.this, WeatherActivity.class);
        Intent intenttemperatura = new Intent(GraphsActivity.this, Sensor1Activity.class);
        Intent intentUmidita = new Intent(GraphsActivity.this, Sensor2Activity.class);
        Intent intentHome = new Intent(GraphsActivity.this, UserHomeActivity.class);
        Intent intentProfilo = new Intent(GraphsActivity.this, ProfileActivity.class);
        Intent intentLogOut = new Intent(GraphsActivity.this, MainActivity.class);
        Intent intentImpostazioni = new Intent(GraphsActivity.this, SettingsActivity.class);
        Intent intentGrafici = new Intent(GraphsActivity.this, GraphsActivity.class);

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