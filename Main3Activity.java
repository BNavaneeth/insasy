package com.pronareshkumar986.insasy;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Main3Activity extends AppCompatActivity {
    public String lat,lon;
    public Button bt10;
    public Button bt11;
    public Button bt12;
    public Button bt13;
    private static final int MY_PERMISSION_REQUEST_SEND_SMS = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        bt10=(Button)findViewById(R.id.bt10);
        bt11=(Button)findViewById(R.id.bt11);
        bt12=(Button)findViewById(R.id.bt12);
        bt13=(Button)findViewById(R.id.bt13);



        Bundle extras=getIntent().getExtras();
        lat=extras.getString("nareshlat");
        lon=extras.getString("nareshlon");
        bt10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mes="Accident Occured At";
                String num="9550933728";
               String mes1="https://maps.google.com/?q="+lat+","+lon;

                sendSMSMMessage(num,mes);
                sendSMSMMessage(num,mes1);

            }
        });
        bt11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mes="Robbery Occured At";
                String num="9550933728";
                String mes1="https://maps.google.com/?q="+lat+","+lon;

                sendSMSMMessage(num,mes);
                sendSMSMMessage(num,mes1);
            }
        });
        bt12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mes="Physical Abuse At";
                String num="9550933728";
                String mes1="https://maps.google.com/?q="+lat+","+lon;

                sendSMSMMessage(num,mes);
                sendSMSMMessage(num,mes1);
            }
        });
        bt13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mes="Eve Teasing At";
                String num="9550933728";
                String mes1="https://maps.google.com/?q="+lat+","+lon;

                sendSMSMMessage(num,mes);
                sendSMSMMessage(num,mes1);
            }
        });
    }
    public void sendSMSMMessage(String number,String message) {


        if (number.trim().length() > 0) {

            if (ContextCompat.checkSelfPermission(Main3Activity.this,
                    Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Main3Activity.this,
                        new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSION_REQUEST_SEND_SMS);
            } else {
                String dial = "smsto:" + number;
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(number, null, message, null, null);
                Toast.makeText(Main3Activity.this, "Message sent", Toast.LENGTH_SHORT).show();

            }

        } else {
            Toast.makeText(Main3Activity.this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
        }
    }


}
