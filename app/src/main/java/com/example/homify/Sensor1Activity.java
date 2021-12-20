package com.example.homify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Sensor1Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private Button btnStampa;
    private List<DataTempHum> dataTempHumList;
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://homify-is07-default-rtdb.europe-west1.firebasedatabase.app/");
    DatabaseReference dataReference = database.getReference("data/"+ FirebaseAuth.getInstance().getCurrentUser().getUid().toString().trim());

    private int yCount;
    private LineChart mChart;
    private ArrayList<Entry> yValues;
    private boolean isFetchingDone;
    private int colorFill = Color.argb(255,6,214,160);
    private int colorFill2 = Color.argb(200,6,214,160);

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("MY HEART WILL GO ON START");
        /*dataReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshotDTH : snapshot.getChildren()){
                    dataTempHumList.clear();
                    DataTempHum dataTempHum=snapshotDTH.getValue(DataTempHum.class);
                    dataTempHumList.add(dataTempHum);
                    yValues.add(new Entry((float)yCount, (float) dataTempHum.getTemperature()));
                    mChart.invalidate();
                    yCount=yCount++;
                    System.out.println(yCount);
                }
                dataTempHumList.get(dataTempHumList.size()-1).printDTH();
                System.out.println("NEEEEEEEEEEEEEEEEEEEW DATAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
                yValues.add(new Entry((float) yCount,(float) dataTempHumList.get(dataTempHumList.size()-1).getTemperature()));
                mChart.invalidate();
                System.out.println("DISEGNOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

         */
        createChart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor1);

        isFetchingDone=false;

        System.out.println(dataReference.toString());
        dataTempHumList = new ArrayList<>();
        btnStampa = findViewById(R.id.btnstampa);
        btnStampa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < dataTempHumList.size(); i++){
                    dataTempHumList.get(i).printDTH();}
                createChart();
            }
        });

        System.out.println("CALMA FAMMI INIZIARE!!!!!!!!!!!!");

        yValues=new ArrayList<>();
        yCount=0;

        dataReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshotDTH : snapshot.getChildren()){
                    DataTempHum dataTempHum=snapshotDTH.getValue(DataTempHum.class);
                    //dataTempHum.printDTH();
                    yValues.add(new Entry((float) yCount,(float) dataTempHum.getTemperature()));
                    yCount++;
                }
                isFetchingDone=true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        System.out.println("FINE PRIMO CARICAMENTO!!!!!!!!!!! SI VEDE?????????????????????");

//        WebView webView = (WebView) findViewById(R.id.webview);
//        WebSettings webSettings = webView.getSettings();
//        webSettings.setJavaScriptEnabled(true);
//        webView.loadUrl("https://thingspeak.com/channels/1595381/charts/1?bgcolor=%23ffffff&color=%23d62020&days=10&dynamic=true&median=60&round=1&title=Temperatura&type=spline&xaxis=Tempo&yaxis=Temperatura+%28%C2%B0C%29");

        System.out.println("è UN COLORE UN POOOOOO..... MOSCIOO.........");

        mChart = (LineChart) findViewById(R.id.linechart);
//        mChart.setOnChartGestureListener(Sensor1Activity.this);
//        mChart.setOnChartValueSelectedListener(Sensor1Activity.this);

        mChart.setTouchEnabled(true);

        mChart.getXAxis().setDrawGridLines(false);
        mChart.getXAxis().setDrawGridLinesBehindData(false);
        mChart.getXAxis().setDrawLabels(false);
        mChart.setScaleMinima(10f,1f);
        mChart.moveViewToX((float) yCount);
        mChart.getXAxis().setDrawAxisLine(false);
        System.out.println("TI PREGO DIMMI CHE HAI SCARICATO I DATI");

        createChart();

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

        navigationView.setCheckedItem(R.id.nav_temperatura);
    }

    public void createChart(){
        if(isFetchingDone==true){

            LineDataSet set1= new LineDataSet(yValues,"Temperatura registrata");

            set1.setDrawIcons(false);
            set1.setColor(colorFill);
            set1.setCircleColor(colorFill);
            set1.setLineWidth(3f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(0f);
            set1.setDrawFilled(true);
            set1.setFormLineWidth(1f);
            set1.setFillColor(colorFill2);
            set1.setCubicIntensity(0.8f);

            //TODO FORMATTARE IL GRAFICO, IN PARTICOLARE SI DEVONO VEDERE GLI ULTIMI RECORD, MAGARI SI RIESCE A FALRO DIVENTARE PIU STONDATO E SE ARRIVA NATALE ANCHE LA SFUMATURA AL BACKGROUND, TOMBOLA CON LINEE DISATTIVATE TOTALMENTE

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            LineData data = new LineData(dataSets);

            mChart.setData(data);
            mChart.invalidate();
            System.out.println("L MURT TU M VUE DISC CHE SI SCARCAT");
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
        Intent intentsveglia = new Intent(Sensor1Activity.this, AlarmClockActivity.class);
        Intent intentmeteo = new Intent(Sensor1Activity.this, WeatherActivity.class);
        Intent intenttemperatura = new Intent(Sensor1Activity.this, Sensor1Activity.class);
        Intent intentUmidita = new Intent(Sensor1Activity.this, Sensor2Activity.class);
        Intent intentHome = new Intent(Sensor1Activity.this, UserHomeActivity.class);
        Intent intentProfilo = new Intent(Sensor1Activity.this, ProfileActivity.class);
        Intent intentLogOut = new Intent(Sensor1Activity.this, MainActivity.class);
        Intent intentImpostazioni = new Intent(Sensor1Activity.this, SettingsActivity.class);
        Intent intentGrafici = new Intent(Sensor1Activity.this, GraphsActivity.class);

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