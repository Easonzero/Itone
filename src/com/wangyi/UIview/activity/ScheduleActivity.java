package com.wangyi.UIview.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.xutils.view.annotation.*;

import com.wangyi.UIview.BaseActivity;
import com.wangyi.UIview.widget.SildingFinishLayout;
import com.wangyi.UIview.widget.SildingFinishLayout.OnSildingFinishListener;
import com.wangyi.define.EventName;
import com.wangyi.define.LessonData;
import com.wangyi.function.ScheduleFunc;
import com.wangyi.reader.R;

import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

@ContentView(R.layout.activity_schedule)
public class ScheduleActivity extends BaseActivity {
	private int[][] lessonLocation;
	private int[] weekDays;
	private int id = -1;
	
	@ViewInject(R.id.lessons)
	private ScrollView scroll;
	@ViewInject(R.id.weekoftoday)
	private TextView weekOfToday;
	
	private ListView lvPopupList;
	private TextView lesson;
	private LinearLayout today;
	private PopupWindow pwMyPopWindow;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		
		today = (LinearLayout) findViewById(weekDays[ScheduleFunc.getInstance()._today-1]);
		today.setBackgroundResource(R.drawable.class_lesson_tody);
		
		initWeekOfToday();
		initLesson(ScheduleFunc.getInstance()._weekOfToday);
		initSetLessonListener();
		initSlidFinish();
	}
	
	@Event(R.id.weekoftoday)
	private void onWeekClick(View view){
		if (pwMyPopWindow.isShowing()) {
            pwMyPopWindow.dismiss();
        } else {
            pwMyPopWindow.showAsDropDown(view,-100,0);  
        }
	}
	
	@Event(value=R.id.lv_popup_list,type=ListView.OnItemClickListener.class)
	private void onListItemClick(AdapterView<?> parent, View view,  
            int position, long id){
		ScheduleFunc.getInstance().currentWeek = position+1;
    	reset(ScheduleFunc.getInstance().currentWeek);
        pwMyPopWindow.dismiss();
	}
	
	@Event(value=R.id.lessons,type=View.OnTouchListener.class)
	private boolean onLessonsTouch(View arg0, MotionEvent event){
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
	
	private void initWeekOfToday(){
		weekOfToday.setText(ScheduleFunc.getInstance().getWeekNumStr(ScheduleFunc.getInstance()._weekOfToday));
		initPopupWindow();
	}
	
	private void initPopupWindow() {  
		ArrayList<Map<String, String>> setList = new ArrayList<Map<String, String>>();  
		Map<String, String> map; 
        for(int i = 0;i < 25;i++){
        	map = new HashMap<String, String>();  
        	map.put("share_key", "µÚ" + (i+1) + "ÖÜ"); 
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
        lvPopupList.setSelection(ScheduleFunc.getInstance()._weekOfToday-2);
        pwMyPopWindow.setBackgroundDrawable(ScheduleActivity.this.getResources().getDrawable(R.drawable.popupwindow));
  
        lvPopupList.measure(View.MeasureSpec.UNSPECIFIED,  
                View.MeasureSpec.UNSPECIFIED);  
        pwMyPopWindow.setWidth(lvPopupList.getMeasuredWidth());  
        pwMyPopWindow.setHeight((lvPopupList.getMeasuredHeight() + 20)  
                * 3);  
        
        pwMyPopWindow.setOutsideTouchable(true);
	}  

	private void initLesson(int week){
		ArrayList<LessonData> lessonDatas = ScheduleFunc.getInstance().find(EventName.ScheduleFunc.FINDBYWEEK).getDataList();
		if(lessonDatas != null){
			for(int i = 0;i < lessonDatas.size();i++){
				LessonData ld= lessonDatas.get(i);
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
		
		for(int i = 1;i <= 12;i++){
			for(int j = 1;j <= 7;j++){
				lesson = (TextView) findViewById(lessonLocation[i][j]);
				final boolean hasLesson = !lesson.getText().toString().equals("");
				lesson.setTag(hasLesson?"lesson":"none");
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
							ScheduleFunc.getInstance().newlesson.weekDay = weekday+"";
							ScheduleFunc.getInstance().newlesson.fromClass = classnum+"";
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
									ScheduleFunc.getInstance().newlesson.weekDay = weekday+"";
									ScheduleFunc.getInstance().newlesson.fromClass = classnum+"";
									startActivityForResult(intent,0);
									view.setBackgroundResource(R.drawable.bg_lesson);
								}
							}
					}
					
				});
			}
		}
	}
	
	private void reset(int weeknum){
		weekOfToday.setText(ScheduleFunc.getInstance().getWeekNumStr(weeknum));
		for(int i = 1;i <= 12;i++){
			for(int j = 1;j <= 7;j++){
				lesson = (TextView) findViewById(lessonLocation[i][j]);
				lesson.setVisibility(View.VISIBLE);
				lesson.setBackgroundResource(R.drawable.bg_lesson);
				lesson.getLayoutParams().height = dip2px(ScheduleActivity.this,80);
				lesson.setText("");
			}
		}
		initLesson(weeknum);
		initSetLessonListener();
		lvPopupList.setSelection(weeknum-2);
	}
	
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(0, R.anim.base_slide_right_out);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch(requestCode){
		case 0:
			ScheduleFunc.getInstance().currentWeek = ScheduleFunc.getInstance()._weekOfToday;
			reset(ScheduleFunc.getInstance().currentWeek);
			initSlidFinish();
			break;
		case 1:
			reset(ScheduleFunc.getInstance().currentWeek);
			initSlidFinish();
			break;
		}
	}
	
	private void setLessonBackground(View lesson){
		Random random = new Random();
		int[] color = new int[]{R.drawable.bg_lesson_green_selector,R.drawable.bg_lesson_purple_selector,
								R.drawable.bg_lesson_blue_selector,R.drawable.bg_lesson_grey_selector};
		lesson.setBackgroundResource(color[random.nextInt(4)]);
	}
	
	public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    } 
}
