package com.ansel.calldemo;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView tv_phone;
    private MyReceiver myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_phone = (TextView) findViewById(R.id.tv_phone);


        myReceiver = new MyReceiver();

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(myReceiver, new IntentFilter("android.intent.action.PHONE_STATE"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceiver);
    }

    class MyReceiver extends BroadcastReceiver {
        private static final String TAG = "PhoneStatReceiver";

        //        private static MyPhoneStateListener phoneListener = new MyPhoneStateListener();

        private boolean incomingFlag = false;

        private String incoming_number = null;

        @Override
        public void onReceive(Context context, Intent intent) {
            //如果是拨打电话
            if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
                incomingFlag = false;
                String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
                Log.i(TAG, "call OUT:" + phoneNumber);
            } else {
                //如果是来电
                TelephonyManager tm =
                        (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);

                switch (tm.getCallState()) {
                    case TelephonyManager.CALL_STATE_RINGING:
                        incomingFlag = true;//标识当前是来电
                        incoming_number = intent.getStringExtra("incoming_number");
                        Log.i(TAG, "RINGING :" + incoming_number);

                        tv_phone.setText("incoming_number = " + incoming_number);
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        if (incomingFlag) {
                            Log.i(TAG, "incoming ACCEPT :" + incoming_number);
                        }
                        break;

                    case TelephonyManager.CALL_STATE_IDLE:
                        if (incomingFlag) {
                            Log.i(TAG, "incoming IDLE");
                        }
                        break;
                }
            }
        }
    }
}
