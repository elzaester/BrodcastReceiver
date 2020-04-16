package com.example.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;


public class SMSReceiver extends BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onReceive(Context context, Intent intent){
        //---get the SMS message passed in---
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = null;
        String str = "SMS from";
        if (bundle != null){
            //---retrieve the SMS message received---
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                msgs = Telephony.Sms.Intents.getMessagesFromIntent(intent);
            }
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];
            for (int i=0; i<msgs.length; i++){
                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                if(i==0){
                    str += msgs[i].getOriginatingAddress();
                    str += ": ";
                }
                str += msgs[i].getMessageBody().toString();
            }
            //---display the new SMS message ---
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
            Log.d("SMSReceiver", str);

           //---Stop the SMS message from being broadcasted---
            this.abortBroadcast();
        }
    }
}
