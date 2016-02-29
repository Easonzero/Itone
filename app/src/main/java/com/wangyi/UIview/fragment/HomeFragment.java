package com.wangyi.UIview.fragment;

import java.io.File;
import org.xutils.view.annotation.*;
import com.artifex.mupdfdemo.MuPDFActivity;
import com.wangyi.UIview.BaseFragment;
import com.wangyi.UIview.activity.ScheduleActivity;
import com.wangyi.UIview.adapter.BookAdapter;
import com.wangyi.UIview.adapter.LessonGridAdapter;
import com.wangyi.UIview.widget.SwipeListView;
import com.wangyi.define.EventName;
import com.wangyi.function.BookManagerFunc;
import com.wangyi.reader.R;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
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
		 @Override 
	        public void handleMessage(Message msg) { 
	            super.handleMessage(msg); 
	            switch(msg.what){
	            case EventName.UI.FINISH:
	        		adapter.notifyDataSetChanged();
	        		break;
	            }
		 }
	};

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        initBookView(this.getActivity().getBaseContext());
        initLessonView(this.getActivity().getBaseContext());
    }
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		if(!hidden){
			BookManagerFunc.getInstance().connect(handler).showFileDir();
		}else{
			BookManagerFunc.getInstance().connect(handler).clear();
		}
	}
	
	@Event(R.id.mybook)
	private void onMyBookClick(View view){
		setMybookConsole(0);
		bookHead.setBackgroundResource(R.drawable.ic_mybook);
		BookManagerFunc.getInstance().connect(handler).showFileDir();
	}
	
	@Event(R.id.read_history)
	private void onReadHistoryClick(View view){
		setMybookConsole(1);
		bookHead.setBackgroundResource(R.drawable.ic_readhistory);
		BookManagerFunc.getInstance().connect(handler).showHistoryDir();
	}
	
	@Event(R.id.lesson_table)
	private void onLessonTableClick(View view){
		Intent intent = new Intent(HomeFragment.this.getActivity().getApplicationContext(),ScheduleActivity.class);
		startActivity(intent);
	}
	
	@Event(value=R.id.item_listview,type=SwipeListView.OnItemClickListener.class)
	private void onListItemClick(AdapterView<?> parent, View view, int position, long id){
		File file = new File(BookManagerFunc.getInstance().getBookData(position).url);
		Uri uri = Uri.parse(file.getAbsolutePath());
		Intent intent = new Intent(HomeFragment.this.getActivity().getApplicationContext(),MuPDFActivity.class);
		intent.setAction(Intent.ACTION_VIEW);
		intent.setData(uri);
		startActivity(intent);
	}
	
	private void initBookView(Context context){
		setMybookConsole(mCurIndicator);
		adapter = new BookAdapter(context,browseList.getRightViewWidth(),
	            new BookAdapter.IOnItemRightClickListener() {
	                @Override
	                public void onRightClick(View v, int position) {
	                BookManagerFunc.getInstance().delete(position);
	             }
	    });
		browseList.setAdapter(adapter);
	}

	private void initLessonView(Context context){
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
