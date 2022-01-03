package com.example.homify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkSuggestion;
import android.net.wifi.hotspot2.PasspointConfiguration;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class WebViewActivity extends AppCompatActivity {

    private WebView webViewConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        // Recupera lo user ID dell'utente
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

        // Copia lo user ID negli appunti del dispositivo
        android.content.ClipboardManager clipboard =  (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", userID);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getApplicationContext(), "User ID copiato negli appunti", Toast.LENGTH_SHORT).show();

        // Mostra la pagina di configurazione
        // (ovviamente bisogna essere connessi alla rete del microcontrollore)
        webViewConfig = findViewById(R.id.webViewConfig);
        webViewConfig.setWebViewClient(new WebViewClient());
        webViewConfig.getSettings().setJavaScriptEnabled(true);
        webViewConfig.loadUrl("192.168.4.1");

    }

}