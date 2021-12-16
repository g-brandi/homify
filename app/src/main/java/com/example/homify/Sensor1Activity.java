package com.example.homify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Sensor1Activity extends AppCompatActivity {


    private Button btnStampa;
    private List<DataTempHum> dataTempHumList;
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://homify-is07-default-rtdb.europe-west1.firebasedatabase.app/");
    DatabaseReference dataReference = database.getReference("data/"+ FirebaseAuth.getInstance().getCurrentUser().getUid().toString().trim());



    @Override
    protected void onStart() {

        super.onStart();
        dataReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshotDTH : snapshot.getChildren()){
                    dataTempHumList.clear();
                    DataTempHum dataTempHum=snapshotDTH.getValue(DataTempHum.class);
                    dataTempHumList.add(dataTempHum);
                }
                dataTempHumList.get(dataTempHumList.size()-1).printDTH();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor1);

        System.out.println(dataReference.toString());
        dataTempHumList = new ArrayList<>();
        btnStampa = findViewById(R.id.btnstampa);
        btnStampa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < dataTempHumList.size(); i++)
                    dataTempHumList.get(i).printDTH();
            }
        });

        dataReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshotDTH : snapshot.getChildren()){
                    DataTempHum dataTempHum=snapshotDTH.getValue(DataTempHum.class);
                    dataTempHum.printDTH();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        System.out.println("FINE PRIMO CARICAMENTO!");

        WebView webView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.loadUrl("https://thingspeak.com/channels/1595381/charts/1?bgcolor=%23ffffff&color=%23d62020&days=10&dynamic=true&median=60&round=1&title=Temperatura&type=spline&xaxis=Tempo&yaxis=Temperatura+%28%C2%B0C%29");
    }

}