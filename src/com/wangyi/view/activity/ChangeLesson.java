package com.wangyi.view.activity;

import org.xutils.view.annotation.*;
import com.wangyi.define.EventName;
import com.wangyi.function.ScheduleFunc;
import com.wangyi.view.BaseActivity;
import com.wangyi.widget.PickWeeksDialog;
import com.wangyi.widget.PickclassesDialog;
import com.wangyi.reader.R;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

@ContentView(R.layout.createlesson)
public class ChangeLesson extends BaseActivity {
	@ViewInject(R.id.lesson_name)
	private EditText lessonName;
	@ViewInject(R.id.location)
	private EditText location;
	@ViewInject(R.id.teacher)
	private EditText teacher;
	@ViewInject(R.id.ClassFromTo)
	private LinearLayout classFromTo;
	@ViewInject(R.id.WeekFromTo)
	private LinearLayout weekFromTo;
	@ViewInject(R.id.class_from_to)
	private TextView _classFromTo;
	@ViewInject(R.id.week_from_to)
	private TextView _weekFromTo;
	private String[] term = new String[25];
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		initData();
	}
	
	private void initData(){
		this.setTitle("修改课程");
		_classFromTo.setText(ScheduleFunc.getInstance().getWeekStr(Integer.parseInt(ScheduleFunc.getInstance().newlesson.weekDay))
				+" " + ScheduleFunc.getInstance().newlesson.fromClass + " - " + ScheduleFunc.getInstance().newlesson.toClass + "节");
		_weekFromTo.setText(ScheduleFunc.getInstance().newlesson.weeknumDelay);
		lessonName.setText(ScheduleFunc.getInstance().newlesson.lessonName);
		location.setText(ScheduleFunc.getInstance().newlesson.classRoom);
		teacher.setText(ScheduleFunc.getInstance().newlesson.teacher);
	}
	
	@Event(R.id.ClassFromTo)
	private void onClassClick(View view){
		final PickclassesDialog dialog = new PickclassesDialog(
				ChangeLesson.this,
				ScheduleFunc.getInstance().newlesson.weekDay,
				ScheduleFunc.getInstance().newlesson.fromClass
				);
		dialog.setButton("确定",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				ScheduleFunc.getInstance().newlesson.weekDay = (dialog.date + 1) + "";
				ScheduleFunc.getInstance().newlesson.fromClass = (dialog.fromClass + 1) + "";
				ScheduleFunc.getInstance().newlesson.toClass = (dialog.toClass + 1) + "";
				_classFromTo.setText(ScheduleFunc.getInstance().getWeekStr(Integer.parseInt(ScheduleFunc.getInstance().newlesson.weekDay))
						+" "+ScheduleFunc.getInstance().newlesson.fromClass+"-"+ScheduleFunc.getInstance().newlesson.toClass+"节");
			}
		});
		
		dialog.setButton2("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	@Event(R.id.WeekFromTo)
	private void onWeekClick(View view){
		equalString(term,ScheduleFunc.getInstance().newlesson.weeks);
		final PickWeeksDialog dialog = new PickWeeksDialog(ChangeLesson.this);
		final View mView = dialog.view;
		final Button dan = (Button) view.findViewById(R.id.dan);
		final Button shuang = (Button) view.findViewById(R.id.shuang);
		final Button all = (Button) view.findViewById(R.id.all);
		
		dan.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View dan) {
				// TODO Auto-generated method stub
				dan.setBackgroundResource(R.drawable.bg_weeks_choose_item_select);
				shuang.setBackgroundResource(R.drawable.bg_weeks_choose_item);
				all.setBackgroundResource(R.drawable.bg_weeks_choose_item);
				for(int i = 0; i < 25;i = i + 2){
					Button week = (Button) mView.findViewById(dialog.weeks[i]);
					week.setBackgroundResource(R.drawable.bg_weeks_choose_item_select);
					week.setTextColor(getResources().getColor(R.color.white));
					term[i] = "true";
				}
				for(int i = 1; i < 25;i = i + 2){
					Button week = (Button) mView.findViewById(dialog.weeks[i]);
					week.setBackgroundResource(R.drawable.bg_weeks_choose_item);
					week.setTextColor(getResources().getColor(R.color.black));
					term[i] = "false";
				}
			}
		
		});
		
		shuang.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View shuang) {
				shuang.setBackgroundResource(R.drawable.bg_weeks_choose_item_select);
				dan.setBackgroundResource(R.drawable.bg_weeks_choose_item);
				all.setBackgroundResource(R.drawable.bg_weeks_choose_item);
				for(int i = 1; i < 25;i = i + 2){
					Button week = (Button) mView.findViewById(dialog.weeks[i]);
					week.setBackgroundResource(R.drawable.bg_weeks_choose_item_select);
					week.setTextColor(getResources().getColor(R.color.white));
					term[i] = "true";
				}
				for(int i = 0; i < 25;i = i + 2){
					Button week = (Button) mView.findViewById(dialog.weeks[i]);
					week.setBackgroundResource(R.drawable.bg_weeks_choose_item);
					week.setTextColor(getResources().getColor(R.color.black));
					term[i] = "false";
				}
			}
		
		});
		
		all.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View all) {
				// TODO Auto-generated method stub
				all.setBackgroundResource(R.drawable.bg_weeks_choose_item_select);
				shuang.setBackgroundResource(R.drawable.bg_weeks_choose_item);
				dan.setBackgroundResource(R.drawable.bg_weeks_choose_item);
				for(int i = 0; i < 25;i++){
					Button week = (Button) mView.findViewById(dialog.weeks[i]);
					week.setBackgroundResource(R.drawable.bg_weeks_choose_item_select);
					week.setTextColor(getResources().getColor(R.color.white));
					term[i] = "true";
				}
			}
		
		});
		
		for(int i = 0;i < 25;i++){
			Button week = (Button) view.findViewById(dialog.weeks[i]);
			if(term[i].equals("true")){
				week.setBackgroundResource(R.drawable.bg_weeks_choose_item_select);
				week.setTextColor(getResources().getColor(R.color.white));
			}
			else{
				week.setBackgroundResource(R.drawable.bg_weeks_choose_item);
				week.setTextColor(getResources().getColor(R.color.black));
			}
			final int num = i;
			week.setOnTouchListener(new View.OnTouchListener() {

				@Override
				public boolean onTouch(View view, MotionEvent event) {
					// TODO Auto-generated method stub
					if(event.getAction() == MotionEvent.ACTION_DOWN){
						shuang.setBackgroundResource(R.drawable.bg_weeks_choose_item);
						dan.setBackgroundResource(R.drawable.bg_weeks_choose_item);
						all.setBackgroundResource(R.drawable.bg_weeks_choose_item);
						
						if(term[num].equals("false")){
							term[num] = "true";
							((Button)view).setBackgroundResource(R.drawable.bg_weeks_choose_item_select);
							((Button)view).setTextColor(getResources().getColor(R.color.white));
						}
						else if(term[num].equals("true")){
							term[num] = "false";
							((Button)view).setBackgroundResource(R.drawable.bg_weeks_choose_item);
							((Button)view).setTextColor(getResources().getColor(R.color.black));
						}
						int danshuang = danshuang();
						if(danshuang == 0){
							dan.setBackgroundResource(R.drawable.bg_weeks_choose_item_select);
							shuang.setBackgroundResource(R.drawable.bg_weeks_choose_item);
							all.setBackgroundResource(R.drawable.bg_weeks_choose_item);
						}
						else if(danshuang == 1){
							shuang.setBackgroundResource(R.drawable.bg_weeks_choose_item_select);
							dan.setBackgroundResource(R.drawable.bg_weeks_choose_item);
							all.setBackgroundResource(R.drawable.bg_weeks_choose_item);
						}
						else{
							if(danshuang == 3){
								shuang.setBackgroundResource(R.drawable.bg_weeks_choose_item);
								dan.setBackgroundResource(R.drawable.bg_weeks_choose_item);
								all.setBackgroundResource(R.drawable.bg_weeks_choose_item_select);
							}
							else{
								shuang.setBackgroundResource(R.drawable.bg_weeks_choose_item);
								dan.setBackgroundResource(R.drawable.bg_weeks_choose_item);
								all.setBackgroundResource(R.drawable.bg_weeks_choose_item);
							}
						}
							
					}
					return false;
				}
			});
		}
		
		dialog.setButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				equalString(ScheduleFunc.getInstance().newlesson.weeks,term);
				int num = 0,j = 0;
				String string = "";
				if(danshuang() != 2||isAll()){
					for(int i = 0;i < 25;i++){
						if(ScheduleFunc.getInstance().newlesson.weeks[i].equals("true")){
							num++;
							j = i;
							if(num == 1){
								string = string + "第" + (i+1) + "周";
							}
						}
					}
					string = string + "至第" + (j+1) + "周";
					if(danshuang() == 0){
						string = string + "（单）";
					}
					else if(danshuang() == 1){
						string = string + "（双）";
					}
				}
				else if(!isAll()){
					for(int i = 0;i < 25;i++){
						if(ScheduleFunc.getInstance().newlesson.weeks[i].equals("true")){
							string = string + (i+1) + " ";
						}
					}
					string = string + "周";
				}
				_weekFromTo.setText(string);
				ScheduleFunc.getInstance().newlesson.weeknumDelay = string;
			}
		});
		
		dialog.setButton2("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	@Event(R.id.confirm)
	private void onConfirmClick(View view){
		if(lessonName.getText().toString().equals("")) return;
		ScheduleFunc.getInstance().newlesson.classRoom = location.getText().toString();
		ScheduleFunc.getInstance().newlesson.teacher = teacher.getText().toString();
		ScheduleFunc.getInstance().newlesson.lessonName = lessonName.getText().toString();
		ScheduleFunc.getInstance().change(EventName.ScheduleFunc.CHANGELESSON);
		ChangeLesson.this.finish();
	}
	
	@Event(R.id.Cancel)
	private void onCancelClick(View view){
		ChangeLesson.this.finish();
	}
	
	private void equalString(String[] str1,String[] str2){
		for(int i = 0;i < str1.length; i++){
			str1[i] = str2[i];
		}
	}
	
	private int danshuang(){
		int[] danshuang = new int[25];
		int num = 0;
		for(int i = 0;i < 25;i++){
			if(term[i].equals("true")){
				danshuang[num] = i+1;
				num++;
			}
		}
		if(num == 25){
			return 3;
		}
		for(int i = 0;i < num;i++){
			if(i == 0){
				continue;
			}
			if(danshuang[i] % 2 == 0&&danshuang[i - 1] % 2 == 0&&(danshuang[i - 1] / 2) + 1 == danshuang[i] / 2){
				if(i == num-1){
					return 1;
				}
				continue;
			}
			else if(danshuang[i] % 2 == 1&&danshuang[i - 1] % 2 == 1&&(danshuang[i - 1] / 2) + 1 == danshuang[i] / 2){
				if(i == num-1){
					return 0;
				}
				continue;
			}
			break;
		}
		return 2;	
	}
	
	private boolean isAll(){
		int[] danshuang = new int[25];
		int num = 0;
		for(int i = 0;i < 25;i++){
			if(term[i].equals("true")){
				danshuang[num] = i+1;
				num++;
			}
		}
		for(int i = 0;i < num;i++){
			if(i == 0){
				continue;
			}
			if(danshuang[i] == danshuang[i - 1] + 1){
				if(i == num-1){
					return true;
				}
				continue;
			}
			break;
		}
		return false;
	}
}
