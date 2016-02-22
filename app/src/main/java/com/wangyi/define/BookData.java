package com.wangyi.define;

import org.xutils.http.annotation.HttpResponse;
import com.wangyi.utils.JsonResponseParser;

@HttpResponse(parser = JsonResponseParser.class)
public class BookData {
	public String bookName;
	public String subject;
	public String occupation;
	public String fromUniversity;
	public int downloadNumber;
	public String url;
}
