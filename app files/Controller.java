package com.android.hot_wheel;

import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;


public class Controller extends ActionBarActivity {

    Button btnDis;
    ImageView fd,bd,fl,fr,bl,br,sl,sr,zerospeed,zerosteer,myimage_view;
    String address = null;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    public static String EXTRA_DATA = "Data";


    //SPP UUID. Look for it
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Intent newint = getIntent();
        address = newint.getStringExtra(DeviceList.EXTRA_ADDRESS); //receive the address of the bluetooth device


        //view of the ledControl
        setContentView(R.layout.activity_controller);

        myimage_view = (ImageView)findViewById(R.id.background_image);
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        Drawable wallpaperDrawable = wallpaperManager.getDrawable();
        myimage_view.setImageDrawable(wallpaperDrawable);


        //call the widgtes
        fd = (ImageView)findViewById(R.id.forward);
        bd = (ImageView)findViewById(R.id.backward);
        sl = (ImageView)findViewById(R.id.left);
        sr = (ImageView)findViewById(R.id.right);

        fl = (ImageView)findViewById(R.id.forwardleft);
        fr = (ImageView)findViewById(R.id.forwardright);
        bl = (ImageView)findViewById(R.id.backwardleft);
        br = (ImageView)findViewById(R.id.backwardright);

        zerospeed = (ImageView)findViewById(R.id.zerospeed);
        zerosteer = (ImageView)findViewById(R.id.zerosteer);

        btnDis = (Button)findViewById(R.id.button_dis);

        new ConnectBT().execute(); //Call the class to connect

        //commands to be sent to bluetooth
        fd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (btSocket!=null)
                {
                    try
                    {
                        btSocket.getOutputStream().write("#f".toString().getBytes());
                    }
                    catch (IOException e)
                    {
                        msg("Error");
                    }
                }
            }
        });

        bd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btSocket != null) {
                    try {
                        btSocket.getOutputStream().write("#b".toString().getBytes());
                    } catch (IOException e) {
                        msg("Error");
                    }
                }
            }
        });
        sl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (btSocket!=null)
                {
                    try
                    {
                        btSocket.getOutputStream().write("#l".toString().getBytes());
                    }
                    catch (IOException e)
                    {
                        msg("Error");
                    }
                }
            }
        });
        sr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btSocket != null) {
                    try {
                        btSocket.getOutputStream().write("#r".toString().getBytes());
                    } catch (IOException e) {
                        msg("Error");
                    }
                }
            }
        });


        fl.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (btSocket!=null)
                {
                    try
                    {
                        btSocket.getOutputStream().write("#fl".toString().getBytes());
                    }
                    catch (IOException e)
                    {
                        msg("Error");
                    }
                }
            }
        });

        fr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (btSocket!=null)
                {
                    try
                    {
                        btSocket.getOutputStream().write("#fr".toString().getBytes());
                    }
                    catch (IOException e)
                    {
                        msg("Error");
                    }
                }
            }
        });
        bl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (btSocket!=null)
                {
                    try
                    {
                        btSocket.getOutputStream().write("#bl".toString().getBytes());
                    }
                    catch (IOException e)
                    {
                        msg("Error");
                    }
                }
            }
        });
        br.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btSocket != null) {
                    try {
                        btSocket.getOutputStream().write("#br".toString().getBytes());
                    } catch (IOException e) {
                        msg("Error");
                    }
                }
            }
        });

        zerospeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btSocket != null) {
                    try {
                        btSocket.getOutputStream().write("#zd".toString().getBytes());
                    } catch (IOException e) {
                        msg("Error");
                    }
                }
            }
        });
        zerosteer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btSocket != null) {
                    try {
                        btSocket.getOutputStream().write("#zr".toString().getBytes());
                    } catch (IOException e) {
                        msg("Error");
                    }
                }
            }
        });




        btnDis.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Disconnect(); //close connection
            }
        });



    }

    private void Disconnect()
    {
        if (btSocket!=null) //If the btSocket is busy
        {
            try
            {
                btSocket.close(); //close connection
            }
            catch (IOException e)
            { msg("Error");}
        }
        finish(); //return to the first layout

    }

    private void sendbt(String x)
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write(x.toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }







    // fast way to call Toast
    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_controller, menu);
        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_about_maker) {
            Toast.makeText(getBaseContext(), "Ashutosh Mishra", Toast.LENGTH_SHORT).show();
            Toast.makeText(getBaseContext(), "MAIT/IP Univ.", Toast.LENGTH_SHORT).show();
            Toast.makeText(getBaseContext(), "ashutosh.m812@gmail.com", Toast.LENGTH_SHORT).show();
        }

        else if (id == R.id.action_lfr)
        {
            if (btSocket!=null)
            {
                try
                {
                    btSocket.getOutputStream().write("#lfr".toString().getBytes());
                }
                catch (IOException e)
                {
                    msg("Error");
                }
            }

        }

        return super.onOptionsItemSelected(item);
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(Controller.this, "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            }
            else
            {
                msg("Connected.");
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }

}
