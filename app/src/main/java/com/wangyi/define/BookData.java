package com.wangyi.define;

import org.xutils.http.annotation.HttpResponse;
import com.wangyi.function.funchelp.JsonResponseParser;

import java.io.Serializable;

@HttpResponse(parser = JsonResponseParser.class)
public class BookData implements Serializable{
	public long id;
	public String bookName;
	public String subject;
	public String occupation;
	public String fromUniversity;
	public String category;
	public float count;
	public int downloadNumber;
	public String uploader;
	public String pic;
	public String url;
}
