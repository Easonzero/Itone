package com.wangyi.UIview.adapter;

import java.util.ArrayList;
import java.util.List;

import com.wangyi.UIview.activity.WatchLesson;
import com.wangyi.UIview.adapter.viewholder.LessonGridVH;
import com.wangyi.define.bean.LessonData;
import com.wangyi.function.ScheduleFunc;
import com.wangyi.reader.R;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class LessonGridAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<LessonData> lessonDatas = null;

	public LessonGridAdapter(Context context){
		lessonDatas = ScheduleFunc.getInstance().find();
		if(lessonDatas == null) lessonDatas = new ArrayList();
		inflater = LayoutInflater.from(context);
	}

	public void update(){
		lessonDatas = ScheduleFunc.getInstance().find();
		if(lessonDatas == null) lessonDatas = new ArrayList();
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
		LessonGridVH holder = null;
		if (null == convertView){
			convertView = inflater.inflate(R.layout.lesson_gird_item,parent,false);
			holder = new LessonGridVH(convertView);
			convertView.setTag(holder);
		}
		else{
			holder = (LessonGridVH)convertView.getTag();
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

}
