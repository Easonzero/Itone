package com.wangyi.function.funchelp;

import java.lang.reflect.Type;
import java.util.List;
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
		if (resultClass == List.class) {
			return new Gson().fromJson(result,resultType);
		} else {
			return new Gson().fromJson(result, resultClass);
		}
	}

}
