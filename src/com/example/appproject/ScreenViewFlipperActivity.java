package com.example.appproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class ScreenViewFlipperActivity extends Activity 
{
	
	ScreenViewFlipper flipper;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.main);
		//getActionBar().setDisplayHomeAsUpEnabled(true);
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.FILL_PARENT);
		
		flipper = new ScreenViewFlipper(this);
		
		setContentView(flipper, params);

		//previous = new Button(this);
			
	}
}

/*

public class ScreenViewFlipperActivity extends Activity{

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tutorial);
		
		
		Button previous = (Button)findViewById(R.id.previous);
		previous.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ScreenViewFlipperActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

}
*/