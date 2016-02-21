package com.wangyi.utils;

import java.lang.reflect.Type;

import org.xutils.http.app.ResponseParser;
import org.xutils.http.request.UriRequest;

import com.google.gson.Gson;

public class JsonResponseParser implements ResponseParser {

	@Override
	public void checkResponse(UriRequest request) throws Throwable {
		// TODO Auto-generated method stub
	}

	@Override
	public Object parse(Type resultType, Class<?> resultClass, String result) throws Throwable {
		// TODO Auto-generated method stub
		return new Gson().fromJson(result, resultClass);
	}

}
