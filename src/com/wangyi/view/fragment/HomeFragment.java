package com.wangyi.view.fragment;

import java.io.File;
import java.util.ArrayList;

import org.xutils.view.annotation.*;

import com.artifex.mupdfdemo.MuPDFActivity;
import com.wangyi.adapter.BookAdapter;
import com.wangyi.adapter.LessonGridAdapter;
import com.wangyi.define.EventName;
import com.wangyi.function.BookManagerFunc;
import com.wangyi.view.BaseFragment;
import com.wangyi.view.activity.ScheduleActivity;
import com.wangyi.widget.SwipeListView;
import com.wangyi.reader.R;

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

@ContentView(R.layout.fragment_home)
public class HomeFragment extends BaseFragment {
	@ViewInject(R.id.today_lesson)
	GridView lessonList;
	@ViewInject(R.id.lessonNoItem)
	RelativeLayout noLessonItem;
	@ViewInject(R.id.mybook)
	Button myBook;
	@ViewInject(R.id.read_history)
	Button readHistory;
	@ViewInject(R.id.item_listview)
	SwipeListView browseList;
	@ViewInject(R.id.book_head)
	RelativeLayout bookHead;
	BookAdapter adapter = null;
	LessonGridAdapter adapter_lesson = null;
	Context context;
	private int mCurIndicator = 0;
	private Handler handler = new Handler(){
		public void handleMessage(Message msg){
			if(msg.what == 0){
				BookManagerFunc.getInstance().showFileDir();
				adapter.notifyDataSetChanged();
			}else if(msg.what == 1){
				BookManagerFunc.getInstance().showHistoryDir();
				adapter.notifyDataSetChanged();
			}
            super.handleMessage(msg);
        }
    };

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState){
		context = this.getActivity();
		
		initBookView();
		initLessonView();
	}
	
	@Event(R.id.mybook)
	private void onMyBookClick(View view){
		setMybookConsole(0);
		bookHead.setBackgroundResource(R.drawable.ic_mybook);
		handler.sendEmptyMessage(0);
	}
	
	@Event(R.id.read_history)
	private void onReadHistoryClick(View view){
		setMybookConsole(1);
		bookHead.setBackgroundResource(R.drawable.ic_readhistory);
		handler.sendEmptyMessage(1);
	}
	
	@Event(R.id.lesson_table)
	private void onLessonTableClick(View view){
		Intent intent = new Intent(HomeFragment.this.getActivity().getApplicationContext(),ScheduleActivity.class);
		startActivity(intent);
	}
	
	@Event(value=R.id.item_listview,type=SwipeListView.OnItemClickListener.class)
	private void onListItemClick(AdapterView<?> parent, View view, int position, long id){
		File file = new File(BookManagerFunc.getInstance().getpa(position));
		Uri uri = Uri.fromFile(file);
		Intent intent = new Intent(HomeFragment.this.getActivity().getApplicationContext(),MuPDFActivity.class);
		intent.setAction(Intent.ACTION_VIEW);
		intent.setData(uri);
		startActivity(intent);
	}
	
	private void initBookView(){
		setMybookConsole(mCurIndicator);
		adapter = new BookAdapter(context,browseList.getRightViewWidth(),
	            new BookAdapter.IOnItemRightClickListener() {
	                @Override
	                public void onRightClick(View v, int position) {
	                        // TODO Auto-generated method stub
	                BookManagerFunc.getInstance().book.bookName = BookManagerFunc.getInstance().getna(position);
	                BookManagerFunc.getInstance().cur = position;
	                BookManagerFunc.getInstance().delete(EventName.BookFunc.DELETEBOOK);
	                handler.sendEmptyMessage(0);
	             }
	    });
		browseList.setAdapter(adapter);
		handler.sendEmptyMessage(0);
	}

	private void initLessonView(){
		adapter_lesson = new LessonGridAdapter(context);
		
		if(adapter_lesson.getCount() != 0){
			lessonList.setAdapter(adapter_lesson);
		}
		else{
			lessonList.setVisibility(View.GONE);
			noLessonItem.setVisibility(View.VISIBLE);
		}
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
}
