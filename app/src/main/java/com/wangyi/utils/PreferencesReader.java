package com.wangyi.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.artifex.mupdfdemo.MuPDFCore;
import com.wangyi.define.BookData;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

public class PreferencesReader {
    public static void saveScheduleData(Activity act,int initWeek,int initDate){
        SharedPreferences prefs = act.getSharedPreferences("schedule",Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putInt("initWeek", initWeek);
        edit.putInt("initDate",initDate);
        edit.commit();
    }

    public static int[] getScheduleData(Activity act){
        SharedPreferences prefs = act.getSharedPreferences("schedule",Context.MODE_PRIVATE);
        int[] data = new int[2];
        data[0] = prefs.getInt("initWeek",1);
        data[1] = prefs.getInt("initDate",5);
        return data;
    }

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

	public static String rePlaceString(String str){
		return str.replace("/", "_").replace(".", "_").replace(" ", "_");
	}
	
	public static String getDataDir(Context context) {
		String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
		return extStorageDirectory + "/Android/data/" + context.getPackageName();
	}
	
	public static void saveThemeMode(Activity act, int theme){
		SharedPreferences prefs = act.getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor edit = prefs.edit();
		edit.putInt("thememode", theme);
		edit.commit();
	}
	
	public static int getThemeMode(Activity act) {
		SharedPreferences prefs = act.getPreferences(Context.MODE_PRIVATE);
		return prefs.getInt("thememode", MuPDFCore.PAPER_NORMAL);
	}
	
	public static void savePageMode(Activity act, int pageMode) {
		SharedPreferences prefs = act.getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor edit = prefs.edit();
		edit.putInt("pagemode", pageMode);
		edit.commit();
	}
	
	public static int getPageMode(Activity act) {
		SharedPreferences prefs = act.getPreferences(Context.MODE_PRIVATE);
		return prefs.getInt("pagemode", MuPDFCore.AUTO_PAGE_MODE);
	}
	
	public static void saveShowCoverPageMode(Activity act, boolean showCover){
		SharedPreferences prefs = act.getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor edit = prefs.edit();
		edit.putBoolean("showcoverpage", showCover);
		edit.commit();
	}
	
	public static boolean isShowCoverPageMode(Activity act) {
		SharedPreferences prefs = act.getPreferences(Context.MODE_PRIVATE);
		return prefs.getBoolean("showcoverpage", true);
	}
	
	public static void saveReflowMode(Activity act, boolean reflow) {
		SharedPreferences prefs = act.getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor edit = prefs.edit();
		edit.putBoolean("reflowmode", reflow);
		edit.commit();
	}
	
	public static boolean isReflow(Activity act) {
		SharedPreferences prefs = act.getPreferences(Context.MODE_PRIVATE);
		return prefs.getBoolean("reflowmode", false);
	}
	
	public static boolean isFirstTime(Activity act){
		SharedPreferences prefs = act.getPreferences(Context.MODE_PRIVATE);
		boolean isFirst = prefs.getBoolean("isfirsttime", true);
		if(isFirst) {
			SharedPreferences.Editor edit = prefs.edit();
			edit.putBoolean("isfirsttime", false);
			edit.commit();
			return true;
		}
		return false;
	}
	
	public static String getCurrentPathBrowse(Activity act) {
		SharedPreferences prefs = act.getPreferences(Context.MODE_PRIVATE);
		return prefs.getString("currentpathbrowse", "");
	}
	
	public static void saveCurrentPathBrowse(Activity act, String path) {
		SharedPreferences prefs = act.getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor edit = prefs.edit();
		edit.putString("currentpathbrowse", path);
		edit.commit();
	}
	
	public static boolean getRemoveAdsPurchased(Activity act){
		SharedPreferences prefs = act.getPreferences(Context.MODE_PRIVATE);
		return prefs.getBoolean("removeadspurchased", false);
	}
	
	public static void saveRemoveAdsPurchased(Activity act, boolean purchased) {
		SharedPreferences prefs = act.getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor edit = prefs.edit();
		edit.putBoolean("removeadspurchased", purchased);
		edit.commit();
	}

}
