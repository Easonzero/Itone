package com.wangyi.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.artifex.mupdfdemo.MuPDFCore;
import com.wangyi.database.BookDate;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

public class PreferencesItOne {
	public static void saveIsLogin(Activity act, boolean is){
		SharedPreferences prefs = act.getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor edit = prefs.edit();
		edit.putBoolean("islogin", is);
		edit.commit();
	}
	
	public static boolean getIsLogin(Activity act){
		SharedPreferences prefs = act.getPreferences(Context.MODE_PRIVATE);
		return prefs.getBoolean("islogin", false); 
	}
	
	public static void saveDownloadList(Activity act,BookDate bdnew){
		ArrayList<BookDate> bd = getDownloadList(act);
		bd.add(bdnew);
		SharedPreferences prefs = act.getSharedPreferences("download",Context.MODE_PRIVATE);
		SharedPreferences.Editor edit = prefs.edit();
		Set<String> book_Name = new HashSet();
		Set<String> book_URL = new HashSet();
		for(BookDate bditem:bd){
			book_Name.add(bditem.bookName);
			book_URL.add(bditem.url);
		}
		edit.putStringSet("book_Name", book_Name);
		edit.putStringSet("book_URL", book_URL);
		edit.commit();
	}
	
	public static void deleteDownloadList(Activity act,int pos){
		ArrayList<BookDate> bd = getDownloadList(act);
		bd.remove(pos);
		SharedPreferences prefs = act.getSharedPreferences("download",Context.MODE_PRIVATE);
		SharedPreferences.Editor edit = prefs.edit();
		Set<String> book_Name = new HashSet();
		Set<String> book_URL = new HashSet();
		for(BookDate bditem:bd){
			book_Name.add(bditem.bookName);
			book_URL.add(bditem.url);
		}
		edit.putStringSet("book_Name", book_Name);
		edit.putStringSet("book_URL", book_URL);
		edit.commit();
	}
	
	public static ArrayList<BookDate> getDownloadList(Activity act){
		SharedPreferences prefs = act.getSharedPreferences("download",Context.MODE_PRIVATE);
		ArrayList<BookDate> bd = new ArrayList<BookDate>();
		Set<String> book_Names = prefs.getStringSet("book_Name", null);
		Set<String> book_URLs = prefs.getStringSet("book_URL", null);
		if(book_Names != null){
			String[] book_Name = (String[])(book_Names.toArray(new String[book_Names.size()]));
			String[] book_URL = (String[])(book_URLs.toArray(new String[book_URLs.size()]));
			for(int i = 0;i < book_Name.length;i++){
				BookDate bditem = new BookDate();
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
