package com.wangyi.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

import com.wangyi.database.DBLesson;
import com.wangyi.database.LessonDate;
import com.wangyi.widget.SildingFinishLayout;
import com.wangyi.widget.SystemBarTintManager;
import com.wangyi.widget.SildingFinishLayout.OnSildingFinishListener;
import com.zreader.main.R;

import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class ScheduleActivity extends Activity {
	private int[][] lessonLocation;
	private int[] weekDays;
	private String[] weeksNum;
	private ScrollView scroll;
	private TextView lesson;
	private LinearLayout today;
	private int _today;
	private int _weekOfToday;
	private TextView weekOfToday;
	private int initWeek;
	private int initDate;
	private int id = -1;
	private PopupWindow pwMyPopWindow;
    private ListView lvPopupList;
    private int delayweek;
    List<Map<String, String>> setList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule);
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		lessonLocation = new int[][]{{R.id.date,R.id.date_mon,R.id.date_tue,R.id.date_wed,R.id.date_thu,R.id.date_fri,R.id.date_sta,R.id.date_sun},
				{R.id.lesson_1,R.id.mon_lesson_1,R.id.tue_lesson_1,R.id.wed_lesson_1,R.id.thu_lesson_1,R.id.fri_lesson_1,R.id.sta_lesson_1,R.id.sun_lesson_1},
				{R.id.lesson_2,R.id.mon_lesson_2,R.id.tue_lesson_2,R.id.wed_lesson_2,R.id.thu_lesson_2,R.id.fri_lesson_2,R.id.sta_lesson_2,R.id.sun_lesson_2},		
				{R.id.lesson_3,R.id.mon_lesson_3,R.id.tue_lesson_3,R.id.wed_lesson_3,R.id.thu_lesson_3,R.id.fri_lesson_3,R.id.sta_lesson_3,R.id.sun_lesson_3},
				{R.id.lesson_4,R.id.mon_lesson_4,R.id.tue_lesson_4,R.id.wed_lesson_4,R.id.thu_lesson_4,R.id.fri_lesson_4,R.id.sta_lesson_4,R.id.sun_lesson_4},
				{R.id.lesson_5,R.id.mon_lesson_5,R.id.tue_lesson_5,R.id.wed_lesson_5,R.id.thu_lesson_5,R.id.fri_lesson_5,R.id.sta_lesson_5,R.id.sun_lesson_5},
				{R.id.lesson_6,R.id.mon_lesson_6,R.id.tue_lesson_6,R.id.wed_lesson_6,R.id.thu_lesson_6,R.id.fri_lesson_6,R.id.sta_lesson_6,R.id.sun_lesson_6},
				{R.id.lesson_7,R.id.mon_lesson_7,R.id.tue_lesson_7,R.id.wed_lesson_7,R.id.thu_lesson_7,R.id.fri_lesson_7,R.id.sta_lesson_7,R.id.sun_lesson_7},
				{R.id.lesson_8,R.id.mon_lesson_8,R.id.tue_lesson_8,R.id.wed_lesson_8,R.id.thu_lesson_8,R.id.fri_lesson_8,R.id.sta_lesson_8,R.id.sun_lesson_8},
				{R.id.lesson_9,R.id.mon_lesson_9,R.id.tue_lesson_9,R.id.wed_lesson_9,R.id.thu_lesson_9,R.id.fri_lesson_9,R.id.sta_lesson_9,R.id.sun_lesson_9},
				{R.id.lesson_10,R.id.mon_lesson_10,R.id.tue_lesson_10,R.id.wed_lesson_10,R.id.thu_lesson_10,R.id.fri_lesson_10,R.id.sta_lesson_10,R.id.sun_lesson_10},
				{R.id.lesson_11,R.id.mon_lesson_11,R.id.tue_lesson_11,R.id.wed_lesson_11,R.id.thu_lesson_11,R.id.fri_lesson_11,R.id.sta_lesson_11,R.id.sun_lesson_11},
				{R.id.lesson_12,R.id.mon_lesson_12,R.id.tue_lesson_12,R.id.wed_lesson_12,R.id.thu_lesson_12,R.id.fri_lesson_12,R.id.sta_lesson_12,R.id.sun_lesson_12}};
		weekDays = new int[]{R.id.mon,R.id.tue,R.id.wed,R.id.thu,R.id.fri,R.id.sta,R.id.sun};
		weeksNum = new String[]{"第1周","第2周","第3周","第4周","第5周","第6周","第7周","第8周","第9周","第10周","第11周","第12周",
				"第13周","第14周","第15周","第16周","第17周","第18周","第19周","第20周","第21周","第22周","第23周","第24周","第25周",};
		_today = getWeekOfDate();
		initWeek = prefs.getInt("initWeek", 1);
		initDate = prefs.getInt("initDate", 36);
		
		scroll = (ScrollView) findViewById(R.id.lessons);
		today = (LinearLayout) findViewById(weekDays[_today-1]);
		weekOfToday = (TextView) findViewById(R.id.weekoftoday);
		
		today.setBackgroundResource(R.drawable.class_lesson_tody);
		initWeekOfToday();
		initLesson(_weekOfToday);
		initSetLessonListener();
		initSlidFinish();
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
	}
	
	private void initSlidFinish(){
		SildingFinishLayout mSildingFinishLayout = (SildingFinishLayout) findViewById(R.id.schedule);
		mSildingFinishLayout.setOnSildingFinishListener(new OnSildingFinishListener() {

			@Override
			public void onSildingFinish() {
				ScheduleActivity.this.finish();
			}
		});
		
		mSildingFinishLayout.setTouchView(scroll);
	}
	
	public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
	
	private void initWeekOfToday(){
		_weekOfToday = initWeek + (getWeekNumber() - initDate);
		delayweek = _weekOfToday;
		weekOfToday.setText(weeksNum[_weekOfToday - 1]);
		initPopupWindow();
		weekOfToday.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				if (pwMyPopWindow.isShowing()) {  
					  
                    pwMyPopWindow.dismiss();
                } else {  
  
                    pwMyPopWindow.showAsDropDown(view,-100,0);  
                }  
			}
			
		});
	}
	
	 private void initPopupWindow() {  
		 	setList = new ArrayList<Map<String, String>>();  
	        Map<String, String> map; 
	        for(int i = 0;i < 25;i++){
	        	map = new HashMap<String, String>();  
	        	map.put("share_key", "第" + (i+1) + "周"); 
	        	setList.add(map);  
	        }
	        LayoutInflater inflater = (LayoutInflater) this  
	                .getSystemService(LAYOUT_INFLATER_SERVICE);  
	        View layout = inflater.inflate(R.layout.task_detail_popupwindow, null);  
	        lvPopupList = (ListView) layout.findViewById(R.id.lv_popup_list);  
	        pwMyPopWindow = new PopupWindow(layout);  
	        pwMyPopWindow.setFocusable(true);
	        lvPopupList.setAdapter(new SimpleAdapter(ScheduleActivity.this, setList,  
	                R.layout.list_item_popupwindow, new String[] { "share_key" },  
	                new int[] { R.id.tv_list_item }));
	        lvPopupList.setSelection(_weekOfToday - 2);
	        lvPopupList.setOnItemClickListener(new OnItemClickListener() {  
	  
	            @Override  
	            public void onItemClick(AdapterView<?> parent, View view,  
	                    int position, long id) 
	            {  
	            	weekOfToday.setText(weeksNum[position]);
	            	initUI();
	                initLesson(position + 1);
	                delayweek = position + 1;
	                initSetLessonListener();
	                lvPopupList.setSelection(position - 1);
	                pwMyPopWindow.dismiss();
	            }  
	        });  
	        pwMyPopWindow.setBackgroundDrawable(ScheduleActivity.this.getResources().getDrawable(R.drawable.popupwindow));
	  
	        lvPopupList.measure(View.MeasureSpec.UNSPECIFIED,  
	                View.MeasureSpec.UNSPECIFIED);  
	        pwMyPopWindow.setWidth(lvPopupList.getMeasuredWidth());  
	        pwMyPopWindow.setHeight((lvPopupList.getMeasuredHeight() + 20)  
	                * 3);  
	        
	        pwMyPopWindow.setOutsideTouchable(true);
	    }  
	
	public int getWeekOfDate() {
		Calendar calendar = Calendar.getInstance();
		boolean isFirstSunday = (calendar.getFirstDayOfWeek() == Calendar.SUNDAY);
		int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
		if(isFirstSunday){
		  weekDay = weekDay - 1;
		  if(weekDay == 0){
		    weekDay = 7;
		  }
		}

        return weekDay;
    }
	
	public static int getWeekNumber(){
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int c = calendar.get(Calendar.WEEK_OF_YEAR);
		int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
		if(weekDay == 0){
			c = c - 1;
		}
		return c;
	}
	
	private void initLesson(int week){
		DBLesson db = new DBLesson(this).open();
		ArrayList<LessonDate> lessonDates = new ArrayList<LessonDate>();
		lessonDates = db.getAllFromWeeknum("true",week);
		db.close();
		if(lessonDates != null){
			for(int i = 0;i < lessonDates.size();i++){
				LessonDate ld= lessonDates.get(i);
				TextView lesson = (TextView) findViewById(lessonLocation[Integer.parseInt(ld.fromClass)][Integer.parseInt(ld.weekDay)]);
				lesson.setText(ld.lessonName+"@"+ld.classRoom);
				setLessonBackground(lesson);
				int dt = Integer.parseInt(ld.toClass) - Integer.parseInt(ld.fromClass);
				for(int j = 1;j <= dt;j++){
					TextView lessongone = (TextView) findViewById(lessonLocation[Integer.parseInt(ld.fromClass)+j][Integer.parseInt(ld.weekDay)]);
					lessongone.setVisibility(View.GONE);
				}
				lesson.setPadding(0, 0, 0, 0);
				lesson.getLayoutParams().height = dip2px(ScheduleActivity.this,80*(dt+1));
				lesson.setTag("lesson");
			}
		}
	}
	
	private void initSetLessonListener(){
		scroll.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				// TODO Auto-generated method stub
				if(MotionEvent.ACTION_MOVE == event.getAction()){
					if(id != (-1)){
						lesson = (TextView) findViewById(id);
						lesson.setBackgroundResource(R.drawable.bg_lesson);
						lesson.setTag("none");
						id = -1;
					}
				}
				return false;
			}
			
		});
		
		for(int i = 1;i <= 12;i++){
			for(int j = 1;j <= 7;j++){
				lesson = (TextView) findViewById(lessonLocation[i][j]);
				String[] weekdays = new String[]{"周一","周二","周三","周四","周五","周六","周日"};
				final boolean hasLesson = !lesson.getText().toString().equals("");
				lesson.setTag(hasLesson?"lesson":"none");
				final String _weekday = weekdays[j-1];
				final int weekday = j;
				final int classnum = i;
				lesson.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View view) {
						// TODO Auto-generated method stub
						if(view.getTag().toString().equals("lesson")){
							if(id != (-1)){
								lesson = (TextView) findViewById(id);
								lesson.setBackgroundResource(R.drawable.bg_lesson);
								lesson.setTag("none");
								id = -1;
							}
							Intent intent = new Intent(ScheduleActivity.this,WatchLesson.class);
							intent.putExtra("weekday", weekday);
							intent.putExtra("weeknum", delayweek);
							intent.putExtra("classnum",classnum);
							startActivityForResult(intent,1);
						}
						else{	
								if(view.getTag().toString().equals("none")){
									view.setBackgroundResource(R.drawable.bg_lesson_add);
									if(id != (-1)){
										lesson = (TextView) findViewById(id);
										lesson.setBackgroundResource(R.drawable.bg_lesson);
										lesson.setTag("none");
									}
									id = view.getId();
									view.setTag("add");
								}
								else if(view.getTag().toString().equals("add")){
									view.setTag("none");
									id = -1;
									Intent intent = new Intent(ScheduleActivity.this,CreateLesson.class);
									intent.putExtra("_weekday", _weekday);
									intent.putExtra("classnum",classnum);
									intent.putExtra("weekday",weekday);
									startActivityForResult(intent,0);
									view.setBackgroundResource(R.drawable.bg_lesson);
								}
							}
					}
					
				});
			}
		}
	}
	
	private void initUI(){
		for(int i = 1;i <= 12;i++){
			for(int j = 1;j <= 7;j++){
				lesson = (TextView) findViewById(lessonLocation[i][j]);
				lesson.setVisibility(View.VISIBLE);
				lesson.setBackgroundResource(R.drawable.bg_lesson);
				lesson.getLayoutParams().height = dip2px(ScheduleActivity.this,80);
				lesson.setText("");
			}
		}
	}
	
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(0, R.anim.base_slide_right_out);
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch(requestCode){
		case 0:
			weekOfToday.setText(weeksNum[_weekOfToday - 1]);
			initUI();
			initLesson(_weekOfToday);
			delayweek = _weekOfToday;
			initSetLessonListener();
			initSlidFinish();
			lvPopupList.setSelection(_weekOfToday - 2);
			break;
		case 1:
			weekOfToday.setText(weeksNum[delayweek - 1]);
			initUI();
			initLesson(delayweek);
			initSetLessonListener();
			initSlidFinish();
			lvPopupList.setSelection(delayweek - 2);
			break;
		}
	}
	
	private void setLessonBackground(View lesson){
		Random random = new Random();
		int[] color = new int[]{R.drawable.bg_lesson_green_selector,R.drawable.bg_lesson_purple_selector,
								R.drawable.bg_lesson_blue_selector,R.drawable.bg_lesson_grey_selector};
		lesson.setBackgroundResource(color[random.nextInt(4)]);
	}
	
}
