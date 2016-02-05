package com.wangyi.define;

import java.util.ArrayList;

public class Response<T> {
	private String event;
	private String msg;
	private Object attr;
	private ArrayList<T> dataList;
	
	public String getEvent(){
		return event;
	}
	
	public String getMessage(){
		return msg;
	}
	
	public Object getAttr(){
		return attr;
	}
	
	public ArrayList<T> getDataList(){
		return dataList;
	}
	
	public void setDataList(ArrayList<T> dataList){
		this.dataList = dataList;
	}
	
	public void init(String event,String msg,Object attr,ArrayList<T> dataList){
		this.event = event;
		this.msg = msg;
		this.attr = attr;
		this.dataList = dataList;
	}
}
