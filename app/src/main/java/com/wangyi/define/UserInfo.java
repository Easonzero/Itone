package com.wangyi.define;

import org.xutils.http.annotation.HttpResponse;
import com.wangyi.function.funchelp.JsonResponseParser;

@HttpResponse(parser = JsonResponseParser.class)
public class UserInfo {
	public String id;
	public String passWords;
	public String userName;
	public String university;
	public String faculty;
	public String picture;
}
