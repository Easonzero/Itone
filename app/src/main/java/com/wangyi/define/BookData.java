package com.wangyi.define;

import org.xutils.http.annotation.HttpResponse;
import com.wangyi.function.funchelp.JsonResponseParser;

@HttpResponse(parser = JsonResponseParser.class)
public class BookData {
	public long id;
	public String bookName;
	public String subject;
	public String occupation;
	public String fromUniversity;
	public int downloadNumber;
	public String url;
}
