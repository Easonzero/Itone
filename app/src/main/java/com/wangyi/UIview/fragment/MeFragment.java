package com.wangyi.UIview.fragment;

import com.wangyi.UIview.activity.IdeaBackActivity;
import com.wangyi.UIview.activity.SettingActivity;
import org.xutils.x;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.*;

import com.wangyi.UIview.BaseFragment;
import com.wangyi.UIview.activity.DownloadActivity;
import com.wangyi.UIview.activity.LoginActivity;
import com.wangyi.define.EventName;
import com.wangyi.define.UserInfo;
import com.wangyi.function.HttpsFunc;
import com.wangyi.function.UserManagerFunc;
import com.wangyi.reader.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
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
	private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            if(msg.what == EventName.UI.SUCCESS){
            	UserInfo userInfo = UserManagerFunc.getInstance().getUserInfo();
            	userName.setText(userInfo.userName);
            	other.setText(userInfo.university + " " + userInfo.faculty + " ");
            	
            	ImageOptions options=new ImageOptions.Builder() 
            			.setLoadingDrawableId(R.drawable.ic_me) 
            			.setFailureDrawableId(R.drawable.ic_me)
            			.setUseMemCache(true)   
            			.setCircular(true)  
            			.setIgnoreGif(false)  
            			.build();
				x.image().bind(pic, HttpsFunc.host+userInfo.picture+"headPic.jpg",options);
            	
            	notLogin.setVisibility(View.GONE);
            	isLogin.setVisibility(View.VISIBLE);
            }
            super.handleMessage(msg);
        }
    };
    
	@Event(R.id.download)
	private void onDownloadClick(View view){
		Intent intent = new Intent(x.app(),DownloadActivity.class);
		startActivity(intent);
	}

	@Event(R.id.ideaback)
	private void onIdeabackClick(View view){
		Intent intent = new Intent(x.app(),IdeaBackActivity.class);
		startActivity(intent);
	}

	@Event(R.id.setting)
	private void onSettingClick(View view){
		Intent intent = new Intent(x.app(),SettingActivity.class);
		startActivity(intent);
	}

	@Event(R.id.back)
	private void onBackClick(View view){
		MeFragment.this.getActivity().finish();
	}
	
	@Event(R.id.menu_in)
	private void onLoginClick(View view){
		Intent intent = new Intent(x.app(),LoginActivity.class);
		startActivity(intent);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(UserManagerFunc.getInstance().isLogin())
			handler.sendEmptyMessage(EventName.UI.SUCCESS);
	}
}
