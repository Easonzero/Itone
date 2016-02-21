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
	private static final String[] weekdays = new String[]{"��һ","�ܶ�","����","����","����","����","����"};
	private static final String[] weeksNum = new String[]{"��1��","��2��","��3��","��4��","��5��","��6��","��7��","��8��","��9��","��10��","��11��","��12��",
			"��13��","��14��","��15��","��16��","��17��","��18��","��19��","��20��","��21��","��22��","��23��","��24��","��25��",};
	private DBInterface<LessonData> lessonDB;
	public int initWeek;
	public int initDate;//��ʾһ���е�initdata��ΪУ����initweek��
	public int _today;//��ʾ�������ڼ�
	public int _weekOfToday;//��ʾ���������
	public int currentWeek;//��ǰ��������
	public LessonData oldlesson;
	public LessonData newlesson;
	private Context context;
	
	private void ScheduleFunc(){}
	
	public void init(Context context){
		oldlesson = new LessonData();
		newlesson = new LessonData();
		this.context = context;
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);//prefs���ع�
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
