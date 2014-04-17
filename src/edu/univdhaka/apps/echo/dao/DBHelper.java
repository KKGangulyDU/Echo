package edu.univdhaka.apps.echo.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DBHelper extends SQLiteOpenHelper {

	// Static Final Variable database meta information

	static final String DATABASE = "echo.db";
	static final int VERSION = 120;
	public static final String TABLE = "echoTable";
	static final String TABLE_DEPT = "dept";

	public static final String C_ID = "_id";
	public static final String C_EVENT = "post";
	public static final String C_IMAGE = "image";
	public static final String C_Date = "date";
	public static final String C_Type = "type";
	public static final String C_Latitude = "latitude";
	public static final String C_Longitude = "longitude";

	// Override constructor
	public DBHelper(Context context) {
		super(context, DATABASE, null, VERSION);

	}

	// Override onCreate method
	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL("CREATE TABLE " + TABLE + " ( " + C_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + C_EVENT + " TEXT, "
				+ C_Date + " TEXT, " + C_Type + " TEXT, " + C_IMAGE + " BLOB, "+C_Latitude+" TEXT, "+C_Longitude+" TEXT"+");");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		// Drop old version table
		db.execSQL("DROP TABLE IF EXISTS " + TABLE);

		// Create New Version table
		onCreate(db);
	}
}