package com.wangyi.activity;

import java.util.ArrayList;

import com.wangyi.database.DBLesson;
import com.wangyi.database.LessonDate;
import com.zreader.main.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class WatchLesson extends Activity {

	public static WatchLesson watchLesson;
	private int weeknum;
	private int classnum;
	private int weekday;
	private Button delete;
	private Button cancel;
	private ImageView edit;
	private TextView title;
	private TextView l_lessonName;
	private TextView l_classroom;
	private TextView l_teacher;
	private TextView l_classnum;
	private TextView l_weeknum;
	private ArrayList<LessonDate> lessonDates = new ArrayList<LessonDate>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.watchlesson);
		title = (TextView) findViewById(R.id.lessontitle);
		delete = (Button) findViewById(R.id.delete);
		cancel = (Button) findViewById(R.id.Cancel);
		edit = (ImageView) findViewById(R.id.edit);
		l_lessonName = (TextView) findViewById(R.id.lesson_name);
		l_classroom = (TextView) findViewById(R.id.location);
		l_teacher = (TextView) findViewById(R.id.teacher);
		l_classnum = (TextView) findViewById(R.id.classnum);
		l_weeknum = (TextView) findViewById(R.id.weeknum);
		
		watchLesson = this;
		initDate();
		initViewEvent();
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
	}
	
	private void initDate(){
		weekday = getIntent().getIntExtra("weekday", 1);
		weeknum = getIntent().getIntExtra("weeknum", 1);
		classnum = getIntent().getIntExtra("classnum", 1);
		
		DBLesson db = new DBLesson(this).open();
		lessonDates = db.getAllFromWeeknum("true",weeknum,weekday,classnum);
		db.close();
	}
	
	private void initViewEvent(){
		String[] weekdays = new String[] {"周一", "周二", "周三", "周四", "周五","周六","周日"};
		title.setText(lessonDates.get(0).lessonName);
		l_lessonName.setText(lessonDates.get(0).lessonName);
		l_classroom.setText(lessonDates.get(0).classRoom);
		l_teacher.setText(lessonDates.get(0).teacher);
		l_classnum.setText(weekdays[weekday - 1] + "  " + lessonDates.get(0).fromClass + " - " + lessonDates.get(0).toClass + "节");
		l_weeknum.setText(lessonDates.get(0).weeknumDelay);
		
		cancel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				WatchLesson.this.finish();
			}
			
		});
		
		edit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(WatchLesson.this,ChangeLesson.class);
				intent.putExtra("weekday", weekday);
				intent.putExtra("weeknum", weeknum);
				intent.putExtra("classnum", classnum);
				startActivity(intent);
			}
			
		});
		
		delete.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				DBLesson db = new DBLesson(WatchLesson.this).open();
				db.delete(lessonDates.get(0).lessonName, weekday+"");
				db.close();
				WatchLesson.this.finish();
			}
			
		});
	}
	
	protected void setTranslucentStatus(boolean on) {
	    Window win = getWindow();
	    WindowManager.LayoutParams winParams = win.getAttributes();
	    final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
	    if (on) {
	        winParams.flags |= bits;
	    } else {
	        winParams.flags &= ~bits;
	    }
	    win.setAttributes(winParams);
	}
}
