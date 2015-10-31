package com.wangyi.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class DBLesson {
	
	public static final String DATABASE_NAME = "ItOneLesson.db";
	public static final String KEY_ID = "_id";
	public static final String KEY_LESSONNAME = "lessonname";
	public static final String KEY_CLASSROOM = "classroom";
	public static final String KEY_TEACHER = "teacher";
	public static final String KEY_WEEKDAY = "weekday";
	public static final String KEY_FROMCLASS = "fromclass";
	public static final String KEY_TOCLASS = "toclass";
	public static final String KEY_WEEK1 = "week1";
	public static final String KEY_WEEK2 = "week2";
	public static final String KEY_WEEK3 = "week3";
	public static final String KEY_WEEK4 = "week4";
	public static final String KEY_WEEK5 = "week5";
	public static final String KEY_WEEK6 = "week6";
	public static final String KEY_WEEK7 = "week7";
	public static final String KEY_WEEK8 = "week8";
	public static final String KEY_WEEK9 = "week9";
	public static final String KEY_WEEK10 = "week10";
	public static final String KEY_WEEK11 = "week11";
	public static final String KEY_WEEK12 = "week12";
	public static final String KEY_WEEK13 = "week13";
	public static final String KEY_WEEK14 = "week14";
	public static final String KEY_WEEK15 = "week15";
	public static final String KEY_WEEK16 = "week16";
	public static final String KEY_WEEK17 = "week17";
	public static final String KEY_WEEK18 = "week18";
	public static final String KEY_WEEK19 = "week19";
	public static final String KEY_WEEK20 = "week20";
	public static final String KEY_WEEK21 = "week21";
	public static final String KEY_WEEK22 = "week22";
	public static final String KEY_WEEK23 = "week23";
	public static final String KEY_WEEK24 = "week24";
	public static final String KEY_WEEK25 = "week25";
	public static final String KEY_WEEKNUMDELAY = "weeknumdelay";
	private static final int DB_VERSION = 1;
	private static final String TABLE_LESSON = "lessontable";
	
	private static final String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_LESSON + " ("
			+ KEY_ID + " INTEGER PRIMARY KEY, " 
			+ KEY_LESSONNAME + " TEXT, "
			+ KEY_CLASSROOM+ " TEXT, "
			+ KEY_TEACHER + " TEXT, "
			+ KEY_WEEKDAY + " TEXT, "
			+ KEY_FROMCLASS + " TEXT, "
			+ KEY_TOCLASS + " TEXT, "
			+ KEY_WEEK1 + " TEXT, "
			+ KEY_WEEK2 + " TEXT, "
			+ KEY_WEEK3 + " TEXT, "
			+ KEY_WEEK4 + " TEXT, "
			+ KEY_WEEK5 + " TEXT, "
			+ KEY_WEEK6 + " TEXT, "
			+ KEY_WEEK7 + " TEXT, "
			+ KEY_WEEK8 + " TEXT, "
			+ KEY_WEEK9 + " TEXT, "
			+ KEY_WEEK10 + " TEXT, "
			+ KEY_WEEK11 + " TEXT, "
			+ KEY_WEEK12 + " TEXT, "
			+ KEY_WEEK13 + " TEXT, "
			+ KEY_WEEK14 + " TEXT, "
			+ KEY_WEEK15 + " TEXT, "
			+ KEY_WEEK16 + " TEXT, "
			+ KEY_WEEK17 + " TEXT, "
			+ KEY_WEEK18 + " TEXT, "
			+ KEY_WEEK19 + " TEXT, "
			+ KEY_WEEK20 + " TEXT, "
			+ KEY_WEEK21 + " TEXT, "
			+ KEY_WEEK22 + " TEXT, "
			+ KEY_WEEK23 + " TEXT, "
			+ KEY_WEEK24 + " TEXT, "
			+ KEY_WEEK25 + " TEXT, "
			+ KEY_WEEKNUMDELAY + " TEXT)";
	
	private final Context context;
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;
	
	public DBLesson(Context ctx) {
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}

	public DBLesson open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		DBHelper.close();
	}
	
	public void addLesson(String lessonName, String classRoom, String teacher, String weekDay, String fromClass, String toClass, String weeks[], String weeknumDelay){
		ContentValues cv = new ContentValues();
		cv.put(KEY_LESSONNAME, lessonName);
		cv.put(KEY_CLASSROOM, classRoom);
		cv.put(KEY_TEACHER, teacher);
		cv.put(KEY_WEEKDAY, weekDay);
		cv.put(KEY_FROMCLASS, fromClass);
		cv.put(KEY_TOCLASS, toClass);
		cv.put(KEY_WEEK1, weeks[0]);
		cv.put(KEY_WEEK2, weeks[1]);
		cv.put(KEY_WEEK3, weeks[2]);
		cv.put(KEY_WEEK4, weeks[3]);
		cv.put(KEY_WEEK5, weeks[4]);
		cv.put(KEY_WEEK6, weeks[5]);
		cv.put(KEY_WEEK7, weeks[6]);
		cv.put(KEY_WEEK8, weeks[7]);
		cv.put(KEY_WEEK9, weeks[8]);
		cv.put(KEY_WEEK10, weeks[9]);
		cv.put(KEY_WEEK11, weeks[10]);
		cv.put(KEY_WEEK12, weeks[11]);
		cv.put(KEY_WEEK13, weeks[12]);
		cv.put(KEY_WEEK14, weeks[13]);
		cv.put(KEY_WEEK15, weeks[14]);
		cv.put(KEY_WEEK16, weeks[15]);
		cv.put(KEY_WEEK17, weeks[16]);
		cv.put(KEY_WEEK18, weeks[17]);
		cv.put(KEY_WEEK19, weeks[18]);
		cv.put(KEY_WEEK20, weeks[19]);
		cv.put(KEY_WEEK21, weeks[20]);
		cv.put(KEY_WEEK22, weeks[21]);
		cv.put(KEY_WEEK23, weeks[22]);
		cv.put(KEY_WEEK24, weeks[23]);
		cv.put(KEY_WEEK25, weeks[24]);
		cv.put(KEY_WEEKNUMDELAY, weeknumDelay);
		db.insert(TABLE_LESSON, null, cv);
	}

	public void delete(String lessonName,String weekday) {
		db.delete(TABLE_LESSON,  KEY_LESSONNAME + "= ?" + " AND " + KEY_WEEKDAY + "= ?", new String[] {lessonName,weekday});
	}
	
	public ArrayList<LessonDate> getAllFromWeeknum(String week,int weeknum) {
		ArrayList<LessonDate> lessonDates = new ArrayList<LessonDate>();
		Cursor cur = null;
		try{
			cur = db.query(true, TABLE_LESSON, new String[] { KEY_LESSONNAME, KEY_CLASSROOM, KEY_TEACHER, KEY_WEEKDAY,
					KEY_FROMCLASS, KEY_TOCLASS, KEY_WEEK1, KEY_WEEK2, KEY_WEEK3, KEY_WEEK4, KEY_WEEK5, KEY_WEEK6, KEY_WEEK7,
					KEY_WEEK8, KEY_WEEK9, KEY_WEEK10, KEY_WEEK11, KEY_WEEK12, KEY_WEEK13, KEY_WEEK14, KEY_WEEK15, KEY_WEEK16, KEY_WEEK17,
					KEY_WEEK18, KEY_WEEK19, KEY_WEEK20, KEY_WEEK21, KEY_WEEK22, KEY_WEEK23, KEY_WEEK24, KEY_WEEK25,KEY_WEEKNUMDELAY}, "week" + weeknum + "= ?",
					new String[] {week}, null, null, KEY_WEEKDAY, null);
		}catch(Exception e){}
		if (cur != null) {
			if (cur.moveToFirst()) {
				do {
					LessonDate bm = new LessonDate();
					bm.lessonName = cur.getString(0);
					bm.classRoom= cur.getString(1);
					bm.teacher = cur.getString(2);
					bm.weekDay = cur.getString(3);
					bm.fromClass = cur.getString(4);
					bm.toClass = cur.getString(5);
					for(int i = 0; i < 25 ;i++){
						bm.weeks[i] = cur.getString(i + 6);
					}
					bm.weeknumDelay = cur.getString(31);
					lessonDates.add(bm);
				} while (cur.moveToNext());
				return lessonDates;
			}
		}
		return null;
	}
	
	public ArrayList<LessonDate> getAllFromWeeknum(String week,int weeknum,int today) {
		ArrayList<LessonDate> lessonDates = new ArrayList<LessonDate>();
		Cursor cur = null; 
		try{
			cur = db.query(true, TABLE_LESSON, new String[] { KEY_LESSONNAME, KEY_CLASSROOM, KEY_TEACHER, KEY_WEEKDAY,
					KEY_FROMCLASS, KEY_TOCLASS, KEY_WEEK1, KEY_WEEK2, KEY_WEEK3, KEY_WEEK4, KEY_WEEK5, KEY_WEEK6, KEY_WEEK7,
					KEY_WEEK8, KEY_WEEK9, KEY_WEEK10, KEY_WEEK11, KEY_WEEK12, KEY_WEEK13, KEY_WEEK14, KEY_WEEK15, KEY_WEEK16, KEY_WEEK17,
					KEY_WEEK18, KEY_WEEK19, KEY_WEEK20, KEY_WEEK21, KEY_WEEK22, KEY_WEEK23, KEY_WEEK24, KEY_WEEK25,KEY_WEEKNUMDELAY}, "week" + weeknum + "= ? AND "+KEY_WEEKDAY+"= ?",
					new String[] {week,today+""}, null, null, KEY_FROMCLASS, null);
		}catch(Exception e){}
		if (cur != null){
			if (cur.moveToFirst()) {
				do {
					LessonDate bm = new LessonDate();
					bm.lessonName = cur.getString(0);
					bm.classRoom= cur.getString(1);
					bm.teacher = cur.getString(2);
					bm.weekDay = cur.getString(3);
					bm.fromClass = cur.getString(4);
					bm.toClass = cur.getString(5);
					for(int i = 0; i < 25 ;i++){
						bm.weeks[i] = cur.getString(i + 6);
					}
					bm.weeknumDelay = cur.getString(31);
					lessonDates.add(bm);
				} while (cur.moveToNext());
				return lessonDates;
			}
		}
		return null;
	}
	
	public ArrayList<LessonDate> getAllFromWeeknum(String week,int weeknum,int today,int classnum) {
		ArrayList<LessonDate> lessonDates = new ArrayList<LessonDate>();
		Cursor cur = null;
		try{
			cur = db.query(true, TABLE_LESSON, new String[] { KEY_LESSONNAME, KEY_CLASSROOM, KEY_TEACHER, KEY_WEEKDAY,
			KEY_FROMCLASS, KEY_TOCLASS, KEY_WEEK1, KEY_WEEK2, KEY_WEEK3, KEY_WEEK4, KEY_WEEK5, KEY_WEEK6, KEY_WEEK7,
			KEY_WEEK8, KEY_WEEK9, KEY_WEEK10, KEY_WEEK11, KEY_WEEK12, KEY_WEEK13, KEY_WEEK14, KEY_WEEK15, KEY_WEEK16, KEY_WEEK17,
			KEY_WEEK18, KEY_WEEK19, KEY_WEEK20, KEY_WEEK21, KEY_WEEK22, KEY_WEEK23, KEY_WEEK24, KEY_WEEK25,KEY_WEEKNUMDELAY}, 
			"week" + weeknum + "= ? AND " + KEY_WEEKDAY+"= ? AND " + KEY_FROMCLASS + "= ?",new String[] {week,today+"",classnum+""}, null, null, KEY_FROMCLASS, null);
		}catch(Exception e){}
		if (cur != null) {
			if (cur.moveToFirst()) {
				do {
					LessonDate bm = new LessonDate();
					bm.lessonName = cur.getString(0);
					bm.classRoom= cur.getString(1);
					bm.teacher = cur.getString(2);
					bm.weekDay = cur.getString(3);
					bm.fromClass = cur.getString(4);
					bm.toClass = cur.getString(5);
					for(int i = 0; i < 25 ;i++){
						bm.weeks[i] = cur.getString(i + 6);
					}
					bm.weeknumDelay = cur.getString(31);
					lessonDates.add(bm);
				} while (cur.moveToNext());
				return lessonDates;
			}
		}
		return null;
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}

	}
}
