package com.wangyi.UIview.adapter;

import java.util.ArrayList;
import java.util.List;

import com.wangyi.define.LessonData;
import com.wangyi.function.ScheduleFunc;
import com.wangyi.reader.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LessonGridAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<LessonData> lessonDatas = null;

	public LessonGridAdapter(Context context){
		//lessonDatas = ScheduleFunc.getInstance().find();
		if(lessonDatas == null) lessonDatas = new ArrayList();
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
		holder.lesson_name.setText(lessonDatas.get(position).getLessonName());
		holder.location.setText(lessonDatas.get(position).getClassRoom());
		holder.class_from_to.setText(lessonDatas.get(position).getFromClass() + " - "
				+ lessonDatas.get(position).getToClass() + "节");

		if(Integer.parseInt(lessonDatas.get(position).getFromClass()) < 5){
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
}
