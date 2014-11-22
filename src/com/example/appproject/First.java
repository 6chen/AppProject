package com.example.appproject;

import com.example.appproject.R;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;

public class First extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first);
        
        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
        	public void run() {
        		Intent intent = new Intent(First.this, MainActivity.class);
        		startActivity(intent);
        		finish();
        	}
        }, 2000);
    }
}