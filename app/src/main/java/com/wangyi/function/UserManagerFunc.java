package com.wangyi.function;

import org.xutils.x;
import com.wangyi.define.UserInfo;
import com.wangyi.utils.ItOneUtils;
import android.content.Context;

public class UserManagerFunc {
	private static final UserManagerFunc INSTANCE = new UserManagerFunc();
	private UserInfo userInfo;
	private boolean isLogin = false;
	private Context context;

	public static UserManagerFunc getInstance(){
		return INSTANCE;
	}

	private void UserManagerFunc(){}

	public void init(Context context){
		this.context = context;
	}

	public void setLoginStatus(boolean isLogin){
		this.isLogin = isLogin;
	}

	public void setUserInfo(UserInfo userInfo){
		this.userInfo = userInfo;
	}

	public boolean isLogin(){
		return isLogin;
	}

	public UserInfo getUserInfo(){
		if(isLogin) return userInfo;
		else{
			ItOneUtils.showToast(context, "请先登录");
			return null;
		}
	}

	public void clear(){
		if(userInfo!=null)
			userInfo = new UserInfo();
		isLogin = false;
	}
}
