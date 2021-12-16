package com.example.homify;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class Sensor1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor1);

        WebView webView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();

        webSettings.setJavaScriptEnabled(true);

        webView.loadUrl("https://thingspeak.com/channels/1595381/charts/2?bgcolor=%23ffffff&color=%23d62020&days=10&dynamic=true&median=60&title=Umidit%C3%A0&type=spline&xaxis=Tempo&yaxis=Umidit%C3%A0+%28%25%29");
    }
}