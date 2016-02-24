package com.wangyi.UIview.adapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.wangyi.define.LessonData;
import com.wangyi.reader.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LessonGridAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private int _weekOfToday;
	private int initWeek;
	private int initDate;
	private int _today;
	private ArrayList<LessonData> lessonDatas;

	public LessonGridAdapter(Context context){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		initWeek = prefs.getInt("initWeek", 1);
		initDate = prefs.getInt("initDate", 10);
		_today = getWeekOfDate();
		_weekOfToday = initWeek + (getWeekNumber() - initDate);
		lessonDatas = new ArrayList<LessonData>();
		DBLesson db = new DBLesson(context).open();
		//lessonDatas = db.getAllFromWeeknum("true",_weekOfToday,_today);
		db.close();

		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lessonDatas.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return lessonDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (null == convertView){
			convertView = inflater.inflate(R.layout.lesson_gird_item,parent,false);
			holder = new ViewHolder();
			holder.lesson_name = (TextView) convertView.findViewById(R.id.lesson_name);
			holder.location = (TextView) convertView.findViewById(R.id.location);
			holder.class_from_to = (TextView) convertView.findViewById(R.id.class_from_to);
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder)convertView.getTag();
		}
		holder.lesson_name.setText(lessonDatas.get(position).lessonName);
		holder.location.setText(lessonDatas.get(position).classRoom);
		holder.class_from_to.setText(lessonDatas.get(position).fromClass + " - "
				+ lessonDatas.get(position).toClass + "节");

		if(Integer.parseInt(lessonDatas.get(position).fromClass) < 5){
			holder.class_from_to.setBackgroundResource(R.drawable.ic_home_blue);
		}
		else{
			holder.class_from_to.setBackgroundResource(R.drawable.ic_home_green);
		}
		return convertView;
	}

	private class ViewHolder{
		private TextView lesson_name;
		private TextView location;
		private TextView class_from_to;
	}

	public static int getWeekNumber(){
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int c = calendar.get(Calendar.WEEK_OF_YEAR);
		int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
		if(weekDay == 0){
			c -= 1;
		}
		return c;
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
}
