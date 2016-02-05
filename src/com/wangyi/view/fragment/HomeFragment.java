package com.wangyi.view.fragment;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

import com.artifex.mupdfdemo.MuPDFActivity;
import com.wangyi.adapter.BookAdapter;
import com.wangyi.adapter.LessonGridAdapter;
import com.wangyi.imp.database.DBBook;
import com.wangyi.imp.database.DBBookmark;
import com.wangyi.imp.database.DBLesson;
import com.wangyi.view.activity.ScheduleActivity;
import com.wangyi.widget.SwipeListView;
import com.wangyi.reader.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;

public class HomeFragment extends Fragment {

	ArrayList<String> names = null;
	ArrayList<String> paths = null;
	SwipeListView browseList = null;
	BookAdapter adapter = null;
	View view = null;
	LessonGridAdapter adapter_lesson = null;
	GridView lessonList = null;
	RelativeLayout noLessonItem = null;
	View lessonTable = null;
	Button myBook;
	Button readHistory;
	RelativeLayout bookHead;
	Context context;
	private int mCurIndicator = 0;
	private Handler handler;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_home, container, false); 
		context = this.getActivity();
		File file = new File("/sdcard/textBook/");
		if(!file.exists()){
			file.mkdirs();
		}
		
		initHandler();
		initBookView();
		initLessonView();
		
        return view;
	}
	
	private void initHandler(){
		handler = new Handler(){
			public void handleMessage(Message msg){
				if(msg.what == 0){
					showFileDir("/sdcard/textBook/");
				}
                super.handleMessage(msg);
            }
        };
	}
	
	private void initBookView(){
		myBook = (Button) view.findViewById(R.id.mybook);
		readHistory = (Button) view.findViewById(R.id.read_history);
		bookHead = (RelativeLayout) view.findViewById(R.id.book_head);
		
		setMybookConsole(mCurIndicator);
		showFileDir("/sdcard/textBook/");
		
		myBook.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				setMybookConsole(0);
				bookHead.setBackgroundResource(R.drawable.ic_mybook);
				showFileDir("/sdcard/textBook/");
			}
			
		});
		
		
		readHistory.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				setMybookConsole(1);
				bookHead.setBackgroundResource(R.drawable.ic_readhistory);
				showHistoryDir();
			}
			
		});
	}

	private void initLessonView(){
		lessonTable = (View) view.findViewById(R.id.lesson_table);
		lessonList = (GridView) view.findViewById(R.id.today_lesson);
		adapter_lesson = new LessonGridAdapter(context);
		noLessonItem = (RelativeLayout) view.findViewById(R.id.lessonNoItem);
		
		if(adapter_lesson.getCount() != 0){
			lessonList.setAdapter(adapter_lesson);
		}
		else{
			lessonList.setVisibility(View.GONE);
			noLessonItem.setVisibility(View.VISIBLE);
		}
		
		lessonTable.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(HomeFragment.this.getActivity().getApplicationContext(),ScheduleActivity.class);
				startActivity(intent);
			}
			
		});
		
		browseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	        @Override
	        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	        	File file = new File(paths.get(position));
				Uri uri = Uri.fromFile(file);
				Intent intent = new Intent(HomeFragment.this.getActivity().getApplicationContext(),MuPDFActivity.class);
				intent.setAction(Intent.ACTION_VIEW);
				intent.setData(uri);
				startActivity(intent);
	        }
	    });
	}
	
	private void showFileDir(String path , FileFilter filefilter){  
		names = new ArrayList<String>();  
		paths = new ArrayList<String>();  
		File file = new File(path);  
		File[] files = file.listFiles(filefilter);  
		  
		for (File f : files){  
		names.add(f.getName());  
		paths.add(f.getPath());  
		}
	}
	
	private void showFileDir(String path){
		names = new ArrayList<String>();  
		paths = new ArrayList<String>();  
		File file = new File(path);  
		File[] files = file.listFiles();  
		  
		for (File f : files){  
		names.add(f.getName());  
		paths.add(f.getPath());  
		}
		
		browseList = (SwipeListView) view.findViewById(R.id.item_listview);
		
		adapter = new BookAdapter(context, names, paths,browseList.getRightViewWidth(),
	            new BookAdapter.IOnItemRightClickListener() {
	                @Override
	                public void onRightClick(View v, int position) {
	                        // TODO Auto-generated method stub
	                 File file = new File(paths.get(position));
	                 file.delete();
	                 DBBook book = new DBBook(context).open();
	                 DBBookmark bookMark = new DBBookmark(context).open();
	                 book.deleteBook(names.get(position));
	                 bookMark.deleteAllFormBookName(names.get(position));
	                 book.close();
	                 bookMark.close();
	                 handler.sendEmptyMessage(0);
	             }
	        });
			
			browseList.setAdapter(adapter);
	}
	
	private void showHistoryDir(){
			browseList.setAdapter(null);
	}
	
	private void setMybookConsole(int which){
		switch (mCurIndicator) {
		case 0:
			myBook.setBackgroundResource(R.drawable.mybook_normal);
			break;
		case 1:
			readHistory.setBackgroundResource(R.drawable.readhistory_normal);
			break;
		}
		
		switch (which) {
		case 0:
			myBook.setBackgroundResource(R.drawable.mybook_select);
			break;
		case 1:
			readHistory.setBackgroundResource(R.drawable.readhistory_select);
			break;
		}

		mCurIndicator = which;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		//initLessonView(view);
	}
	
}
