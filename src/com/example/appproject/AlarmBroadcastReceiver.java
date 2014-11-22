package com.example.appproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;

public class AlarmBroadcastReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		
		int modeCode = intent.getIntExtra("ModeCode", 0);
		
		switch (modeCode) {
		case 1:
			vibrate(audioManager);
			break;
		case 2:
			ring(audioManager);
			break;
		}
	}

	protected void vibrate(AudioManager audioManager){
		Log.i("VibrateMode","OK");
		audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
		audioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_OFF);
		audioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION, AudioManager.VIBRATE_SETTING_ON);
	}

	protected void ring(AudioManager audioManager){
		Log.i("RingMode","OK");
		audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		audioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_OFF);
		audioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION, AudioManager.VIBRATE_SETTING_OFF);
	}
}
