package com.pronareshkumar986.insasy;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity implements ConnectionCallbacks, OnConnectionFailedListener {
    private static final int MY_PERMISSION_REQUEST_SEND_SMS = 1;
    public static final int RequestPermissionCode = 1;
    private static final int REQUEST_CALL = 1;
    protected GoogleApiClient googleApiClient;
    protected TextView con1;
    protected TextView con2;
    protected Location lastLocation;
    private Button bt1;
    private Button bt2;
    private Button btresp;
    private EditText et1;
    private EditText et2;
    private EditText et3;
    private FusedLocationProviderClient fusedLocationProviderClient;
    public String lat, lon;
    public String n,mes;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    private Button res;
    private int c=0;
    public String num1;
    public String num2;
    public String num3;
    private Button btsd;
    private Button btstp;
    private MediaPlayer song;
    public String dial;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt1=(Button)findViewById(R.id.bt1);
        bt2=(Button)findViewById(R.id.bt2);
        et1=(EditText)findViewById(R.id.et1);
        et2=(EditText)findViewById(R.id.et2);
        et3=(EditText)findViewById(R.id.et3);
        btsd=(Button)findViewById(R.id.btsd);
        btstp=(Button)findViewById(R.id.btstp);
        btresp=(Button)findViewById(R.id.btresp) ;


       con1=(TextView)findViewById(R.id.con1);
       con2=(TextView)findViewById(R.id.con2);

       bt1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               SharedPreferences sharedPreferences = getSharedPreferences("shared", MODE_PRIVATE);
               SharedPreferences.Editor editor = sharedPreferences.edit();
               editor.putString("text1", et1.getText().toString());
               editor.putString("text2", et2.getText().toString());
               editor.putString("text3", et3.getText().toString());

               editor.apply();

           }
       });
       update();
       bt2.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent=new Intent(MainActivity.this,Main2Activity.class);
               startActivity(intent);
           }
       });
       btsd.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               play();
           }
       });
       btstp.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               stop();
           }
       });

       num1=et1.getText().toString();
       num2=et2.getText().toString();
       num3=et3.getText().toString();




        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {

                // tvShake.setText("Shake Action is just detected!!");
               // Toast.makeText(MainActivity.this, "Shaked!!!", Toast.LENGTH_SHORT).show();
                //  mainActivity.sendSMSMMessage(mainActivity.n,mainActivity.mes);
                c=c+1;
                if(c==2) {

                   // mes = "http://www.google.com/maps/place/" + lat + "," + lon;

                    mes="https://maps.google.com/?q="+lat+","+lon;
                    sendSMSMMessage(num1, mes);
                    sendSMSMMessage(num2,mes);
                    sendSMSMMessage(num3,mes);
                    c=0;
                }
            }
        });
        btresp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent=new Intent(getApplicationContext(),Main3Activity.class);
                intent.putExtra("nareshlat",con1.getText().toString());
                intent.putExtra("nareshlon",con2.getText().toString());
                startActivity(intent);
            }

        });


    }
    private void update() {

        SharedPreferences sharedPreferences = getSharedPreferences("shared", MODE_PRIVATE);
        String text1 = sharedPreferences.getString("text1", "");
        et1.setText(text1);
        String text2 = sharedPreferences.getString("text2", "");
        et2.setText(text2);
        String text3 = sharedPreferences.getString("text3", "");
        et3.setText(text3);
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
        super.onStop();
    }
    public void play(){
        if(song==null) {
            song = MediaPlayer.create(MainActivity.this,R.raw.police);
        }
        song.start();
    }
    public void stop(){
        song.release();
        song=null;
    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        } else {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                con1.setText(String.valueOf(location.getLatitude()));
                                con2.setText(String.valueOf(location.getLongitude()));
                                lat = String.valueOf(location.getLatitude());
                                lon = String.valueOf(location.getLongitude());
                               // setLat(lat);
                                //setLon(lon);
                            }
                        }
                    });
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(MainActivity.this, new
                String[]{ACCESS_FINE_LOCATION}, RequestPermissionCode);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("MainActivity", "Connection failed: " + connectionResult.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("MainActivity", "Connection suspendedd");
    }

    @Override
    public boolean dispatchKeyEvent (KeyEvent event){
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_UP) {
                    if (event.getEventTime() - event.getDownTime() > ViewConfiguration.getLongPressTimeout()) {
                        //TODO long click action
                        makePhoneCall(num1);
                    } else {
                        //TODO click action
                        makePhoneCall(num2);
                    }
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_UP) {
                    if (event.getEventTime() - event.getDownTime() > ViewConfiguration.getLongPressTimeout()) {
                        //TODO long click action
                        String help="I'm in Problem,Please help me.";
                        sendSMSMMessage(num1,help);
                        sendSMSMMessage(num2,help);
                        sendSMSMMessage(num3,help);

                    } else {
                        //TODO click action
                        makePhoneCall(num3);
                    }
                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }

    }

    public void sendSMSMMessage(String number,String message) {


        if (number.trim().length() > 0) {

            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSION_REQUEST_SEND_SMS);
            } else {
                String dial = "smsto:" + number;
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(number, null, message, null, null);
                Toast.makeText(MainActivity.this, "Message sent", Toast.LENGTH_SHORT).show();

            }

        } else {
            Toast.makeText(MainActivity.this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }
    private void makePhoneCall(String num) {

        if (num.trim().length() > 0) {

            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            } else {
                dial = "tel:" + num;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }

        } else {
            Toast.makeText(MainActivity.this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
        }
    }
    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall(dial);
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
   /* public String getLat(){
        return lat;
    }
    public void setLat(String lat1){
        lat=lat1;
    }
    public String getLon(){

        return lon;
    }
    public void setLon(String lon1){
        lon=lon1;
    }*/
}





