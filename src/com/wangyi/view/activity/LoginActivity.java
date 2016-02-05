package com.wangyi.view.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import com.wangyi.define.UserInfo;
import com.wangyi.utils.MyHttps;
import com.wangyi.utils.PreferencesItOne;
import com.wangyi.reader.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private EditText userName;
	private EditText passWords;
	private Button confirm;
	private Button close;
	private TextView register;
	private Handler handler;
	Map<String, String> userInfo = new HashMap<String, String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		userName = (EditText) findViewById(R.id.editUserName);
		passWords = (EditText) findViewById(R.id.editPassWords);
		register = (TextView) findViewById(R.id.register);
		confirm = (Button) findViewById(R.id.confirm);
		close = (Button) findViewById(R.id.close);
		
		initHandler();
		initListener();
		Log.v("LoginActivity","onCreate");
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
	}

	private void initListener(){
		close.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				LoginActivity.this.finish();
			}
			
		});
		
		register.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(isNetworkConnected(LoginActivity.this)){
					Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
					startActivity(intent);
				}
				else{
					Toast.makeText(LoginActivity.this, "网络未连接", 5000).show();
				}
			}
			
		});
		
		confirm.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(isNetworkConnected(LoginActivity.this)){
					new Thread() {  
	                    public void run() {
	                    	String url = "/ItOne/LoginServlet";
	                    	userInfo.put("userName", userName.getText().toString());
	        				userInfo.put("passWords", passWords.getText().toString());
	        				List<BasicNameValuePair> postData = new ArrayList<BasicNameValuePair>();
	        				for (Map.Entry<String, String> entry : userInfo.entrySet()) {
	        		            postData.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
	        		        }
	        				MyHttps myHttps = new MyHttps(url);
	        				HttpResponse httpResponse = myHttps.postLogin(postData);
	        				if(httpResponse != null){
	        					Header[] is = httpResponse.getHeaders("isChecked");
		        				if(is[0].getValue().equals("true")){
		        					Header[] id = httpResponse.getHeaders("sessionId");
		        					UserInfo.sessionId = id[0].getValue();
		        					Message message = new Message();
		        					message.what = 1;
		        					handler.sendMessage(message);
		        				}
		        				else{
		        					Message message = new Message();
		        					message.what = 2;
		        					handler.sendMessage(message);
		        				}
	        				}
	        				else{
	        					Message message = new Message();
	        					message.what = 3;
	        					handler.sendMessage(message);
	        				}
	                    }
	                }.start();
				}
				else{
					Toast.makeText(LoginActivity.this, "网络未连接", 5000).show();
				}
			}
        });
	}
	
	private void initHandler(){
		handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what == 1){
                	LoginActivity.this.finish();
                }
                else if(msg.what == 2){
                	Toast.makeText(LoginActivity.this, "登录失败", 5000).show();
                }
                else if(msg.what == 3){
                	Toast.makeText(LoginActivity.this,"服务器抽风中...",5000).show();
                }
                super.handleMessage(msg);
            }
        };
	}
	
	public boolean isNetworkConnected(Context context) {
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
	
	protected void setTranslucentStatus(boolean on) {
	    Window win = getWindow();
	    WindowManager.LayoutParams winParams = win.getAttributes();
	    final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
	    if (on) {
	        winParams.flags |= bits;
	    } else {
	        winParams.flags &= ~bits;
	    }
	    win.setAttributes(winParams);
	}
}
