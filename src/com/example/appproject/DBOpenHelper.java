package com.example.appproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper{

	public DBOpenHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("create table if not exists class(_classid integer primary key, _classname text not null, "
				+ "_teacher text, _starttime text not null, _endtime text not null, _dayofweek text not null, "
				+ "_color text not null, _msgyn integer not null, _msgindex integer, _msgcontent text)");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
