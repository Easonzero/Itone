package com.wangyi.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.artifex.mupdfdemo.MuPDFCore;
import com.wangyi.define.bean.BookData;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import com.wangyi.function.BookManagerFunc;
import org.xutils.x;

public class PreferencesReader {
	public static int getVersionCode(Context context){
		try {

			PackageManager pm = context.getPackageManager();
			PackageInfo pinfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
			return pinfo.versionCode;
		} catch (PackageManager.NameNotFoundException e) {
		}
		return 50;
	}

	public static void saveUser(String id,String pw){
		if(id == null ||id.equals("")) return;
		SharedPreferences prefs = x.app().getSharedPreferences("user",Context.MODE_PRIVATE);
		SharedPreferences.Editor edit = prefs.edit();
		edit.putString("id", id);
		edit.putString("pw",pw);
		edit.commit();
	}

	public static String[] getUser(){
		SharedPreferences prefs = x.app().getSharedPreferences("user",Context.MODE_PRIVATE);
		String[] user = new String[2];
		user[0] = prefs.getString("id","none");
		user[1] = prefs.getString("pw","none");
		return user;
	}

	public static void saveApplicationSetting(boolean[] setting){
		SharedPreferences prefs = x.app().getSharedPreferences("setting",Context.MODE_PRIVATE);
		SharedPreferences.Editor edit = prefs.edit();
		edit.putBoolean("autoupdate", setting[0]);
		edit.putBoolean("wifidownload",setting[1]);
		edit.commit();
	}

	public static boolean[] getApplicationSetting(){
		SharedPreferences prefs = x.app().getSharedPreferences("setting",Context.MODE_PRIVATE);
		boolean[] setting = new boolean[2];
		setting[0] = prefs.getBoolean("autoupdate",false);
		setting[1] = prefs.getBoolean("wifidownload",false);
		return setting;
	}

    public static int[] getScheduleData(){
		Date date=new Date();
        int[] data = new int[2];
		data[0] = 1;
		if(date.getMonth()>7)
        	data[1] = 36;
		else if(date.getMonth()<7)
			data[1] = 10;
		else data[1] = 0;
        return data;
    }

	public static void saveBookHistory(Activity act,ArrayList<BookData> bdh){
		SharedPreferences prefs = act.getSharedPreferences("bookhistory",Context.MODE_PRIVATE);
		SharedPreferences.Editor edit = prefs.edit();
		Set<String> book_Name = new HashSet();
		for(BookData bditem:bdh){
			book_Name.add(bditem.bookName);
		}
		edit.putStringSet("book_Name", book_Name);
		edit.commit();
	}
	
	public static ArrayList<BookData> getBookHistory(Activity act){
		SharedPreferences prefs = act.getSharedPreferences("bookhistory",Context.MODE_PRIVATE);
		ArrayList<BookData> bd = new ArrayList<BookData>();
		Set<String> book_Names = prefs.getStringSet("book_Name", null);
		if(book_Names != null){
			String[] book_Name = (String[])(book_Names.toArray(new String[book_Names.size()]));
			for(int i = 0;i < book_Name.length;i++){
				BookData bditem = new BookData();
				bditem.bookName = book_Name[i];
				bditem.url = BookManagerFunc.FILEPATH+book_Name[i];
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
}
