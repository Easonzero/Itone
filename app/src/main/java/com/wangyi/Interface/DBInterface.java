package com.wangyi.Interface;

import com.wangyi.define.LessonData;
import com.wangyi.define.Response;

public interface DBInterface<T> {
	DBInterface open();
	void add(Response<T> response);
	void delete(Response<T> response);
	Response<T> find(Response<T> response);
	void change(Response<T> response);
	void close();
}
