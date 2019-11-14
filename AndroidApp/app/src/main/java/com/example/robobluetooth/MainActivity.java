package com.example.robobluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

import static android.bluetooth.BluetoothDevice.EXTRA_NAME;

public class MainActivity extends AppCompatActivity {
    Button mButton;
    BluetoothAdapter mBluetoothAdapter;
    Intent btEnable;
    ListView pairedDevices;
    public static String EXTRA_NAME = "device_name";
    public static String EXTRA_ADDRESS = "device_address";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton = findViewById(R.id.buttonPaired);
        pairedDevices = findViewById(R.id.deviceList);
        mBluetoothAdapter= BluetoothAdapter.getDefaultAdapter();
        btEnable = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        btEnable();
        showPairedDevices();
    }

    private void showPairedDevices() {
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("RoboBluetooth","button clicked");
                Set<BluetoothDevice> bt = mBluetoothAdapter.getBondedDevices();
                String[] strings = new String[bt.size()];
                int index = 0;
                if (bt.size()>0){
                    for(BluetoothDevice device: bt){
                        strings[index] = device.getName().trim()+"\n"+device.getAddress().trim();
                        index++;
                    }
                    ArrayAdapter arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,strings);
                    pairedDevices.setAdapter(arrayAdapter);
                    Log.d("RoboBluetooth","array adapter called");
                    pairedDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            String info = (String) ((TextView) view).getText();
                            String name = info.substring(0,info.indexOf("\n"));
                            String address = info.substring(info.length() - 17);
                            Log.d("RoboBluetooth", "Name of device" + name+ " "+ "Address of device"+ address);
                            Intent nextActivity = new Intent(MainActivity.this, Controller.class);

                            //Change the activity.
                            nextActivity.putExtra(EXTRA_ADDRESS, address);
                            nextActivity.putExtra(EXTRA_NAME, name);
                            startActivity(nextActivity);
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1){
            if(resultCode==RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Bluetooth enabled", Toast.LENGTH_LONG).show();
            }
            else if(resultCode==RESULT_CANCELED){
                Toast.makeText(getApplicationContext(), "Bluetooth is disabled", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void btEnable() {
        if (mBluetoothAdapter==null){
            Toast.makeText(getApplicationContext(),"Bluetooth not supported", Toast.LENGTH_LONG).show();
        }
        else{
            if(!mBluetoothAdapter.isEnabled()){
                startActivityForResult(btEnable,1);
            }
        }
    }

}
