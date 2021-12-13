package com.example.homify;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class UserHomeActivity extends AppCompatActivity {

    // dichiarazione variabili per Bluetooth
    public UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    BluetoothAdapter mBluetoothAdapter=null;
    BluetoothSocket mmSocket=null;
    BluetoothDevice mmDevice=null;
    OutputStream outStream;
    private ToggleButton tgbBluetooth;

    // dichiarazione variabili per logout
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);


        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(UserHomeActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });


        // Bluetooth
        tgbBluetooth = findViewById(R.id.tgbBluetooth);
        // evento: tap sul togglebutton per la connessione del bluetooth
        tgbBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tgbBluetooth.isChecked()) { // controlla che sia attivo il toogle button
                    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (mBluetoothAdapter != null) {
                        //control that bluetooth is enabled
                        if (mBluetoothAdapter.isEnabled()) {
                            mmDevice = mBluetoothAdapter.getRemoteDevice("MAC"); // TODO: MAC address del bluetooth di arduino da inserire
                            try {
                                //bluetooth connection
                                mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
                                mmSocket.connect();
                                outStream = mmSocket.getOutputStream();
                                Toast.makeText(UserHomeActivity.this, "ON", Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                tgbBluetooth.setChecked(false);
                                Log.i("Bluetooth",e.toString());
                                try {
                                    //try to close bluetooth connection
                                    mmSocket.close();
                                } catch (IOException ceXC) {
                                }
                                Toast.makeText(UserHomeActivity.this, "Bluetooth isn't connect", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(UserHomeActivity.this, "Bluetooth isn't enabled", Toast.LENGTH_LONG).show();
                            tgbBluetooth.setChecked(false);
                        }
                    } //close mBluetoothAdapter!=null
                } else {
                    try {
                        //try to close socket connections
                        outStream.close();
                        mmSocket.close();
                    } catch (IOException ceXC) {}
                }
            }
        });

    }

    //funzione per scrivere nella output del bluetooth
    private void outMessage(String message) {
        if (outStream == null) {
            return;
        }
        byte[] msgBuffer = message.getBytes();
        try{
            outStream.write(msgBuffer);
        } catch (IOException e){
            Toast.makeText(UserHomeActivity.this, "Messaggio non Inviato", Toast.LENGTH_SHORT).show();
        }
    }
}