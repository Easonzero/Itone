package com.wangyi.fragment;

import com.wangyi.activity.DownloadActivity;
import com.wangyi.activity.LoginActivity;
import com.wangyi.database.UserInfo;
import com.wangyi.utils.ImageUtil;
import com.wangyi.utils.MyHttps;
import com.zreader.main.R;

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

public class MeFragment extends Fragment {
	Button login ;
	Context context;
	LinearLayout download;
	LinearLayout back;
	LinearLayout isLogin;
	LinearLayout notLogin;
	
	private Handler handler;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_me, container, false);
		context = this.getActivity();
		
		initView(view);
		initHandler();
		
        return view;
	}
	
	private void initView(View view){
		login = (Button) view.findViewById(R.id.menu_in);
		download = (LinearLayout) view.findViewById(R.id.download);
		back = (LinearLayout) view.findViewById(R.id.back);
		isLogin = (LinearLayout) view.findViewById(R.id.Login);
		notLogin = (LinearLayout) view.findViewById(R.id.notLogin);
		
		download.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context,DownloadActivity.class);
				startActivity(intent);
			}
			
		});
		
		back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				MeFragment.this.getActivity().finish();
			}
			
		});
		
		login.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context,LoginActivity.class);
				startActivity(intent);
			}
			
		});
	}
	
	private void initHandler(){
		handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what == 1){
                	TextView userName = (TextView) isLogin.findViewById(R.id.userName);
                	TextView other = (TextView) isLogin.findViewById(R.id.other);
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
                	ImageView pic = (ImageView) isLogin.findViewById(R.id.headPic);
                	pic.setImageBitmap(ImageUtil.toRoundBitmap((Bitmap)msg.obj));
                }
                super.handleMessage(msg);
            }
        };
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
