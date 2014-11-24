package com.example.appproject;
import java.lang.reflect.Array;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Calendar;

import javax.security.auth.SubjectDomainCombiner;

import com.example.appproject.R;

import android.R.integer;
import android.R.string;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.MailTo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;


public class Plus extends Activity 
{
	private EditText plus_title,plus_professor;
	private ToggleButton plus_Sunday, plus_Monday, plus_Tuesday, plus_Wednesday, plus_Thursday, plus_Friday, plus_Saturday;
	private ToggleButton tog_1red, tog_2hotpink, tog_3orange, tog_4lightorange, tog_5yellow;
	private int classId;

	//Start,Finish Time
	private TextView plus_starttime, plus_endtime;
	private Button plus_pickTime1, plus_pickTime2;

	private int nHour;
	private int nMinute;
	private int mHour;
	private int mMinute;

	//Dialog
	static final int TIME_DIALOG_ID = 1;
	static final int TIME_DIALOG_ID1 = 2;

	//switch
	private Switch plus_switch;

	//spinner
	private Spinner plus_spinner;

	//select
	private EditText plus_message;

	//Close
	private Button Close01, Close02; 

	//SubjectTitle
	private String subjectTitle, subjectProfessor;

	//dayofweek Checked Array
	private String dayofweekCheckedString;
	private int[] dayofweekCheckedArr = {0,0,0,0,0,0,0};
	private int[] beforedayofweek = null;
	private String insertStartTime = "";
	private String insertEndTime = "";
	private String color = "pink";
	private int msgYn;
	private int msgIndex;
	private String msgContent;

	private int isFromDB = 0;
	private int updatedYn = 0;
	private String beforeTitle, beforeProfessor;
	private String afterTitle, afterProfessor;
	private String beforeStartTime, beforeEndTime;
	private AlarmManager alarmManager;
	private long oneWeek = (7*24*60*60)*1000;

	private Calendar curCalendar, setStartCalendar, setEndCalendar;

	//@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plus);

		//This part is used for intent receive value test.
		Intent getIntent = getIntent();
		classId = getIntent.getIntExtra("classId",0);

		xmlInitial();

		//
		DBOpenHelper helper = new DBOpenHelper(Plus.this, "classscheduler.db", null, 1);
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor resultCursor = db.rawQuery("select * from class where _classid = "+classId, null);

		if (resultCursor.getCount() == 0) {
			//Set dayofweek
			dayofweekInitial(classId%10);

			//set starttime and end time 
			mHour = classId/10+9;
			mMinute = 0;
			nHour = (classId/10)+9+1;
			nMinute = 0;
			updateDisplay();
		}else if (resultCursor.getCount() > 0) {
			isFromDB = 1;
			resultCursor.moveToNext();

			beforeTitle = resultCursor.getString(resultCursor.getColumnIndex("_classname"));
			beforeProfessor = resultCursor.getString(resultCursor.getColumnIndex("_teacher"));

			plus_title.setText(beforeTitle);
			plus_professor.setText(beforeProfessor);

			int dayofweek = resultCursor.getInt(resultCursor.getColumnIndex("_dayofweek"));
			dayofweekInitial(dayofweek);

			beforedayofweek = dayofweekCheckedArr;

			String startTime = resultCursor.getString(resultCursor.getColumnIndex("_starttime"));
			String endTime = resultCursor.getString(resultCursor.getColumnIndex("_endtime"));

			beforeStartTime = startTime;
			beforeEndTime = endTime;

			mHour = Integer.parseInt(startTime.substring(0, 2));
			mMinute = Integer.parseInt(startTime.substring(2, 4));
			nHour = Integer.parseInt(endTime.substring(0, 2));
			nMinute = Integer.parseInt(endTime.substring(2, 4));
			updateDisplay();

			msgYn = resultCursor.getInt(resultCursor.getColumnIndex("_msgyn"));

			if (msgYn == 1) {
				plus_switch.setChecked(true);
				msgIndex = resultCursor.getInt(resultCursor.getColumnIndex("_msgindex"));

				if (msgIndex == 4) {
					String msgContent = resultCursor.getString(resultCursor.getColumnIndex("_msgcontent"));
					plus_message.setText(msgContent);
				}
			}
		}
		resultCursor.close();
		db.close();

		plus_pickTime1.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View v) 
			{
				showDialog(TIME_DIALOG_ID);
			}
		});

		plus_pickTime2.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View v) 
			{		
				showDialog(TIME_DIALOG_ID1);
			}
		});

		//Save Button Event
		Close01.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View v) 
			{		
				subjectTitle = plus_title.getText().toString();
				subjectProfessor = plus_professor.getText().toString();

				//set starttime and endtime for insert to database
				if ((mHour+"").length() < 2 ) {
					insertStartTime += "0"+ mHour ;
				}else {
					insertStartTime += mHour;
				}

				if ((mMinute+"").length() < 2 ) {
					insertStartTime += "0"+ mMinute ;
				}else {
					insertStartTime += mMinute;
				}

				if ((nHour+"").length() < 2 ) {
					insertEndTime += "0"+ nHour ;
				}else {
					insertEndTime += nHour;
				}

				if ((nMinute+"").length() < 2 ) {
					insertEndTime += "0"+ nMinute ;
				}else {
					insertEndTime += nMinute;
				}

				if (plus_switch.isChecked()) {
					msgYn = 1;
					if (msgIndex == 4) {
						msgContent = plus_message.getText().toString();
					}
				}else {
					msgYn = 0;
				}

				if (isFromDB == 0) { //If isFromDB is 0 then it's a new Class, So it just need to insert the data into DB.
					DBOpenHelper helper = new DBOpenHelper(Plus.this, "classscheduler.db", null, 1);
					SQLiteDatabase db = helper.getWritableDatabase();

					for (int i = 0; i < dayofweekCheckedArr.length;i++) {
						int dayofweek = i;
						if (dayofweekCheckedArr[dayofweek] != 0) {

							int Y = mHour - 9;
							classId = Y*10 + dayofweek;

							if (msgYn == 0) {
								db.execSQL("insert into class(_classid, _classname, _teacher, _starttime, _endtime, _dayofweek, _color, _msgyn)"
										+ " values(" + classId +", '" + subjectTitle +"', '"+ subjectProfessor +"', '" + insertStartTime +"', '" 
										+ insertEndTime +"', " + dayofweek +", '" + color +"', " + msgYn+")");
							}else if (msgYn == 1) {
								if (msgIndex != 4) {
									db.execSQL("insert into class(_classid, _classname, _teacher, _starttime, _endtime, _dayofweek, _color, _msgyn, _msgindex)"
											+ " values(" + classId +", '" + subjectTitle +"', '"+ subjectProfessor +"', '" + insertStartTime +"', '" 
											+ insertEndTime +"', " + dayofweek +", '" + color +"', " + msgYn + ", " + msgIndex + ")");
								}else if(msgIndex == 4){
									db.execSQL("insert into class(_classid, _classname, _teacher, _starttime, _endtime, _dayofweek, _color, _msgyn, _msgindex, _msgcontent)"
											+ " values(" + classId +", '" + subjectTitle + "', '"+ subjectProfessor +"', '" + insertStartTime +"', '" 
											+ insertEndTime +"', " + dayofweek +", '" + color +"', " + msgYn + ", " + msgIndex + ", '"+ msgContent+"')");
								}
							}

							//Set Alarm if this is a new page for creating a new class 

							//Get current time 
							String curTimestamp = getCurrentTimestamp();
							long curTimeMilli = curCalendar.getTimeInMillis();
							Log.i("curTimestamp", curTimestamp);
							Log.i("curTimestampMillinSecond", curTimeMilli+"");

							//Get the start time of class
							String setStartTimestamp = getSetStartTimestamp(dayofweek, mHour, mMinute);
							long setStartTimeMilli = setStartCalendar.getTimeInMillis();
							Log.i("setStartTimestamp", setStartTimestamp);
							Log.i("setStartTimestampMillinSecond", setStartTimeMilli+"");

							//Get the end time of class
							String setEndTimestamp = getSetEndTimestamp(dayofweek, mHour, mMinute);
							long setEndTimeMilli = setEndCalendar.getTimeInMillis();
							Log.i("setStartTimestamp", setEndTimestamp);
							Log.i("setEndTimestampMillinSecond", setEndTimeMilli+"");

							if (curTimeMilli > setStartTimeMilli) {
								Log.i("Result","a week later");

								int startAlarmId = classId * 10 + 1;
								int endAlarmId = classId * 10 + 2;
								alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

								//set alarm of start time
								Intent intentStart = new Intent(Plus.this, AlarmBroadcastReceiver.class);
								intentStart.putExtra("ModeCode", 1); //If this is start alarm then the ModeCode is 1
								PendingIntent startAlarmIntent = PendingIntent.getBroadcast(Plus.this, startAlarmId, intentStart, 0);
								//alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, setStartTimeMilli+oneWeek, oneWeek, startAlarmIntent);
								alarmManager.set(AlarmManager.RTC_WAKEUP, setStartTimeMilli, startAlarmIntent); // this code is for test
								Log.i("StartAlarmSeted","New");

								//set alarm of end time
								Intent intentEnd = new Intent(Plus.this, AlarmBroadcastReceiver.class);
								intentEnd.putExtra("ModeCode", 2);//If this is end alarm then the ModeCode is 2
								PendingIntent endAlarmIntent = PendingIntent.getBroadcast(Plus.this, endAlarmId, intentEnd, 0);
								//alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, setEndTimeMilli+oneWeek, oneWeek, endAlarmIntent);
								alarmManager.set(AlarmManager.RTC_WAKEUP, setEndTimeMilli, endAlarmIntent);// this code is for test
								Log.i("EndAlarmSeted","New");

							}else {
								Log.i("Result","this week");

								int startAlarmId = classId * 10 + 1;
								int endAlarmId = classId * 10 + 2;
								alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

								//set alarm of start time
								Intent intentStart = new Intent(Plus.this, AlarmBroadcastReceiver.class);
								intentStart.putExtra("ModeCode", 1); //If this is start alarm then the ModeCode is 1
								PendingIntent startAlarmIntent = PendingIntent.getBroadcast(Plus.this, startAlarmId, intentStart, 0);
								//								alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, setStartTimeMilli, oneWeek, startAlarmIntent);
								alarmManager.set(AlarmManager.RTC_WAKEUP, setStartTimeMilli, startAlarmIntent);// this code is for test
								Log.i("StartAlarmSeted","New");

								//set alarm of end time
								Intent intentEnd = new Intent(Plus.this, AlarmBroadcastReceiver.class);
								intentEnd.putExtra("ModeCode", 2);//If this is end alarm then the ModeCode is 2
								PendingIntent endAlarmIntent = PendingIntent.getBroadcast(Plus.this, endAlarmId, intentEnd, 0);
								//								alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, setEndTimeMilli, oneWeek, endAlarmIntent);
								alarmManager.set(AlarmManager.RTC_WAKEUP, setEndTimeMilli, endAlarmIntent);// this code is for test
								Log.i("EndAlarmSeted","New");
							}

						}
					}

					db.close();
				}else if (isFromDB == 1) {//If isFromDB is 1 then is not a new Class, So we need to know if the data have been changed.

					if (!beforeTitle.equals(subjectTitle) || !beforeProfessor.equals(subjectProfessor)) {
						updatedYn = 1;
					}

					if (!Arrays.equals(dayofweekCheckedArr, beforedayofweek)) {
						updatedYn = 1;
					}

					if (!beforeStartTime.equals(insertStartTime) || !beforeEndTime.equals(insertEndTime)) {
						updatedYn = 1;
					}



					if (updatedYn == 0) {//If the data have not been changed it just need to finish this page.
						finish();
						//						Log.i("step","from db, but no update");
					}else if (updatedYn == 1) {//If the data have been changed then we need to update the class in DB
						DBOpenHelper helper = new DBOpenHelper(Plus.this, "classscheduler.db", null, 1);
						SQLiteDatabase db = helper.getWritableDatabase();

						for (int i = 0; i < dayofweekCheckedArr.length;i++) {
							int dayofweek = i;
							if (dayofweekCheckedArr[dayofweek] != 0) {

								int Y = mHour - 9;
								classId = Y*10 + dayofweek;


								//								Log.i("classid",""+classId);
								//								Log.i("step","from db, and have been updated");


								if (msgYn == 0) {
									db.execSQL("update class set _classname = '"+ subjectTitle +"', _teacher = '"+ subjectProfessor+"',"
											+ " _starttime ='"+ insertStartTime+"', _endtime = '"+insertEndTime+"',"
											+ " _dayofweek = "+ dayofweek +", _color = '"+color+"', _msgyn = "+msgYn+" where _classid = "+ classId + "");
								}else if (msgYn == 1) {
									if (msgIndex != 4) {
										db.execSQL("update class set _classname = '"+ subjectTitle +"', _teacher = '"+ subjectProfessor+"',"
												+ " _starttime ='"+ insertStartTime+"', _endtime = '"+insertEndTime+"',"
												+ " _dayofweek = "+ dayofweek +", _color = '"+color+"', _msgyn = "+msgYn+","
												+ " _msgindex = "+msgIndex+" where _classid = "+ classId + "");
									}else if(msgIndex == 4){
										db.execSQL("update class set _classname = '"+ subjectTitle +"', _teacher = '"+ subjectProfessor+"',"
												+ " _starttime ='"+ insertStartTime+"', _endtime = '"+insertEndTime+"',"
												+ " _dayofweek = "+ dayofweek +", _color = '"+color+"', _msgyn = "+msgYn+","
												+ " _msgindex = "+msgIndex+", _msgcontent = '"+msgContent+"' where _classid = "+ classId + "");
									}
								}

								//Set Alarm for Updated class   

								//Get current time 
								String curTimestamp = getCurrentTimestamp();
								long curTimeMilli = curCalendar.getTimeInMillis();
								Log.i("curTimestamp", curTimestamp);
								Log.i("curTimestampMillinSecond", curTimeMilli+"");

								//Get the start time of class
								String setStartTimestamp = getSetStartTimestamp(dayofweek, mHour, mMinute);
								long setStartTimeMilli = setStartCalendar.getTimeInMillis();
								Log.i("setStartTimestamp", setStartTimestamp);
								Log.i("setStartTimestampMillinSecond", setStartTimeMilli+"");

								//Get the end time of class
								String setEndTimestamp = getSetEndTimestamp(dayofweek, mHour, mMinute);
								long setEndTimeMilli = setEndCalendar.getTimeInMillis();
								Log.i("setStartTimestamp", setEndTimestamp);
								Log.i("setEndTimestampMillinSecond", setEndTimeMilli+"");

								if (curTimeMilli > setStartTimeMilli) {
									Log.i("Result","a week later");

									int startAlarmId = classId * 10 + 1;
									int endAlarmId = classId * 10 + 2;
									alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

									//set alarm of start time
									Intent intentStart = new Intent(Plus.this, AlarmBroadcastReceiver.class);
									intentStart.putExtra("ModeCode", 1); //If this is start alarm then the ModeCode is 1
									PendingIntent startAlarmIntent = PendingIntent.getBroadcast(Plus.this, startAlarmId, intentStart, 0);

									//cancel alarm of start time 

									//									alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, setStartTimeMilli+oneWeek, oneWeek, startAlarmIntent);
									alarmManager.set(AlarmManager.RTC_WAKEUP, setStartTimeMilli, startAlarmIntent); // this code is for test
									Log.i("StartAlarmSeted","Update");

									//set alarm of end time
									Intent intentEnd = new Intent(Plus.this, AlarmBroadcastReceiver.class);
									intentEnd.putExtra("ModeCode", 2);//If this is end alarm then the ModeCode is 2
									PendingIntent endAlarmIntent = PendingIntent.getBroadcast(Plus.this, endAlarmId, intentEnd, 0);

									//cancel alarm of end time 


									//									alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, setEndTimeMilli+oneWeek, oneWeek, endAlarmIntent);
									alarmManager.set(AlarmManager.RTC_WAKEUP, setEndTimeMilli, endAlarmIntent);// this code is for test
									Log.i("EndAlarmSeted","Update");

								}else {
									Log.i("Result","this week");

									int startAlarmId = classId * 10 + 1;
									int endAlarmId = classId * 10 + 2;
									alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

									//set alarm of start time
									Intent intentStart = new Intent(Plus.this, AlarmBroadcastReceiver.class);
									intentStart.putExtra("ModeCode", 1); //If this is start alarm then the ModeCode is 1
									PendingIntent startAlarmIntent = PendingIntent.getBroadcast(Plus.this, startAlarmId, intentStart, 0);

									//cancel alarm of start time 


									//									alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, setStartTimeMilli, oneWeek, startAlarmIntent);
									alarmManager.set(AlarmManager.RTC_WAKEUP, setStartTimeMilli, startAlarmIntent);// this code is for test
									Log.i("StartAlarmSeted","Update");

									//set alarm of end time
									Intent intentEnd = new Intent(Plus.this, AlarmBroadcastReceiver.class);
									intentEnd.putExtra("ModeCode", 2);//If this is end alarm then the ModeCode is 2
									PendingIntent endAlarmIntent = PendingIntent.getBroadcast(Plus.this, endAlarmId, intentEnd, 0);

									//cancel alarm of end time 

									//alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, setEndTimeMilli, oneWeek, endAlarmIntent);
									alarmManager.set(AlarmManager.RTC_WAKEUP, setEndTimeMilli, endAlarmIntent);// this code is for test
									Log.i("EndAlarmSeted","Update");
								}
							}
						}
						db.close();
					}
				}

				//				String text = plus_title.getText().toString();
				Intent dataIntent = new Intent();
				dataIntent.putExtra("dataIntent",2);//titleEdit.getText()
				setResult(2,dataIntent);

				Toast.makeText(getApplicationContext(),"saved", Toast.LENGTH_SHORT).show();
				finish();
			}
		});

		Close02.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) 
			{		
				finish();
			}
		});

		String[] set_message = {"Sorry, I am in class now", 
				"Sorry, I am in meeting now", 
				"Sorry, I am in working now",
				"Sorry, I cannot be in touch now" ,
		"user defined"};

		Spinner spinner = (Spinner)findViewById(R.id.plus_spinner);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, set_message);

		spinner.setAdapter(adapter);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		if (msgIndex != 4) {
			spinner.setSelection(0);
		}else {
			spinner.setSelection(msgIndex);
		}

		spinner.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
			{
				switch (pos) 
				{
				case 0:
					plus_message.setVisibility(View.INVISIBLE);
					break;

				case 1:
					plus_message.setVisibility(View.INVISIBLE);
					break;

				case 2:
					plus_message.setVisibility(View.INVISIBLE);
					break;

				case 3:
					plus_message.setVisibility(View.INVISIBLE);
					break;

				case 4:
					//Toast.makeText(parent.getContext(),"" + parent.getItemAtPosition(4).toString(), Toast.LENGTH_SHORT).show();
					plus_message.setVisibility(View.VISIBLE);
					break;
				}

				msgIndex = pos;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) 
			{
				// TODO Auto-generated method stub
				msgIndex = 0;
			}
		});

	}

	//day Toggle
	public ToggleButton.OnCheckedChangeListener sunON = new Switch.OnCheckedChangeListener(){
		public void onCheckedChanged(CompoundButton cb, boolean isChecking){
			if(isChecking)
			{ 
				plus_Sunday.setBackgroundResource(R.drawable.plus_toggle_select);
				dayofweekCheckedArr[0] = 1;
			}
			else
			{ 
				plus_Sunday.setBackgroundResource(R.drawable.plus_toggle);
				dayofweekCheckedArr[0] = 0;
			}
		}
	};

	public ToggleButton.OnCheckedChangeListener monON = new Switch.OnCheckedChangeListener(){
		public void onCheckedChanged(CompoundButton cb, boolean isChecking){
			if(isChecking)
			{ 
				plus_Monday.setBackgroundResource(R.drawable.plus_toggle_select);
				dayofweekCheckedArr[1] = 1;
			}
			else
			{ 
				plus_Monday.setBackgroundResource(R.drawable.plus_toggle);
				dayofweekCheckedArr[1] = 0;
			}
		}
	};

	public ToggleButton.OnCheckedChangeListener tueON = new Switch.OnCheckedChangeListener(){
		public void onCheckedChanged(CompoundButton cb, boolean isChecking){
			if(isChecking)
			{ 
				plus_Tuesday.setBackgroundResource(R.drawable.plus_toggle_select);
				dayofweekCheckedArr[2] = 1;
			}
			else
			{ 
				plus_Tuesday.setBackgroundResource(R.drawable.plus_toggle);
				dayofweekCheckedArr[2] = 0;
			}
		}
	};

	public ToggleButton.OnCheckedChangeListener wedON = new Switch.OnCheckedChangeListener(){
		public void onCheckedChanged(CompoundButton cb, boolean isChecking){
			if(isChecking)
			{ 
				plus_Wednesday.setBackgroundResource(R.drawable.plus_toggle_select);
				dayofweekCheckedArr[3] = 1;
			}
			else
			{ 
				plus_Wednesday.setBackgroundResource(R.drawable.plus_toggle);
				dayofweekCheckedArr[3] = 0;
			}
		}
	};

	public ToggleButton.OnCheckedChangeListener thrON = new Switch.OnCheckedChangeListener(){
		public void onCheckedChanged(CompoundButton cb, boolean isChecking){
			if(isChecking)
			{ 
				plus_Thursday.setBackgroundResource(R.drawable.plus_toggle_select);
				dayofweekCheckedArr[4] = 1;
			}
			else
			{ 
				plus_Thursday.setBackgroundResource(R.drawable.plus_toggle);
				dayofweekCheckedArr[4] = 0;
			}
		}
	};

	public ToggleButton.OnCheckedChangeListener friON = new Switch.OnCheckedChangeListener(){
		public void onCheckedChanged(CompoundButton cb, boolean isChecking){
			if(isChecking)
			{ 
				plus_Friday.setBackgroundResource(R.drawable.plus_toggle_select);
				dayofweekCheckedArr[5] = 1;
			}
			else
			{ 
				plus_Friday.setBackgroundResource(R.drawable.plus_toggle);
				dayofweekCheckedArr[5] = 0;
			}
		}
	};

	public ToggleButton.OnCheckedChangeListener satON = new Switch.OnCheckedChangeListener(){
		public void onCheckedChanged(CompoundButton cb, boolean isChecking){
			if(isChecking)
			{ 
				plus_Saturday.setBackgroundResource(R.drawable.plus_toggle_select);
				dayofweekCheckedArr[6] = 1;
			}
			else
			{ 
				plus_Saturday.setBackgroundResource(R.drawable.plus_toggle);
				dayofweekCheckedArr[6] = 0;
			}
		}
	};

	//�ؽ�Ʈ�� ����
	private void updateDisplay()
	{
		plus_starttime.setText(String.format("%d hour %d min",  mHour, mMinute));
		plus_endtime.setText(String.format("%d hour %d min",  nHour, nMinute));
	}

	//TimePicker ������
	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener()
	{
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) 
		{
			mHour = hourOfDay;
			mMinute = minute;
			updateDisplay();
		}  
	};

	private TimePickerDialog.OnTimeSetListener nTimeSetListener = new TimePickerDialog.OnTimeSetListener()
	{
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) 
		{
			nHour = hourOfDay;
			nMinute = minute;
			updateDisplay();
		}  
	};

	@Override
	protected Dialog onCreateDialog(int id) 
	{
		switch (id) 
		{   
		case TIME_DIALOG_ID :
			return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute, false);

		case TIME_DIALOG_ID1 :
			return new TimePickerDialog(this, nTimeSetListener, nHour, nMinute, false);  
		}
		return null;
	}

	//Color toggle
	public ToggleButton.OnCheckedChangeListener redON = new Switch.OnCheckedChangeListener(){
		public void onCheckedChanged(CompoundButton cb, boolean isChecking){
			if(isChecking)
			{ tog_1red.setBackgroundResource(R.drawable.plus_tog_1red_select);}
			else
			{ tog_1red.setBackgroundResource(R.drawable.plus_tog_1red);}
		}
	};

	public ToggleButton.OnCheckedChangeListener hotpinkON = new Switch.OnCheckedChangeListener(){
		public void onCheckedChanged(CompoundButton cb, boolean isChecking){
			if(isChecking)
			{ tog_2hotpink.setBackgroundResource(R.drawable.plus_tog_2hotpink_select);}
			else
			{ tog_2hotpink.setBackgroundResource(R.drawable.plus_tog_2hotpink);}
		}
	};

	public ToggleButton.OnCheckedChangeListener orangeON = new Switch.OnCheckedChangeListener(){
		public void onCheckedChanged(CompoundButton cb, boolean isChecking){
			if(isChecking)
			{ tog_3orange.setBackgroundResource(R.drawable.plus_tog_3orange_select);}
			else
			{ tog_3orange.setBackgroundResource(R.drawable.plus_tog_3orange);}
		}
	};

	public ToggleButton.OnCheckedChangeListener lightorangeON = new Switch.OnCheckedChangeListener(){
		public void onCheckedChanged(CompoundButton cb, boolean isChecking){
			if(isChecking)
			{ tog_4lightorange.setBackgroundResource(R.drawable.plus_tog_4lightorange_select);}
			else
			{ tog_4lightorange.setBackgroundResource(R.drawable.plus_tog_4lightorange);}
		}
	};

	public ToggleButton.OnCheckedChangeListener yellowON = new Switch.OnCheckedChangeListener(){
		public void onCheckedChanged(CompoundButton cb, boolean isChecking){
			if(isChecking)
			{ tog_5yellow.setBackgroundResource(R.drawable.plus_tog_5yellow_select);}
			else
			{ tog_5yellow.setBackgroundResource(R.drawable.plus_tog_5yellow);}
		}
	};

	//Switch
	public Switch.OnCheckedChangeListener SWITCH = new Switch.OnCheckedChangeListener()
	{
		public void onCheckedChanged(CompoundButton cb, boolean isChecking)
		{
			String string = String.valueOf(isChecking);

			if(isChecking)
			{
				plus_spinner.setVisibility(View.VISIBLE);
				plus_message.setVisibility(View.INVISIBLE);
			}

			else
				plus_spinner.setVisibility(View.INVISIBLE);
			plus_message.setVisibility(View.INVISIBLE);
		}
	};

	private void xmlInitial(){
		//XML Object Initialization
		//title, professor
		plus_title = (EditText) findViewById(R.id.plus_title);
		plus_professor = (EditText) findViewById(R.id.plus_professor);

		//day
		plus_Sunday = (ToggleButton)findViewById(R.id.plus_Sunday);
		plus_Monday = (ToggleButton)findViewById(R.id.plus_Monday);
		plus_Tuesday = (ToggleButton)findViewById(R.id.plus_Tuesday);
		plus_Wednesday = (ToggleButton)findViewById(R.id.plus_Wednesday);
		plus_Thursday = (ToggleButton)findViewById(R.id.plus_Thursday);
		plus_Friday = (ToggleButton)findViewById(R.id.plus_Friday);
		plus_Saturday = (ToggleButton)findViewById(R.id.plus_Saturday);

		//Start,Finish time
		plus_pickTime1 = (Button)findViewById(R.id.plus_pickTime1);
		plus_pickTime2 = (Button)findViewById(R.id.plus_pickTime2);
		plus_starttime = (TextView)findViewById(R.id.plus_text1);
		plus_endtime = (TextView)findViewById(R.id.plus_text2);

		//day
		tog_1red = (ToggleButton)findViewById(R.id.tog_1red);
		tog_2hotpink = (ToggleButton)findViewById(R.id.tog_2hotpink);
		tog_3orange = (ToggleButton)findViewById(R.id.tog_3orange);
		tog_4lightorange = (ToggleButton)findViewById(R.id.tog_4lightorange);
		tog_5yellow = (ToggleButton)findViewById(R.id.tog_5yellow);

		//switch
		plus_switch = (Switch)findViewById(R.id.plus_switch); 

		//spinner
		plus_spinner = (Spinner) findViewById(R.id.plus_spinner);

		//plus_message
		plus_message = (EditText)findViewById(R.id.plus_message);

		//Close
		Close01 = (Button) findViewById(R.id.Close01);
		Close02 = (Button) findViewById(R.id.Close02);
		Close01.setText("Save");
		Close02.setText("Cancel");

		//switch
		plus_spinner.setVisibility(View.INVISIBLE);
		plus_message.setVisibility(View.INVISIBLE);

		//ChangeListener
		plus_switch.setOnCheckedChangeListener(SWITCH);

		plus_Sunday.setOnCheckedChangeListener(sunON);
		plus_Monday.setOnCheckedChangeListener(monON);
		plus_Tuesday.setOnCheckedChangeListener(tueON);
		plus_Wednesday.setOnCheckedChangeListener(wedON);
		plus_Thursday.setOnCheckedChangeListener(thrON);
		plus_Friday.setOnCheckedChangeListener(friON);
		plus_Saturday.setOnCheckedChangeListener(satON);

		tog_1red.setOnCheckedChangeListener(redON);
		tog_2hotpink.setOnCheckedChangeListener(hotpinkON);
		tog_3orange.setOnCheckedChangeListener(orangeON);
		tog_4lightorange.setOnCheckedChangeListener(lightorangeON);
		tog_5yellow.setOnCheckedChangeListener(yellowON);
	}

	private String getCurrentTimestamp(){
		curCalendar = Calendar.getInstance();
		String curTimestamp = "";
		curTimestamp += curCalendar.get(Calendar.YEAR)+"-";
		curTimestamp += (curCalendar.get(Calendar.MONTH)+1)+"-";
		curTimestamp += curCalendar.get(Calendar.DAY_OF_MONTH)+" ";
		curTimestamp += curCalendar.get(Calendar.HOUR_OF_DAY)+":";
		curTimestamp += curCalendar.get(Calendar.MINUTE)+":";
		curTimestamp += curCalendar.get(Calendar.SECOND)+":";
		curTimestamp += curCalendar.get(Calendar.MILLISECOND)+"";
		return curTimestamp;
	}

	private String getSetStartTimestamp(int dayofweek, int mHour, int mMinute){
		setStartCalendar = Calendar.getInstance();
		String setStartTimestamp = "";
		setStartCalendar.set(Calendar.DAY_OF_WEEK, dayofweek+1);
		setStartCalendar.set(Calendar.HOUR_OF_DAY, mHour);
		setStartCalendar.set(Calendar.MINUTE, mMinute);
		setStartCalendar.set(Calendar.SECOND, 0);
		setStartCalendar.set(Calendar.MILLISECOND, 0);
		setStartTimestamp += setStartCalendar.get(Calendar.YEAR)+"-";
		setStartTimestamp += (setStartCalendar.get(Calendar.MONTH)+1)+"-";
		setStartTimestamp += setStartCalendar.get(Calendar.DAY_OF_MONTH)+" ";
		setStartTimestamp += setStartCalendar.get(Calendar.HOUR_OF_DAY)+":";
		setStartTimestamp += setStartCalendar.get(Calendar.MINUTE)+":";
		setStartTimestamp += setStartCalendar.get(Calendar.SECOND)+":";
		setStartTimestamp += setStartCalendar.get(Calendar.MILLISECOND)+"";
		return setStartTimestamp;
	}

	private String getSetEndTimestamp(int dayofweek, int mHour, int mMinute){
		setEndCalendar = Calendar.getInstance();
		String setEndTimestamp = "";
		setEndCalendar.set(Calendar.DAY_OF_WEEK, dayofweek+1);
		setEndCalendar.set(Calendar.HOUR_OF_DAY, nHour);
		setEndCalendar.set(Calendar.MINUTE, nMinute);
		setEndCalendar.set(Calendar.SECOND, 0);
		setEndCalendar.set(Calendar.MILLISECOND, 0);
		setEndTimestamp += setEndCalendar.get(Calendar.YEAR)+"-";
		setEndTimestamp += (setEndCalendar.get(Calendar.MONTH)+1)+"-";
		setEndTimestamp += setEndCalendar.get(Calendar.DAY_OF_MONTH)+" ";
		setEndTimestamp += setEndCalendar.get(Calendar.HOUR_OF_DAY)+":";
		setEndTimestamp += setEndCalendar.get(Calendar.MINUTE)+":";
		setEndTimestamp += setEndCalendar.get(Calendar.SECOND)+":";
		setEndTimestamp += setEndCalendar.get(Calendar.MILLISECOND)+"";
		return setEndTimestamp;
	}
	
	private void dayofweekInitial(int dayofweek){
		switch (dayofweek) {
		case 0:
			plus_Sunday.setChecked(true);
			break;
		case 1:
			plus_Monday.setChecked(true);
			break;
		case 2:
			plus_Tuesday.setChecked(true);
			break;
		case 3:
			plus_Wednesday.setChecked(true);
			break;
		case 4:
			plus_Thursday.setChecked(true);
			break;
		case 5:
			plus_Friday.setChecked(true);
			break;
		case 6:
			plus_Saturday.setChecked(true);
			break;
		}
	}
}

