package com.wangyi.adapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.wangyi.activity.ScheduleActivity;
import com.wangyi.database.DBLesson;
import com.wangyi.database.LessonDate;
import com.wangyi.widget.ScrollingTextView;
import com.zreader.main.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LessonGridAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private int _weekOfToday;
	private int initWeek;
	private int initDate;
	private int _today;
	private ArrayList<LessonDate> lessonDates;
	
	public LessonGridAdapter(Context context){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		initWeek = prefs.getInt("initWeek", 1);
		initDate = prefs.getInt("initDate", 10);
		_today = getWeekOfDate();
		_weekOfToday = initWeek + (getWeekNumber() - initDate);
		lessonDates = new ArrayList<LessonDate>();
		DBLesson db = new DBLesson(context).open();
		//lessonDates = db.getAllFromWeeknum("true",_weekOfToday,_today);
		db.close();
		
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lessonDates.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return lessonDates.get(position);
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
		holder.lesson_name.setText(lessonDates.get(position).lessonName);
		holder.location.setText(lessonDates.get(position).classRoom);
		holder.class_from_to.setText(lessonDates.get(position).fromClass + " - " 
		+ lessonDates.get(position).toClass + "½Ú");
		
		if(Integer.parseInt(lessonDates.get(position).fromClass) < 5){
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
