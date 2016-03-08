package com.wangyi.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.net.wifi.WifiManager;
import android.view.LayoutInflater;
import android.view.View;
import com.wangyi.UIview.widget.PopupView;
import org.xutils.x;

import com.wangyi.reader.R;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.widget.Toast;

public class ItOneUtils {
	static List<File> listFile = new ArrayList<File>();
	static String fileType;
	public static int[] colorBg = { 
			R.color.md_light_green, 
			R.color.md_indigo,			
			R.color.md_cyam, 
			R.color.md_orange,			
			R.color.md_teal, 
			R.color.md_purple,
			R.color.md_blue,
			R.color.md_deep_orange,			
			R.color.md_light_blue, 
			R.color.md_green, 
			R.color.md_blue_gray,
			R.color.md_deep_purple, 
			R.color.md_red, 
			R.color.md_pink	
			};
	
	public static long getCurrentTiem(){
		return System.currentTimeMillis();
	}

	public static float convertDpToPixel(float dp, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return px;
	}

	public static List<File> searchFileFrom(String rootPath, String type) {

		listFile.clear();
		fileType = type;

		File rootFile = new File(rootPath);
		if (rootFile.isDirectory()) {
			searchDir(rootFile);
		}

		return listFile;
	}
	
	private static void searchDir(File dirFile){
		File[] files = dirFile.listFiles(); 
		if (files != null) {
			for (File file : files) {
				if (file.isDirectory()) {
					if (file.getName().subSequence(0, 1).equals(".") || file.getName().equals("Android")
							|| file.getPath().equalsIgnoreCase("/storage/emulated")) {

					} else {
						searchDir(file);
					}
				} else {
					if (file.getName().toLowerCase().contains(fileType.toLowerCase())) {
						listFile.add(file);
					}
				}
			}
		}
	}
	
	private static void searchDirSort(File dirFile) {
		File[] files = dirFile.listFiles();
		List<File> dirList = new ArrayList<File>();
		if (files != null) {
			for (File file : files) {
				if (file.isDirectory()) {
					dirList.add(file);
				} else {
					if (file.getName().toLowerCase().contains(fileType.toLowerCase())) {
						listFile.add(file);
					}
				}
			}
			for (File dir : dirList) {
				if (dir.getName().subSequence(0, 1).equals(".") || dir.getName().equals("Android")) {

				} else {
					searchDirSort(dir);
				}
			}
		}
	}
	
	public static String wordSpace(String source) {
		BreakIterator boundary = BreakIterator.getLineInstance(new Locale("th"));
		boundary.setText(source);		
	     int start = boundary.first();
	     StringBuffer wordbuffer = new StringBuffer("");
	     for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary.next()) {
	    	 wordbuffer.append(source.substring(start, end)+"\u200b");
//	    	 wordbuffer.append(source.substring(start, end)+"\ufeff");
	     }
	     return wordbuffer.toString();
	 }	
	
	public static void copy(File src, File dst) throws IOException {
	    InputStream in = new FileInputStream(src);
	    OutputStream out = new FileOutputStream(dst);

	    // Transfer bytes from in to out
	    byte[] buf = new byte[1024];
	    int len;
	    while ((len = in.read(buf)) > 0) {
	        out.write(buf, 0, len);
	    }
	    in.close();
	    out.close();
	}
	
	public static boolean isIntNum(String strNum) {
		boolean ret = true;
		try {
			Integer.parseInt(strNum);
		} catch (NumberFormatException e) {
			ret = false;
		}
		return ret;
	}
	
	public static boolean renameThumbnail(){
		
		return false;
	}
	
	public static void showToast(final Context context,
            final CharSequence text) {
		Looper mainLooper = Looper.getMainLooper();
		Runnable r = new Runnable() {
			@Override
			public void run() {
				Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
			}
		};
		if (Looper.myLooper() != mainLooper) {
			Handler handler = new Handler(mainLooper);
			handler.post(r);
		} else {
			r.run();
		}
	}

	public static boolean getWifiState(Context context){
		WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
		if(wifiManager != null){
			return wifiManager.getWifiState() == wifiManager.WIFI_STATE_ENABLED;
		}
		return false;
	}
}
