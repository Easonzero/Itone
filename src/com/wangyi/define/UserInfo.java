package com.wangyi.define;

import org.xutils.http.annotation.HttpResponse;
import com.wangyi.utils.JsonResponseParser;

@HttpResponse(parser = JsonResponseParser.class)
public class UserInfo {
	public String userID;
	public String userName;
	public String province;
	public String city;
	public String university;
	public String faculty;
	public String occupation;
	public String imageUrl;
}
