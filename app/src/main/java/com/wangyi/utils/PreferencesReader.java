package com.wangyi.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import com.wangyi.define.BookData;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

public class PreferencesReader {
	public static void saveBookHistory(Activity act,ArrayList<BookData> bdh){
		SharedPreferences prefs = act.getSharedPreferences("bookhistory",Context.MODE_PRIVATE);
		SharedPreferences.Editor edit = prefs.edit();
		Set<String> book_Name = new HashSet();
		Set<String> book_URL = new HashSet();
		for(BookData bditem:bdh){
			book_Name.add(bditem.bookName);
			book_URL.add(bditem.url);
		}
		edit.putStringSet("book_Name", book_Name);
		edit.putStringSet("book_URL", book_URL);
		edit.commit();
	}
	
	public static ArrayList<BookData> getBookHistory(Activity act){
		SharedPreferences prefs = act.getSharedPreferences("bookhistory",Context.MODE_PRIVATE);
		ArrayList<BookData> bd = new ArrayList<BookData>();
		Set<String> book_Names = prefs.getStringSet("book_Name", null);
		Set<String> book_URLs = prefs.getStringSet("book_URL", null);
		if(book_Names != null){
			String[] book_Name = (String[])(book_Names.toArray(new String[book_Names.size()]));
			String[] book_URL = (String[])(book_URLs.toArray(new String[book_URLs.size()]));
			for(int i = 0;i < book_Name.length;i++){
				BookData bditem = new BookData();
				bditem.bookName = book_Name[i];
				bditem.url = book_URL[i];
				bd.add(bditem);
			}
		}
		return bd;
	}

	public static void saveDownloadList(Activity act,BookData bdnew){
		ArrayList<BookData> bd = getDownloadList(act);
		bd.add(bdnew);
		SharedPreferences prefs = act.getSharedPreferences("download",Context.MODE_PRIVATE);
		SharedPreferences.Editor edit = prefs.edit();
		Set<String> book_Name = new HashSet();
		Set<String> book_URL = new HashSet();
		for(BookData bditem:bd){
			book_Name.add(bditem.bookName);
			book_URL.add(bditem.url);
		}
		edit.putStringSet("book_Name", book_Name);
		edit.putStringSet("book_URL", book_URL);
		edit.commit();
	}
	
	public static void deleteDownloadList(Activity act,int pos){
		ArrayList<BookData> bd = getDownloadList(act);
		bd.remove(pos);
		SharedPreferences prefs = act.getSharedPreferences("download",Context.MODE_PRIVATE);
		SharedPreferences.Editor edit = prefs.edit();
		Set<String> book_Name = new HashSet();
		Set<String> book_URL = new HashSet();
		for(BookData bditem:bd){
			book_Name.add(bditem.bookName);
			book_URL.add(bditem.url);
		}
		edit.putStringSet("book_Name", book_Name);
		edit.putStringSet("book_URL", book_URL);
		edit.commit();
	}
	
	public static ArrayList<BookData> getDownloadList(Activity act){
		SharedPreferences prefs = act.getSharedPreferences("download",Context.MODE_PRIVATE);
		ArrayList<BookData> bd = new ArrayList<BookData>();
		Set<String> book_Names = prefs.getStringSet("book_Name", null);
		Set<String> book_URLs = prefs.getStringSet("book_URL", null);
		if(book_Names != null){
			String[] book_Name = (String[])(book_Names.toArray(new String[book_Names.size()]));
			String[] book_URL = (String[])(book_URLs.toArray(new String[book_URLs.size()]));
			for(int i = 0;i < book_Name.length;i++){
				BookData bditem = new BookData();
				bditem.bookName = book_Name[i];
				bditem.url = book_URL[i];
				bd.add(bditem);
			}
		}
		return bd;
	}
}
