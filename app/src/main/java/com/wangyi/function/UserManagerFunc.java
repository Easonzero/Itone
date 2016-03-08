package com.wangyi.function;

import com.wangyi.define.SettingName;
import com.wangyi.define.UserInfo;
import com.wangyi.function.funchelp.Function;
import com.wangyi.utils.ItOneUtils;
import android.content.Context;
import com.wangyi.utils.PreferencesReader;

public class UserManagerFunc implements Function {
	private static final UserManagerFunc INSTANCE = new UserManagerFunc();
	private UserInfo userInfo;
	private boolean isLogin = false;
	private boolean[] setting;
	private Context context;

	public static UserManagerFunc getInstance(){
		return INSTANCE;
	}

	private UserManagerFunc(){}

	@Override
	public void init(Context context){
		this.context = context;
		setting = PreferencesReader.getApplicationSetting();
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

	public boolean getSetting(SettingName settingName){
		return setting[settingName.ordinal()];
	}

	public void setSetting(SettingName settingName,boolean value){
		setting[settingName.ordinal()] = value;
	}

	public void ifNeedSave(){
		PreferencesReader.saveApplicationSetting(setting);
	}

	public void clear(){
		if(userInfo!=null)
			userInfo = new UserInfo();
		isLogin = false;
	}
}
