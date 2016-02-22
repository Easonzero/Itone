package com.wangyi.imp.database;

import java.util.ArrayList;

import com.wangyi.Interface.DBInterface;
import com.wangyi.define.EventName;
import com.wangyi.define.LessonData;
import com.wangyi.define.Response;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class DBLesson implements DBInterface<LessonData>{
	
	public static final String DATABASE_NAME = "ItOneLesson.db";
	public static final String KEY_ID = "_id";
	public static final String KEY_LESSONNAME = "lessonname";
	public static final String KEY_CLASSROOM = "classroom";
	public static final String KEY_TEACHER = "teacher";
	public static final String KEY_WEEKDAY = "weekday";
	public static final String KEY_FROMCLASS = "fromclass";
	public static final String KEY_TOCLASS = "toclass";
	public static final String KEY_WEEK = "week";
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
			+ KEY_WEEK + "1 TEXT, " + KEY_WEEK + "2 TEXT, " + KEY_WEEK + "3 TEXT, " + KEY_WEEK + "4 TEXT, " + KEY_WEEK + "5 TEXT, "
			+ KEY_WEEK + "6 TEXT, " + KEY_WEEK + "7 TEXT, " + KEY_WEEK + "8 TEXT, " + KEY_WEEK + "9 TEXT, " + KEY_WEEK + "10 TEXT, "
			+ KEY_WEEK + "11 TEXT, " + KEY_WEEK + "12 TEXT, " + KEY_WEEK + "13 TEXT, " + KEY_WEEK + "14 TEXT, " + KEY_WEEK + "15 TEXT, "
			+ KEY_WEEK + "16 TEXT, " + KEY_WEEK + "17 TEXT, " + KEY_WEEK + "18 TEXT, " + KEY_WEEK + "19 TEXT, " + KEY_WEEK + "20 TEXT, "
			+ KEY_WEEK + "21 TEXT, " + KEY_WEEK + "22 TEXT, " + KEY_WEEK + "23 TEXT, " + KEY_WEEK + "24 TEXT, " + KEY_WEEK + "25 TEXT, "
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
	
	private void addLesson(String lessonName, String classRoom, String teacher, String weekDay, String fromClass, String toClass, String weeks[], String weeknumDelay){
		ContentValues cv = new ContentValues();
		cv.put(KEY_LESSONNAME, lessonName);
		cv.put(KEY_CLASSROOM, classRoom);
		cv.put(KEY_TEACHER, teacher);
		cv.put(KEY_WEEKDAY, weekDay);
		cv.put(KEY_FROMCLASS, fromClass);
		cv.put(KEY_TOCLASS, toClass);
		for(int i=0;i < 25;i++){
			cv.put(KEY_WEEK+(i+1), weeks[i]);
		}
		cv.put(KEY_WEEKNUMDELAY, weeknumDelay);
		db.insert(TABLE_LESSON, null, cv);
	}

	private void delete(String lessonName,String weekDay) {
		db.delete(TABLE_LESSON,  KEY_LESSONNAME + "= ?" + " AND " + KEY_WEEKDAY + "= ?", new String[] {lessonName,weekDay});
	}
	
	private ArrayList<LessonData> getAllFromWeeknum(String week) {
		ArrayList<LessonData> lessonDatas = new ArrayList<LessonData>();
		Cursor cur = null;
		try{
			cur = db.query(true, TABLE_LESSON, new String[] { KEY_LESSONNAME, KEY_CLASSROOM, KEY_TEACHER, KEY_WEEKDAY,
					KEY_FROMCLASS, KEY_TOCLASS, KEY_WEEK+1, KEY_WEEK+2, KEY_WEEK+3, KEY_WEEK+4, KEY_WEEK+5, KEY_WEEK+6, KEY_WEEK+7,
					KEY_WEEK+8, KEY_WEEK+9, KEY_WEEK+10, KEY_WEEK+11, KEY_WEEK+12, KEY_WEEK+13, KEY_WEEK+14, KEY_WEEK+15, KEY_WEEK+16, KEY_WEEK+17,
					KEY_WEEK+18, KEY_WEEK+19, KEY_WEEK+20, KEY_WEEK+21, KEY_WEEK+22, KEY_WEEK+23, KEY_WEEK+24, KEY_WEEK+25,KEY_WEEKNUMDELAY}, "week" + week + "= ?",
					new String[] {"true"}, null, null, KEY_WEEKDAY, null);
		}catch(Exception e){}
		if (cur != null) {
			if (cur.moveToFirst()) {
				do {
					LessonData bm = new LessonData();
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
					lessonDatas.add(bm);
				} while (cur.moveToNext());
				return lessonDatas;
			}
		}
		return null;
	}
	
	private ArrayList<LessonData> getAllFromWeeknum(String week,String weekDay) {
		ArrayList<LessonData> lessonDatas = new ArrayList<LessonData>();
		Cursor cur = null; 
		try{
			cur = db.query(true, TABLE_LESSON, new String[] { KEY_LESSONNAME, KEY_CLASSROOM, KEY_TEACHER, KEY_WEEKDAY,
					KEY_FROMCLASS, KEY_TOCLASS, KEY_WEEK+1, KEY_WEEK+2, KEY_WEEK+3, KEY_WEEK+4, KEY_WEEK+5, KEY_WEEK+6, KEY_WEEK+7,
					KEY_WEEK+8, KEY_WEEK+9, KEY_WEEK+10, KEY_WEEK+11, KEY_WEEK+12, KEY_WEEK+13, KEY_WEEK+14, KEY_WEEK+15, KEY_WEEK+16, KEY_WEEK+17,
					KEY_WEEK+18, KEY_WEEK+19, KEY_WEEK+20, KEY_WEEK+21, KEY_WEEK+22, KEY_WEEK+23, KEY_WEEK+24, KEY_WEEK+25,KEY_WEEKNUMDELAY}, "week" + week + "= ? AND "+KEY_WEEKDAY+"= ?",
					new String[] {"true",weekDay}, null, null, KEY_FROMCLASS, null);
		}catch(Exception e){}
		if (cur != null){
			if (cur.moveToFirst()) {
				do {
					LessonData bm = new LessonData();
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
					lessonDatas.add(bm);
				} while (cur.moveToNext());
				return lessonDatas;
			}
		}
		return null;
	}
	
	private ArrayList<LessonData> getAllFromWeeknum(String week,String weekDay,String classnum) {
		ArrayList<LessonData> lessonDatas = new ArrayList<LessonData>();
		Cursor cur = null;
		try{
			cur = db.query(true, TABLE_LESSON, new String[] { KEY_LESSONNAME, KEY_CLASSROOM, KEY_TEACHER, KEY_WEEKDAY,
			KEY_FROMCLASS, KEY_TOCLASS, KEY_WEEK+1, KEY_WEEK+2, KEY_WEEK+3, KEY_WEEK+4, KEY_WEEK+5, KEY_WEEK+6, KEY_WEEK+7,
			KEY_WEEK+8, KEY_WEEK+9, KEY_WEEK+10, KEY_WEEK+11, KEY_WEEK+12, KEY_WEEK+13, KEY_WEEK+14, KEY_WEEK+15, KEY_WEEK+16, KEY_WEEK+17,
			KEY_WEEK+18, KEY_WEEK+19, KEY_WEEK+20, KEY_WEEK+21, KEY_WEEK+22, KEY_WEEK+23, KEY_WEEK+24, KEY_WEEK+25,KEY_WEEKNUMDELAY}, 
			KEY_WEEK + week + "= ? AND " + KEY_WEEKDAY+"= ? AND " + KEY_FROMCLASS + "= ?",new String[] {"true",weekDay,classnum}, null, null, KEY_FROMCLASS, null);
		}catch(Exception e){}
		if (cur != null) {
			if (cur.moveToFirst()) {
				do {
					LessonData bm = new LessonData();
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
					lessonDatas.add(bm);
				} while (cur.moveToNext());
				return lessonDatas;
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

	@Override
	public void add(Response<LessonData> response) {
		// TODO Auto-generated method stub
		String event = response.getEvent();
		LessonData lesson = response.getDataList().get(0);
		if(event.equals(EventName.ScheduleFunc.ADDLESSON)){
			addLesson(lesson.lessonName,lesson.classRoom,lesson.teacher,lesson.weekDay,lesson.fromClass,lesson.toClass,lesson.weeks,lesson.weeknumDelay);
		}
	}

	@Override
	public void delete(Response<LessonData> response) {
		// TODO Auto-generated method stub
		String event = response.getEvent();
		LessonData lesson = response.getDataList().get(0);
		if(event.equals(EventName.ScheduleFunc.DELELESSON)){
			delete(lesson.lessonName,lesson.weekDay);
		}
		
	}

	@Override
	public Response<LessonData> find(Response<LessonData> response) {
		// TODO Auto-generated method stub
		String event = response.getEvent();
		LessonData lesson = response.getDataList().get(0);
		String weeknum = response.getAttr()+"";
		ArrayList<LessonData> lessons = null;
		if(event.equals(EventName.ScheduleFunc.FINDBYCLASS))
			lessons = getAllFromWeeknum(weeknum,lesson.weekDay,lesson.fromClass);
		else if(event.equals(EventName.ScheduleFunc.FINDBYDAY))
			lessons = getAllFromWeeknum(weeknum,lesson.weekDay);
		else if(event.equals(EventName.ScheduleFunc.FINDBYWEEK))
			lessons = getAllFromWeeknum(weeknum);
		response.setDataList(lessons);
		return response;
	}

	@Override
	public void change(Response<LessonData> response) {
		// TODO Auto-generated method stub
		String event = response.getEvent();
		LessonData oldLesson = (LessonData)response.getAttr();
		LessonData newLesson = response.getDataList().get(0);
		if(event.equals(EventName.ScheduleFunc.CHANGELESSON)){
			delete(oldLesson.lessonName,oldLesson.weekDay);
			addLesson(newLesson.lessonName,newLesson.classRoom,newLesson.teacher,newLesson.weekDay,newLesson.fromClass,newLesson.toClass,newLesson.weeks,newLesson.weeknumDelay);
		}
	}
}
