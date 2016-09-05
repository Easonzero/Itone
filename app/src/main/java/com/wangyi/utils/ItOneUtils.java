package com.wangyi.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import android.net.wifi.WifiManager;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.wangyi.define.bean.StringListItem;

public class ItOneUtils {
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	public static long getCurrentTiem(){
		return System.currentTimeMillis();
	}

	public static float convertDpToPixel(float dp, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return px;
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

	public static String[] parseMessage(String str){
		return str.split("@");
	}

	public static String generateMessage(String str1,String str2){
		return str1+"@"+str2;
	}

	public static ArrayList<StringListItem> search(ArrayList<StringListItem> origin, String param){
		if(param.equals("")) return origin;
		ArrayList<StringListItem> result = new ArrayList<>();
		for(StringListItem str:origin){
			if(str.name.contains(param)){
				result.add(str);
			}
		}
		return result;
	}
}
