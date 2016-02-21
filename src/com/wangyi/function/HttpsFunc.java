package com.wangyi.function;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.xutils.common.Callback;

import com.wangyi.define.BookData;
import com.wangyi.define.EventName;
import com.wangyi.define.UserInfo;
import com.wangyi.utils.HttpsUtils;
import com.wangyi.utils.ItOneUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

public class HttpsFunc {
	public static String host = "http://192.168.0.102:8080";
	
	private static final HttpsFunc INSTANCE = new HttpsFunc();
	
	private Handler handler = null;
	private Context context;
	private void HttpsFunc(){}
	
	public static HttpsFunc getInstance(){
		return INSTANCE;
	}
	
	public void init(Context context){
		this.context = context;
	}
	
	public void connect(Handler handler){
		this.handler = handler;
	}
	
	public void Login(String userID,String passWords){
		if(!isNetworkConnected()){
			ItOneUtils.showToast(context,"网络未连接");
			return;
		}
		handler.sendEmptyMessage(EventName.UI.START);
		Map<String,Object> map=new HashMap<>();
		map.put("userID", userID);
		map.put("passWords", passWords);
		HttpsUtils.Post(host+"/ItOne/LoginServlet", map, new Callback.CommonCallback<String>(){

			@Override
			public void onCancelled(CancelledException arg0) {}

			@Override
			public void onError(Throwable ex, boolean isCheck) {
				ItOneUtils.showToast(context,"服务器抽风中...");
			}

			@Override
			public void onFinished() {
				handler.sendEmptyMessage(EventName.UI.FINISH);
			}

			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub
				if(result.equals(EventName.Https.OK)){
					ItOneUtils.showToast(context,"登陆成功");
					visitUserInfo();
					UserManagerFunc.getInstance().setLoginStatus(true);
					handler.sendEmptyMessage(EventName.UI.SUCCESS);
				}else if(result.equals(EventName.Https.ERROR)){
					ItOneUtils.showToast(context,"用户名或密码错误");
				}
			}
			
		}); 
	}
	
	public void register(UserInfo user,String passWords){
		if(!isNetworkConnected()){
			ItOneUtils.showToast(context,"网络未连接");
			return;
		}
		
		Map<String,Object> map=new HashMap<>();
		map.put("userID", user.userID);
		map.put("passWords",passWords);
		map.put("userName", user.userName);
		map.put("province", user.province);
		map.put("city", user.city);
		map.put("university", user.university);
		map.put("faculty", user.faculty);
		map.put("occupation", user.occupation);
		map.put("imageUrl", user.imageUrl);
		map.put("file", new File(user.imageUrl));
		
		if(user.imageUrl!=null){
			HttpsUtils.UpLoadFile(host+"/ItOne/OpenFormServlet", map, new Callback.CommonCallback<String>(){

				@Override
				public void onCancelled(CancelledException arg0) {}

				@Override
				public void onError(Throwable ex, boolean isCheck) {
					ItOneUtils.showToast(context,"服务器抽风中...");
				}

				@Override
				public void onFinished() {
					handler.sendEmptyMessage(EventName.UI.FINISH);
				}

				@Override
				public void onSuccess(String result) {
					// TODO Auto-generated method stub
					if(result.equals(EventName.Https.OK)){
						ItOneUtils.showToast(context,"注册成功");
						visitUserInfo();
						UserManagerFunc.getInstance().setLoginStatus(true);
						handler.sendEmptyMessage(EventName.UI.SUCCESS);
					}else if(result.equals(EventName.Https.ERROR)){
						ItOneUtils.showToast(context,"用户名已经存在");
					}
				}
				
			});  
		}
		
	}

	public void searchBookByName(String bookName){
		if(!isNetworkConnected()){
			ItOneUtils.showToast(context,"网络未连接");
			return;
		}
		handler.sendEmptyMessage(EventName.UI.START);
		Map<String,Object> map=new HashMap<>();
		map.put("bookName", bookName);
		HttpsUtils.Post(host+"/ItOne/GetBook", map, new Callback.CommonCallback<ArrayList<BookData>>(){

			@Override
			public void onCancelled(CancelledException arg0) {}

			@Override
			public void onError(Throwable ex, boolean isCheck) {
				ItOneUtils.showToast(context,"服务器抽风中...");
			}

			@Override
			public void onFinished() {
				handler.sendEmptyMessage(EventName.UI.FINISH);
			}

			@Override
			public void onSuccess(ArrayList<BookData> result) {
				// TODO Auto-generated method stub
				BookManagerFunc.getInstance().setBooks(result);
				handler.sendEmptyMessage(EventName.UI.SUCCESS);
			}
			
		});
	}
	
	public void searchBooksBySubject(String subject){
		if(!isNetworkConnected()){
			ItOneUtils.showToast(context,"网络未连接");
			return;
		}
		handler.sendEmptyMessage(EventName.UI.START);
		Map<String,Object> map=new HashMap<>();
		map.put("subject", subject);
		HttpsUtils.Post(host+"/ItOne/GetBook", map, new Callback.CommonCallback<ArrayList<BookData>>(){

			@Override
			public void onCancelled(CancelledException arg0) {}

			@Override
			public void onError(Throwable ex, boolean isCheck) {
				ItOneUtils.showToast(context,"服务器抽风中...");
			}

			@Override
			public void onFinished() {
				handler.sendEmptyMessage(EventName.UI.FINISH);
			}

			@Override
			public void onSuccess(ArrayList<BookData> result) {
				// TODO Auto-generated method stub
				BookManagerFunc.getInstance().setBooks(result);
				handler.sendEmptyMessage(EventName.UI.SUCCESS);
			}
			
		});
	}
	
	private void visitUserInfo(){
		HttpsUtils.Post(host+"/ItOne/GetUserInfo", null, new Callback.CommonCallback<UserInfo>(){

			@Override
			public void onCancelled(CancelledException arg0) {}

			@Override
			public void onError(Throwable ex, boolean isCheck) {
				// TODO Auto-generated method stub
				ItOneUtils.showToast(context,"服务器抽风中...");
			}

			@Override
			public void onFinished() {}

			@Override
			public void onSuccess(UserInfo result) {
				// TODO Auto-generated method stub
				if(result != null){
					UserManagerFunc.getInstance().setUserInfo(result);
				}
			}
			
		}); 
	}
	
	private boolean isNetworkConnected() {
		if (context != null) {
		ConnectivityManager mConnectivityManager = (ConnectivityManager) context
							.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}
}
