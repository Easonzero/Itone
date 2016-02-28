package com.wangyi.function;

import java.io.File;
import java.util.ArrayList;
import com.wangyi.define.BookData;
import com.wangyi.define.EventName;
import com.wangyi.utils.PreferencesReader;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;

public class BookManagerFunc {
	private static final BookManagerFunc INSTANCE = new BookManagerFunc();
	public static final String FILEPATH = "/sdcard/textBook/";
	private ArrayList<BookData> books = new ArrayList();
	private ArrayList<BookData> historyCache;
	private Context context;
	private Handler handler;
	private BookManagerFunc(){}
	
	public void init(Context context){
		this.context = context;
		File file = new File(FILEPATH);
		if(!file.exists()){
			file.mkdirs();
		}
		
		//historyCache = PreferencesReader.getBookHistory((Activity)context);
	}
	
	public static BookManagerFunc getInstance(){
		return INSTANCE;
	}

	public BookManagerFunc connect(Handler handler){
        if(this.handler != handler)
		    this.handler = handler;
		return INSTANCE;
	}
	
	public BookData getBookData(int cur){
		return cur < books.size()?books.get(cur):null;
	}
	
	public void setBooks(ArrayList<BookData> books){
		this.books = books;
	}
	
	public int getBooksNum(){
		return books.size();
	}
	
	public void changeIfRead(BookData book){
		for(BookData a:historyCache){
			if(a.bookName.equals(book.bookName)){
				historyCache.remove(a);
			}
		}
		books.add(book);
	}
	
	public void saveIfNeed(){
		PreferencesReader.saveBookHistory((Activity)context, historyCache);
	}
	
	public void delete(int cur){
		File file = new File(books.get(cur).url);
        file.delete();
        for(BookData a:historyCache){
			if(a.bookName.equals(books.get(cur).bookName)){
				historyCache.remove(a);
			}
		}
        handler.sendEmptyMessage(EventName.UI.FINISH);
	}
	
	public void showFileDir(){
		clear();
		File[] files = new File(FILEPATH).listFiles();
		for (File f : files){
			BookData book = new BookData();
			book.bookName = f.getName();
			book.url = f.getPath();
			books.add(book);
		}
		handler.sendEmptyMessage(EventName.UI.FINISH);
	}
	
	public void showHistoryDir(){
		books = (ArrayList)historyCache.clone();
		handler.sendEmptyMessage(EventName.UI.FINISH);
	}
	
	public void clear(){
		if(books!=null)
			books.clear();
	}
}
