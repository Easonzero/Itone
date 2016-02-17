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
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.xutils.view.annotation.*;

import com.wangyi.define.UserInfo;
import com.wangyi.utils.MyHttps;
import com.wangyi.utils.PreferencesReader;
import com.wangyi.view.BaseActivity;
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

@ContentView(R.layout.login)
public class LoginActivity extends BaseActivity {
	@ViewInject(R.id.editUserName)
	private EditText userName;
	@ViewInject(R.id.editPassWords)
	private EditText passWords;
	
	private Handler handler = new Handler(){
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
	Map<String, String> userInfo = new HashMap<String, String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Event(R.id.close)
	private void onCloseClick(View view){
		LoginActivity.this.finish();
	}
	
	@Event(R.id.register)
	private void onRegisterClick(View view){
		if(isNetworkConnected(LoginActivity.this)){
			Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
			startActivity(intent);
		}
		else{
			Toast.makeText(LoginActivity.this, "网络未连接", 5000).show();
		}
	}
	
	@Event(R.id.confirm)
	private void onConfirmClick(View view){
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
}
