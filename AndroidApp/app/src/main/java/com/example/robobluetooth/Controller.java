package com.example.robobluetooth;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class Controller extends AppCompatActivity {
    private Button right, up, left, down, disconnect;
    private String MAC_ADD = null;
    private String device = null;
    private ProgressDialog mProgressDialog;
    private BluetoothAdapter mBluetoothAdapter=null;
    private BluetoothSocket mBluetoothSocket = null;
    private boolean isBTConnected = false;
    static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00905F0B34FB");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);
        right = findViewById(R.id.right_button);
        up = findViewById(R.id.up_button);
        left = findViewById(R.id.left_button);
        down = findViewById(R.id.down_button);
        disconnect = findViewById(R.id.disconnect);
        Intent intent = getIntent();
        MAC_ADD = intent.getStringExtra(MainActivity.EXTRA_ADDRESS);
        device = intent.getStringExtra(MainActivity.EXTRA_NAME);
        new BTConnect().execute();
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mBluetoothSocket!=null){
                    try {
                        mBluetoothSocket.getOutputStream().write(Integer.parseInt("R"));
                    } catch (IOException e) {
                        Log.d("RoboBluetooth","Error sending message");
                    }
                }
            }
        });
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mBluetoothSocket!=null){
                    try {
                        mBluetoothSocket.getOutputStream().write(Integer.parseInt("L"));
                    } catch (IOException e) {
                        Log.d("RoboBluetooth","Error sending message");
                    }
                }
            }
        });
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mBluetoothSocket!=null){
                    try {
                        mBluetoothSocket.getOutputStream().write(Integer.parseInt("F"));
                    } catch (IOException e) {
                        Log.d("RoboBluetooth","Error sending message");
                    }
                }
            }
        });
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mBluetoothSocket!=null){
                    try {
                        mBluetoothSocket.getOutputStream().write(Integer.parseInt("B"));
                    } catch (IOException e) {
                        Log.d("RoboBluetooth","Error sending message");
                    }
                }
            }
        });
        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mBluetoothSocket.close();
                } catch (IOException e) {
                    Log.d("RoboBluetooth","Error");
                }
                Intent intent1 = new Intent(Controller.this, MainActivity.class);
                startActivity(intent1);
            }
        });
    }
    private class BTConnect extends AsyncTask<Void, Void, Void>{
        private boolean connectionSuccessful = true;
        @Override
        protected Void doInBackground(Void... voids) {
            if(mBluetoothSocket == null||isBTConnected) {
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                BluetoothDevice bluetoothDevice = mBluetoothAdapter.getRemoteDevice(MAC_ADD);
                try {
                    mBluetoothSocket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(uuid);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    mBluetoothSocket.connect();
                } catch (IOException e) {
                    connectionSuccessful = false;
                }

            }
            return null;
        }


        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(Controller.this, "Connecting..." +
                    " "+device, "Please wait!!");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(!connectionSuccessful){
                Toast.makeText(getApplicationContext(),"Connection failed",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Controller.this, MainActivity.class);
                startActivity(intent);
            }
            else Toast.makeText(getApplicationContext(),"Bluetooth connected to" + device, Toast.LENGTH_SHORT).show();
            mProgressDialog.dismiss();
        }
    }
}
