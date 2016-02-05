package com.wangyi.view.activity;

import java.util.ArrayList;

import com.wangyi.define.Event;
import com.wangyi.define.LessonData;
import com.wangyi.function.ScheduleFunc;
import com.wangyi.imp.database.DBLesson;
import com.wangyi.reader.R;

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
	private Button delete;
	private Button cancel;
	private ImageView edit;
	private TextView title;
	private TextView l_lessonName;
	private TextView l_classroom;
	private TextView l_teacher;
	private TextView l_classnum;
	private TextView l_weeknum;
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
		ScheduleFunc.getInstance().newlesson = ScheduleFunc.getInstance().find(Event.ScheduleFunc.FINDBYCLASS).getDataList().get(0);
		initViewEvent();
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
	}
	
	private void initViewEvent(){
		title.setText(ScheduleFunc.getInstance().newlesson.lessonName);
		l_lessonName.setText(ScheduleFunc.getInstance().newlesson.lessonName);
		l_classroom.setText(ScheduleFunc.getInstance().newlesson.classRoom);
		l_teacher.setText(ScheduleFunc.getInstance().newlesson.teacher);
		l_classnum.setText(ScheduleFunc.getInstance().getWeekStr(Integer.parseInt(ScheduleFunc.getInstance().newlesson.weekDay))
				+ "  " + ScheduleFunc.getInstance().newlesson.fromClass + " - " + ScheduleFunc.getInstance().newlesson.toClass + "½Ú");
		l_weeknum.setText(ScheduleFunc.getInstance().newlesson.weeknumDelay);
		
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
				ScheduleFunc.getInstance().newlesson = ScheduleFunc.getInstance().newlesson;
				ScheduleFunc.getInstance().oldlesson = ScheduleFunc.getInstance().newlesson;
				startActivity(intent);
			}
			
		});
		
		delete.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ScheduleFunc.getInstance().delete(Event.ScheduleFunc.DELELESSON);
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
