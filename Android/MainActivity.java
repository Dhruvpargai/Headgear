/*
Android Example to connect to and communicate with Bluetooth
In this exercise, the target is a Arduino Due + HC-06 (Bluetooth Module)

Ref:
- Make BlueTooth connection between Android devices
http://android-er.blogspot.com/2014/12/make-bluetooth-connection-between.html
- Bluetooth communication between Android devices
http://android-er.blogspot.com/2014/12/bluetooth-communication-between-android.html
 */
package com.example.dhruvpargai.headgear;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.*;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.util.Log;
import java.io.*;

import android.widget.TextView;
import android.widget.Toast;


//import com.example.androidbtcontrol.R;

import java.util.*;
import java.util.Set;
import java.io.IOException;
import java.io.OutputStream;


public class MainActivity extends Activity {

    Button b1,b2;
    TextView t;
    private BluetoothAdapter BA;
    ListView lv;
    String query="";
    BluetoothDevice mDevice;
    String msg = "Bluetooth ";
    String server_url="http://192.168.1.9:5000/";
//    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1 = (Button) findViewById(R.id.button2);
        t=findViewById(R.id.textView);

        BA = BluetoothAdapter.getDefaultAdapter();
        lv = (ListView) findViewById(R.id.listView);
        if (!BA.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 1);
        }
    }

    public void Connect(View v) {
        Set<BluetoothDevice> pairedDevices = BA.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                mDevice = device;
            }
            ArrayList list = new ArrayList();
            System.out.println("huhgu");
            for(BluetoothDevice bt : pairedDevices) list.add(bt.getName());
            final ArrayAdapter adapter = new  ArrayAdapter(this,android.R.layout.simple_list_item_1, list);
            lv.setAdapter(adapter);

            ConnectThread mConnectThread = new ConnectThread(mDevice);
            mConnectThread.start();
        }
    }
    public void off(View v){
        BA.disable();
        System.out.println("Disconnect");
        Toast.makeText(getApplicationContext(), "Turned off" ,Toast.LENGTH_LONG).show();
    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        private final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
        public ConnectThread(BluetoothDevice device) {
            BluetoothSocket tmp = null;
            mmDevice = device;
            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) { }
            mmSocket = tmp;
        }
        public void run() {
            BA.cancelDiscovery();
            try {
                mmSocket.connect();
                Log.d(msg, "COnnected");
            } catch (IOException connectException) {
                try {
                    mmSocket.close();
                } catch (IOException closeException) { }
                return;
            }
            ConnectedThread mConnectedThread = new ConnectedThread(mmSocket);
            mConnectedThread.start();
        }
        public void cancel() {
            try {
                mmSocket.close();
                System.out.println("Disconnect");
            } catch (IOException e) { }
        }
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private byte[] buffer_ans;
        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }
            mmInStream = tmpIn;
        }
        public void run() {
            byte[] buffer = new byte[1024];
            int begin = 0;
            int bytes = 0;
            Boolean x=true;

            while (true) {
                try {
                    int temp = bytes;
                    bytes += mmInStream.read(buffer, bytes, buffer.length - bytes);
                    buffer_ans = new byte[bytes - temp];
                    for (int i = temp; i < bytes; i++)
                        buffer_ans[i - temp] = buffer[i];
                    query=new String(buffer_ans);
//                    String[] input = new String(buffer_ans).split(" ");
                    t.setText(query);


                }
                catch (IOException e) {
                    break;
                }
            }





//            String output=""
//            for(byte x : buffer) {
//               output+=
//            }
//            System.out.println(output);
        }


    }



}