package com.wangyi.function;

import com.wangyi.define.UserInfo;

public class UserManagerFunc {
	private static final UserManagerFunc INSTANCE = new UserManagerFunc();
	private UserInfo userInfo;
	private boolean isLogin = false;
	
	public static UserManagerFunc getInstance(){
		return INSTANCE;
	}
	
	private void UserManagerFunc(){}
	
	public void init(){}
	
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
		return userInfo;
	}
}
