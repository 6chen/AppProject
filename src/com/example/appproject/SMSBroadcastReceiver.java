package com.example.appproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

public class SMSBroadcastReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)) {
			
			Log.i("PhoneState", "Receive a call");
			
			String phoneNumber = intent.getStringExtra("incoming_number");
			
			Log.i("phoneNumber",phoneNumber);
			
		
//			PendingIntent pIntent = PendingIntent.getActivities(this, 0, GetTelephone.class,0);
//			SmsManager sms = SmsManager.getDefault();
//			sms.sendTextMessage(phoneNumber, null, "Sorry, I am in class now", null, null);  
		}
	}
	
//	public void send()
//    {        
//    	String phoneNumber = phoneTextField.getText().toString();
//        String msg = msgTextField.getText().toString();  
//        
//        // make sure the fields are not empty
//        if (phoneNumber.length()>0 && msg.length()>0)                
//        {
//        	// call the sms manager
//            PendingIntent pi = PendingIntent.getBroadcast(this, 0, new Intent(this, GetTelephone.class), 0);                
//            SmsManager sms = SmsManager.getDefault();
//            sms.sendTextMessage(phoneNumber, null, msg, pi, null);  
//        }
//        else
//        {
//        	// display message if text fields are empty
//            Toast.makeText(getBaseContext(),"All field are required",Toast.LENGTH_SHORT).show();
//        }
//    }  

	

}
