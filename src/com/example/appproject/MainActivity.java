package com.example.appproject;

import java.util.Arrays;

import com.example.p1.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridLayout.Spec;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	protected static final int RESULT_OK = 0;
	private final int CALLER_REQUEST = 1;
	Button nextButton;
	Button button8;
	TextView recieve_title;
	private TextView titletextTextView;
	

	//This button is used to tell you which button was clicked by user.
	private Button clickedButton;
	
	private int gridRow = 13;
	private int gridCol = 7;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//registerForContextMenu(clickedButton);
		
		//Get the size(pixel) of screen
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		int unitH = dm.heightPixels/17;
		int unitHH = dm.heightPixels/34;
		
		//made by JH
		LinearLayout btn_Linearayout = (LinearLayout) findViewById(R.id.btn_Linearayout);
		/*ImageView image = new ImageView(this);
		image.setBackgroundResource(R.drawable.smile);
		image.setMaxHeight(unitHH);
		image.setMaxWidth(unitHH);
		btn_Linearayout.addView(image);*/
		String strColor = "#46433a";
		String strSizeString = "30dp";
		
		TextView proverb = new TextView(this);
		proverb.setText("Enjoy your time");
		btn_Linearayout.setGravity(1);
		proverb.setTextSize(23);
		
		
		proverb.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
		proverb.setWidth(dm.widthPixels/2);
		proverb.setHeight(unitHH*3);
		btn_Linearayout.addView(proverb);
		//proverb.setTextSize(50);


			
		/*ImageButton nextButton = new ImageButton(this);
		//nextButton.setText("����");
		nextButton.setMaxHeight(unitHH*3);
		nextButton.setBackgroundResource(R.drawable.next_button);
		btn_Linearayout.addView(nextButton);*/
		
		LinearLayout bottom_Linearlayout = (LinearLayout) findViewById(R.id.bottom_Linearlayout);
		Button nextButton = new Button(this);
		nextButton.setText("Tutorial");
		bottom_Linearlayout.setGravity(1);
		
		nextButton.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
		nextButton.setHeight(unitHH*3);
		nextButton.setTextSize(23);
		nextButton.setWidth(dm.widthPixels/2);
		
		nextButton.setBackgroundResource(R.drawable.tutorial);
		bottom_Linearlayout.addView(nextButton);	
		
		/*Button lastButton = new Button(this);
		lastButton.setText("������ ��ư");
		lastButton.setHeight(unitHH*3);
		lastButton.setTextColor(Color.parseColor(strColor));
		bottom_Linearlayout.addView(lastButton);*/
				
		
		LinearLayout weekLinearLayout = (LinearLayout) findViewById(R.id.week_LinearLayout);
		String [] week = {"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};
		
		TextView firstTextView = new TextView(this);
		firstTextView.setText(" ");
		firstTextView.setGravity(1);
		firstTextView.setWidth(dm.widthPixels/15);
		//firstTextView.setHeight(unitH);
		//firstTextView.setBackgroundResource(R.drawable.mainbackground);
		weekLinearLayout.addView(firstTextView);

		for(int i=0 ; i<7 ; i++)
		{
			TextView text = new TextView(this);
			text.setText(week[i]);
			text.setGravity(1);
			text.setWidth((dm.widthPixels)*2/15);
			//text.setHeight(unitH);
			text.setBackgroundResource(R.drawable.day);
			weekLinearLayout.addView(text);
		}

		//made by JH
		//set day textview like mon, sun ... // calculate the size of the textview according to the size of screen
		GridLayout timeGrid = (GridLayout) findViewById(R.id.timeGrid);
		
		String [] time = {"10","11","12","1","2","3","4","5","6","7","8","9"};
		
		/*Button longButton = new Button(this);
		longButton.setText("");
		longButton.setTextColor(Color.parseColor(strColor));
		bottom_Linearlayout.addView(longButton);*/
		
		TextView timetext1 = new TextView(this);
		timetext1.setText(" ");
		timetext1.setHeight(unitH/2);
		timetext1.setWidth(dm.widthPixels/15);
		timetext1.setGravity(Gravity.CENTER|Gravity.CENTER_VERTICAL);
		//timetext1.setBackgroundResource(R.drawable.time);
		timeGrid.addView(timetext1);
		
		for(int i=0 ; i<12 ; i++)
		{
			TextView timetext2 = new TextView(this);
			timetext2.setText(time[i]);
			timetext2.setHeight(unitH);
			timetext2.setWidth(dm.widthPixels/15);
			timetext2.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
			timetext2.setPadding(0, 0, 7, 0);
			//timetext2.setBackgroundResource(R.drawable.time);
			timeGrid.addView(timetext2);
		}

		TextView timetext3 = new TextView(this);
		timetext3.setText(" ");
		timetext3.setHeight(unitH/2);
		timetext3.setWidth(dm.widthPixels/15);
		//timetext3.setBackgroundResource(R.drawable.time);
		timeGrid.addView(timetext3);
		
		
		DBOpenHelper helper = new DBOpenHelper(MainActivity.this, "classscheduler.db", null, 1);
		SQLiteDatabase db = helper.getWritableDatabase();

		Cursor resultCursor = db.rawQuery("select * from class", null);

		int[] existsClassIdArray = new int[resultCursor.getCount()]; 
		int existsClassIdArrayLoc = 0;
		while (resultCursor.moveToNext()) {
			existsClassIdArray[existsClassIdArrayLoc] = resultCursor.getInt(resultCursor.getColumnIndex("_classid"));
			existsClassIdArrayLoc++;
		}

		GridLayout gridLayout = (GridLayout) findViewById(R.id.mainGrid);

		for (int row = 0; row < gridRow; row++) {
			for (int col = 0; col < gridCol; col++) {
				int location = (row*10)+col;
				if (Arrays.binarySearch(existsClassIdArray,location) < 0) {
					Button btn = new Button(this);
					btn.setId(location);
					btn.setWidth((dm.widthPixels*2)/15); 
					btn.setHeight(unitH);
					//btn.setText(location+"");
					btn.setBackgroundResource(R.drawable.mainbackground);
					
					btn.setOnClickListener(this);
					
					registerForContextMenu(btn);
					
					Spec rowSpec = gridLayout.spec(row);
					Spec colSpec = gridLayout.spec(col);
					GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec,colSpec);
					gridLayout.addView(btn,params);	
				}
			}
		}
		resultCursor.close();
		
		Cursor resultCursor1 = db.rawQuery("select * from class", null);

		while (resultCursor1.moveToNext()) {
			int classId = resultCursor1.getInt(resultCursor1.getColumnIndex("_classid"));
			int classIdRow = classId/10;
			int classIdCol = classId%10;

			String className = resultCursor1.getString(resultCursor1.getColumnIndex("_classname"));
			String classStartTime = resultCursor1.getString(resultCursor1.getColumnIndex("_starttime"));
			String classEndTime = resultCursor1.getString(resultCursor1.getColumnIndex("_endtime"));	
			int span = Integer.parseInt(classEndTime.substring(1, 2)) - Integer.parseInt(classStartTime.substring(1, 2));
			String classColor = resultCursor1.getString(resultCursor1.getColumnIndex("_color"));

			Button btn = new Button(this);
			btn.setId(classId);
			btn.setWidth((dm.widthPixels*2)/15); 
			btn.setHeight(unitH);
			
			if (classColor.equals("pink")) {
				btn.setBackgroundResource(R.drawable.mainborder);
			}
			
			btn.setText(className);
			btn.bringToFront();
			btn.setOnClickListener(this);

			registerForContextMenu(btn);
			
			Spec rowSpec = gridLayout.spec(classIdRow,span); //<-should modify 1 to span
			Spec colSpec = gridLayout.spec(classIdCol);
			GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec,colSpec);
			params.setGravity(Gravity.FILL);
			gridLayout.addView(btn,params);	
		}
		resultCursor1.close();
		db.close();
		
		 nextButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, ScreenViewFlipperActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}


	public void onClick(View v) { 
		clickedButton = (Button) findViewById(v.getId());
		
		 		Intent intent = new Intent(MainActivity.this, Plus.class); 
					intent.putExtra("classId", clickedButton.getId());
					startActivityForResult(intent,1);
	}
		 
	
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
	{
		Toast.makeText(getApplicationContext(),"context", Toast.LENGTH_SHORT).show();
		
		super.onCreateContextMenu(menu,  v,  menuInfo);
		
		menu.setHeaderTitle("Delete or not");
		menu.add(0,1,0, "Delete");
		menu.add(0,2,0, "Cancle");
	}
	
	public boolean onContextItemSelected(MenuItem item)
	{
		//Button btnButton = (Button) item;
		//btnButton.getId();
//		
//		Toast.makeText(getApplicationContext(),btnButton.getId(), Toast.LENGTH_SHORT).show();
		
		DBOpenHelper helper = new DBOpenHelper(MainActivity.this, "classscheduler", null, 1);
		SQLiteDatabase db = helper.getWritableDatabase();
		
		//Cursor resultcuCursor = db.rawQuery("select _classid from class", null);
	
		switch(item.getItemId()){
		
//		Button btnButton = (Button) item;
//		btnButton.getId();
		
		case 1:
			//Toast.makeText(getApplicationContext(),btnButton.getId(), Toast.LENGTH_SHORT).show();
			//Log.i("PhoneState", btnButton.getId());
			//Log.i("PhoneState", db.rawQuery("select_classid form class",null));
			//Toast.makeText(getApplicationContext(),(CharSequence) db.rawQuery("select_classid form class", null), Toast.LENGTH_SHORT).show();
			//Cursor resultcuCursor = db.rawQuery("delete row from classscheduler", null);
			
			db.execSQL("delete row from classscheduler");
			
			return true;
		
		case 2:
			Toast.makeText(getApplicationContext(),"Cancle", Toast.LENGTH_SHORT).show();

			return true;
		
		default :
			return super.onContextItemSelected(item);
			
				
		}
	}
	

	protected void onActivityResult (int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 1 && resultCode ==2)
		{
			
			finish();
			Intent intent = new Intent(MainActivity.this,MainActivity.class);
			startActivity(intent);
			
			//--> This part is used for database connect test
//    		DBOpenHelper helper = new DBOpenHelper(MainActivity.this, "classscheduler", null, 1);
//    		SQLiteDatabase db = helper.getWritableDatabase();
//    		
//    		Cursor resultcuCursor = db.rawQuery("select _classid from class", null);
//    		if (resultcuCursor!=null) {
//    			String[] columns = resultcuCursor.getColumnNames();
//    			while (resultcuCursor.moveToNext()) {
//    				for (String columnName :columns) {
//    					Log.i("info", resultcuCursor.getString(resultcuCursor.getColumnIndex(columnName)));
//    				}
//    			}
//    			resultcuCursor.close();
//    		}
//    		
//    		db.close();
//			
//			
//			GridLayout gridLayout = (GridLayout) findViewById(R.id.mainGrid);
//
//			Spec rowSpec = gridLayout.spec(clickedButton.getId()/10,2);
//			Spec colSpec = gridLayout.spec(clickedButton.getId()%10);
//
//			clickedButton.setBackgroundResource(R.drawable.mainborder);
//			clickedButton.bringToFront();
//			GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec,colSpec);
//			params.setGravity(Gravity.FILL);
//			clickedButton.setLayoutParams(params);
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
