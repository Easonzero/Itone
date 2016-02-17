package com.wangyi.view.fragment;

import org.xutils.view.annotation.*;

import com.wangyi.define.UserInfo;
import com.wangyi.utils.ImageUtil;
import com.wangyi.utils.MyHttps;
import com.wangyi.view.BaseFragment;
import com.wangyi.view.activity.DownloadActivity;
import com.wangyi.view.activity.LoginActivity;
import com.wangyi.reader.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@ContentView(R.layout.fragment_me)
public class MeFragment extends BaseFragment {
	@ViewInject(R.id.Login)
	private LinearLayout isLogin;
	@ViewInject(R.id.notLogin)
	private LinearLayout notLogin;
	@ViewInject(R.id.headPic)
	private ImageView pic;
	@ViewInject(R.id.userName)
	private TextView userName;
	@ViewInject(R.id.other)
	private TextView other;
	private Context context;
	private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            if(msg.what == 1){
            	UserInfo userInfo = (UserInfo) msg.obj;
            	String otherString = userInfo.university + " " + userInfo.faculty + " " + userInfo.occupation;
            	userName.setText(userInfo.userName);
            	other.setText(otherString);
            	notLogin.setVisibility(View.GONE);
            	isLogin.setVisibility(View.VISIBLE);
            	if(!((UserInfo)msg.obj).imageUrl.equals("null")){
            		new Thread(){
            			public void run(){
            				String url0 = "/ItOne/DownloadServlet";
            				MyHttps mHttps = new MyHttps(url0);
            				if(UserInfo.sessionId != null){
            					Bitmap headPic = mHttps.getImage(UserInfo.sessionId);
            					Message message = new Message();
            					message.obj = headPic;
            					message.what = 3;
            					handler.sendMessage(message);
            				}
            			}
            		}.start();
            	}
            }
            else if(msg.what == 3){
            	pic.setImageBitmap(ImageUtil.toRoundBitmap((Bitmap)msg.obj));
            }
            super.handleMessage(msg);
        }
    };

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState){
		context = this.getActivity();
	}
	
	@Event(R.id.download)
	private void onDownloadClick(View view){
		Intent intent = new Intent(context,DownloadActivity.class);
		startActivity(intent);
	}
	
	@Event(R.id.back)
	private void onBackClick(View view){
		MeFragment.this.getActivity().finish();
	}
	
	@Event(R.id.menu_in)
	private void onLoginClick(View view){
		Intent intent = new Intent(context,LoginActivity.class);
		startActivity(intent);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		new Thread(){
			public void run(){
				UserInfo userInfo = new UserInfo();
				String url0 = "/ItOne/GetUserInfo";
				MyHttps mHttps = new MyHttps(url0);
				boolean isLogin = mHttps.isLogin(UserInfo.sessionId,userInfo);
				if(isLogin){
					Message message = new Message();
					message.obj = userInfo;
					message.what = 1;
					handler.sendMessage(message);
				}
				else{
					Message message = new Message();
					message.what = 2;
					handler.sendMessage(message);
				}
			}
			
		}.start();
	}
	
}
