package com.wangyi.imp.database;

import java.util.ArrayList;

import com.wangyi.Interface.DBInterface;
import com.wangyi.define.BookData;
import com.wangyi.define.EventName;
import com.wangyi.define.LessonData;
import com.wangyi.define.Response;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBBook implements DBInterface<BookData>{
	public static final String DATABASE_NAME = "ItOneBook.db";
	public static final String KEY_BOOKNAME = "bookname";
	public static final String KEY_SUBJECT = "subject";
	public static final String KEY_OCCUPATION = "occupation";
	public static final String KEY_FROMUNIVERSITY = "fromuniversity";
	public static final String KEY_DOWNLOADNUMBER = "downloadnumber";
	public static final String KEY_COUNT = "count";
	public static final String KEY_FILELENGTH = "filelength";
	public static final String KEY_URL = "url";
	private static final int DB_VERSION = 1;
	private static final String TABLE_BOOK = "tablebook";
	
	private static final String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_BOOK + " ("
			+ KEY_BOOKNAME + " TEXT, "
			+ KEY_SUBJECT+ " TEXT, " 
			+ KEY_OCCUPATION + " TEXT, "
			+ KEY_FROMUNIVERSITY + " TEXT, "
			+ KEY_DOWNLOADNUMBER + " INTEGER, "
			+ KEY_COUNT + " INTEGER, "
			+ KEY_FILELENGTH + " INTEGER)";
	
	private final Context context;
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;
	
	public DBBook(Context ctx) {
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}

	public DBBook open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		DBHelper.close();
	}
	
	public void addBook(String bookName, String subject, String occupation, String fromUniversity,int downloadNumber,long count,long fileLength,String url){
		ContentValues cv = new ContentValues();
		cv.put(KEY_BOOKNAME, bookName);
		cv.put(KEY_FROMUNIVERSITY, fromUniversity);
		cv.put(KEY_SUBJECT, subject);
		cv.put(KEY_DOWNLOADNUMBER, downloadNumber);
		cv.put(KEY_OCCUPATION, occupation);
		cv.put(KEY_COUNT, count);
		cv.put(KEY_FILELENGTH, fileLength);
		cv.put(KEY_URL,url);
		db.insert(TABLE_BOOK, null, cv);
	}
	
	public void setBookCount(String url,long count){
		ContentValues values = new ContentValues();
		values.put(KEY_COUNT, count);
		db.update(TABLE_BOOK, values, KEY_URL + "=?", new String[]{url});
	}
	
	public void deleteBook(String bookName) {
		db.delete(TABLE_BOOK,  KEY_BOOKNAME + "= ?", new String[] {bookName});
	}
	
	public ArrayList<BookData> getFromBookName(String bookName) {
		ArrayList<BookData> books = new ArrayList<BookData>();
		Cursor cur = null;
		try{
			cur = db.query(true, TABLE_BOOK, new String[] { KEY_BOOKNAME, KEY_SUBJECT, KEY_OCCUPATION, KEY_FROMUNIVERSITY,
			KEY_DOWNLOADNUMBER, KEY_COUNT, KEY_FILELENGTH, KEY_URL}, KEY_BOOKNAME + "= ?", new String[] {bookName}, null, null,KEY_BOOKNAME,null);
		}catch(Exception e){}
		if (cur != null) {
			if (cur.moveToFirst()) {
				do {
					BookData b = new BookData();
					b.bookName = cur.getString(0);
					b.subject = cur.getString(1);
					b.occupation = cur.getString(2);
					b.fromUniversity = cur.getString(3);
					b.downloadNumber = cur.getInt(4);
					b.count = cur.getLong(5);
					b.fileLength = cur.getLong(6);
					b.url = cur.getString(7);
					books.add(b);
				} while (cur.moveToNext());
			}
			return books;
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
	public void add(Response<BookData> response) {
		// TODO Auto-generated method stub
		String event = response.getEvent();
		BookData book = response.getDataList().get(0);
		if(event.equals(EventName.BookFunc.ADDBOOK)){
			addBook(book.bookName, book.subject, book.occupation, book.fromUniversity,book.downloadNumber,book.count,book.fileLength,book.url);
		}
	}

	@Override
	public void delete(Response<BookData> response) {
		// TODO Auto-generated method stub
		String event = response.getEvent();
		BookData book = response.getDataList().get(0);
		if(event.equals(EventName.BookFunc.DELETEBOOK)){
			deleteBook(book.bookName);
		}
	}

	@Override
	public Response<BookData> find(Response<BookData> response) {
		// TODO Auto-generated method stub
		String event = response.getEvent();
		BookData book = response.getDataList().get(0);
		ArrayList<BookData> books = null;
		if(event.equals(EventName.BookFunc.FINDBYNAME)){
			books = getFromBookName(book.bookName);
		}
		response.setDataList(books);
		return response;
	}

	@Override
	public void change(Response<BookData> response) {
		// TODO Auto-generated method stub
		String event = response.getEvent();
		BookData book = response.getDataList().get(0);
		if(event.equals(EventName.BookFunc.CHANGECOUNT)){
			setBookCount(book.url,book.count);
		}
	}
}
