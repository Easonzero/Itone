package com.wangyi.function;

import java.io.File;
import java.util.ArrayList;

import com.wangyi.Interface.DBInterface;
import com.wangyi.define.BookData;
import com.wangyi.define.LessonData;
import com.wangyi.define.Response;
import com.wangyi.imp.database.DBBook;
import com.wangyi.imp.database.DBBookmark;
import com.wangyi.imp.database.DBLesson;
import com.wangyi.utils.PreferencesReader;

import android.app.Activity;
import android.content.Context;

public class BookManagerFunc {
	private static final BookManagerFunc INSTANCE = new BookManagerFunc();
	private static final String FILEPATH = "/sdcard/textBook/";
	private DBInterface<BookData> bookDB;
	public BookData book = new BookData();
	public int cur;
	private File file;
	private ArrayList<String> names;
	private ArrayList<String> paths;
	private ArrayList<BookData> bookHistory;
	private int booknum = 0;
	private Context context;
	
	public void init(Context context){
		this.context = context;
		file = new File(FILEPATH);
		if(!file.exists()){
			file.mkdirs();
		}
		names = new ArrayList<String>();  
		paths = new ArrayList<String>();  
		bookHistory = PreferencesReader.getBookHistory((Activity)context);
	}
	
	public static BookManagerFunc getInstance(){
		return INSTANCE;
	}
	
	public int getBooknum(){
		return booknum;
	}
	
	public String getna(int pos){
		return names.get(pos);
	}
	
	public String getpa(int pos){
		return paths.get(pos);
	}
	
	public void changeIfRead(String name){
		for(BookData x:bookHistory){
			if(x.bookName.equals(name)){
				bookHistory.remove(book);
			}
		}
		bookHistory.add(book);
	}
	
	public Response<BookData> find(String event){
		Response<BookData> response = new Response();
		ArrayList<BookData> dataList = new ArrayList();
		BookData book = new BookData();
		dataList.add(book);
		response.init(event, null, null, dataList);
		bookDB = new DBBook(context).open();
		response = bookDB.find(response);
		bookDB.close();
		return response;
	}
	
	public void add(String event){
		Response<BookData> response = new Response();
		ArrayList<BookData> dataList = new ArrayList();
		dataList.add(book);
		response.init(event, null, null, dataList);
		bookDB = new DBBook(context).open();
		bookDB.add(response);
		bookDB.close();
	}
	
	public void delete(String event){
		Response<BookData> response = new Response();
		ArrayList<BookData> dataList = new ArrayList();
		dataList.add(book);
		response.init(event, null, null, dataList);
		File file = new File(paths.get(cur));
        file.delete();
		bookDB = new DBBook(context).open();
		bookDB.delete(response);
		bookDB.close();
		DBBookmark bookMark = new DBBookmark(context).open();
		bookMark.deleteAllFormBookName(names.get(cur));
		bookMark.close();
		for(BookData x:bookHistory){
			if(x.bookName.equals(book.bookName)){
				bookHistory.remove(book);
			}
		}
	}
	
	public void change(String event){
		Response<BookData> response = new Response();
		ArrayList<BookData> dataList = new ArrayList();
		dataList.add(book);
		response.init(event, null, null, dataList);
		//bookDB = new DBBook(context).open();
		bookDB.change(response);
		bookDB.close();
	}
	
	public void showFileDir(){
		File[] files = file.listFiles();
		
		names.clear();
		paths.clear();
		  
		for (File f : files){  
			names.add(f.getName());
			paths.add(f.getPath());
		}
		
		booknum = names.size();
	}
	
	public void showHistoryDir(){
		names.clear();
		paths.clear();
		
		int length = bookHistory.size();
		
		for(int i=0;i < length;i++){
			names.add(bookHistory.get(length-i).bookName);
			paths.add(bookHistory.get(length-i).url);
		}
		
		booknum = names.size();
	}
	
	public void finish(){
		PreferencesReader.saveBookHistory((Activity)context, bookHistory);
	}
}
