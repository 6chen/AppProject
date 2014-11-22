package com.example.appproject;

import com.example.appproject.R;

import android.content.Context;
import android.content.Intent;
import android.view.ActionMode.Callback;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

public class ScreenViewFlipper extends LinearLayout implements OnTouchListener {

	// Count of index buttons. Default is 3
	public static int countIndexes = 3;
	// Button Layout
	LinearLayout buttonLayout;
	// Index button images
	ImageView[] indexButtons;
////	// Image tutorial
	ImageView[] tutorialImageViews;
	
	// Views for the Flipper
	View[] views;
	// Flipper instance
	ViewFlipper flipper;
	// X coordinate for touch down
	float downX;
	// X coordinate for touch up
	float upX;
	// current index
	int currentIndex = 0;

	public ScreenViewFlipper(Context context) {
		super(context);

		this.setBackgroundColor(0xFFFFFF);

		// Layout inflation
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.screenview, this, true);

/*
		Button previous = (Button)findViewById(R.id.previous);
		previous.setOnClickListener(new OnClickListener() {
				

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ScreenViewFlipper.this, MainActivity.class);
				startActionMode((Callback) intent);
			}
		});*/
		
		/*Button previous = (Button)findViewById(R.id.previous);
		previous.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ScreenViewFlipperActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
*/		
		
		buttonLayout = (LinearLayout) findViewById(R.id.buttonLayout);

		
		
		flipper = (ViewFlipper) findViewById(R.id.flipper);
		flipper.setOnTouchListener(this);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		params.leftMargin = 50;

		indexButtons = new ImageView[countIndexes];
				
		for (int i = 0; i < countIndexes; i++) {
			indexButtons[i] = new ImageView(context);
			if (i == currentIndex) {
				indexButtons[i].setImageResource(R.drawable.flip1);
			} else {
				indexButtons[i].setImageResource(R.drawable.flip2);
			}
			//indexButtons[i].setPadding(10, 10, 10, 10);
			buttonLayout.addView(indexButtons[i], params);
		
			int gallery_grid_Images[] = {R.drawable.tuto1,R.drawable.tuto2,R.drawable.tuto3};
			for(int j=0; j<3;j++)
			{
				setFlipperImage(gallery_grid_Images[j]);
			}
					}
	}

	private void setFlipperImage(int res) {
    //Log.i("Set Filpper Called", res+"");
    //ImageView image = new ImageView(getApplicationContext());
	ImageView image = new ImageView(getContext());
    image.setBackgroundResource(res);
    flipper.addView(image);
}


	/**
	 * Update the display of index buttons
	 */
	private void updateIndexes() {
		for (int i = 0; i < countIndexes; i++) {
			if (i == currentIndex) {
				indexButtons[i].setImageResource(R.drawable.flip1);
			} else {
				indexButtons[i].setImageResource(R.drawable.flip2);
			}
		}
	}

	/**
	 * onTouch event handling
	 */
	public boolean onTouch(View v, MotionEvent event) {
		if (v != flipper)
			return false;

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			downX = event.getX();
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			upX = event.getX();

			if (upX < downX) { // in case of right direction

				// flipper.setInAnimation(AnimationUtils.loadAnimation(getContext(),
				// R.anim.push_left_in));
				// flipper.setOutAnimation(AnimationUtils.loadAnimation(getContext(),
				// R.anim.push_left_out));

				flipper.setInAnimation(AnimationUtils.loadAnimation(
						getContext(), R.anim.wallpaper_open_enter));
				flipper.setOutAnimation(AnimationUtils.loadAnimation(
						getContext(), R.anim.wallpaper_open_exit));

				if (currentIndex < (countIndexes - 1)) {
					flipper.showNext();

					// update index buttons
					currentIndex++;
					updateIndexes();
				}
			} else if (upX > downX) { // in case of left direction

				flipper.setInAnimation(AnimationUtils.loadAnimation(
						getContext(), R.anim.push_right_in));
				flipper.setOutAnimation(AnimationUtils.loadAnimation(
						getContext(), R.anim.push_right_out));

				if (currentIndex > 0) {
					flipper.showPrevious();

					// update index buttons
					currentIndex--;
					updateIndexes();
				}
			}
		}

		return true;
	}
}
