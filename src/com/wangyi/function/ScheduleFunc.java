package com.wangyi.function;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;

import com.wangyi.Interface.DBInterface;
import com.wangyi.define.EventName;
import com.wangyi.define.LessonData;
import com.wangyi.define.Response;
import com.wangyi.imp.database.DBLesson;

public class ScheduleFunc{
	private static final ScheduleFunc INSTANCE = new ScheduleFunc();
	private static final String[] weekdays = new String[]{"周一","周二","周三","周四","周五","周六","周日"};
	private static final String[] weeksNum = new String[]{"第1周","第2周","第3周","第4周","第5周","第6周","第7周","第8周","第9周","第10周","第11周","第12周",
			"第13周","第14周","第15周","第16周","第17周","第18周","第19周","第20周","第21周","第22周","第23周","第24周","第25周",};
	private DBInterface<LessonData> lessonDB;
	public int initWeek;
	public int initDate;//表示一年中第initdata周为校历第initweek周
	public int _today;//表示今天星期几
	public int _weekOfToday;//表示今天的周数
	public int currentWeek;//当前访问周数
	public LessonData oldlesson;
	public LessonData newlesson;
	private Context context;
	
	private void ScheduleFunc(){}
	
	public void init(Context context){
		oldlesson = new LessonData();
		newlesson = new LessonData();
		this.context = context;
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);//prefs待重构
		initWeek = prefs.getInt("initWeek", 1);
		initDate = prefs.getInt("initDate", 5);
		_weekOfToday = initWeek + (getWeekNumber() - initDate);
		_today = getWeekOfDate();
		currentWeek = _weekOfToday;
	}
	
	public String getWeekStr(int weekday){
		return weekdays[weekday-1];
	}
	
	public String getWeekNumStr(int weeknum){
		return weeksNum[weeknum-1];
	}
	
	public static ScheduleFunc getInstance(){
		return INSTANCE;
	}
	
	public Response<LessonData> find(String event){
		Response<LessonData> response = new Response();
		ArrayList<LessonData> dataList = new ArrayList();
		dataList.add(newlesson);
		response.init(event, null, currentWeek, dataList);
		lessonDB = new DBLesson(context).open();
		response = lessonDB.find(response);
		lessonDB.close();
		return response;
	}
	
	public void add(String event){
		Response<LessonData> response = new Response();
		ArrayList<LessonData> dataList = new ArrayList();
		dataList.add(newlesson);
		response.init(event, null, null, dataList);
		lessonDB = new DBLesson(context).open();
		lessonDB.add(response);
		lessonDB.close();
	}
	
	public void delete(String event){
		Response<LessonData> response = new Response();
		ArrayList<LessonData> dataList = new ArrayList();
		dataList.add(newlesson);
		response.init(event, null, null, dataList);
		lessonDB = new DBLesson(context).open();
		lessonDB.delete(response);
		lessonDB.close();
	}
	
	public void change(String event){
		Response<LessonData> response = new Response();
		ArrayList<LessonData> dataList = new ArrayList();
		dataList.add(newlesson);
		response.init(event, null, oldlesson, dataList);
		lessonDB = new DBLesson(context).open();
		lessonDB.change(response);
		lessonDB.close();
	}
	
	private int getWeekOfDate() {
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
	
	private int getWeekNumber(){
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
}
