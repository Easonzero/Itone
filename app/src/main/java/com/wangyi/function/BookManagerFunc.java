package com.wangyi.function;

import java.io.File;
import java.util.ArrayList;
import com.wangyi.define.BookData;
import com.wangyi.define.EventName;
import com.wangyi.function.funchelp.Function;
import com.wangyi.utils.PreferencesReader;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;

public class BookManagerFunc implements Function {
	private static final BookManagerFunc INSTANCE = new BookManagerFunc();
	public static final String FILEPATH = "/sdcard/textBook/";
	private ArrayList<BookData> books = new ArrayList();
	private ArrayList<BookData> historyCache;
	private Context context;
	private Handler handler;
	private BookManagerFunc(){}

	@Override
	public void init(Context context){
		this.context = context;
		File file = new File(FILEPATH);
		if(!file.exists()){
			file.mkdirs();
		}
		
		historyCache = PreferencesReader.getBookHistory((Activity)context);
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

	public void insertBooks(ArrayList<BookData> books){
		if(this.books != null){
			this.books.addAll(books);
		}else{
			setBooks(books);
		}
	}
	
	public int getBooksNum(){
		return books.size();
	}
	
	public void changeIfRead(int position){
		deletehistory(position);
		historyCache.add(0,books.get(position));
	}
	
	public void saveIfNeed(){
		PreferencesReader.saveBookHistory((Activity)context, historyCache);
	}

	public void deletehistory(int cur){
		for(BookData a:historyCache){
			if(a.url.equals(books.get(cur).url)){
				historyCache.remove(a);
				break;
			}
		}
	}
	
	public void delete(int cur){
		File file = new File(books.get(cur).url);
        file.delete();
		deletehistory(cur);
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
		clear();
		books.addAll(historyCache);
		handler.sendEmptyMessage(EventName.UI.FINISH);
	}
	
	public void clear(){
		if(books!=null)
			books.clear();
	}
}
