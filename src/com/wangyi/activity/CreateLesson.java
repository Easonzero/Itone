package com.wangyi.activity;

import com.wangyi.database.DBLesson;
import com.wangyi.widget.PickWeeksDialog;
import com.wangyi.widget.PickclassesDialog;
import com.zreader.main.R;

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

public class CreateLesson extends Activity {
	EditText lessonName;
	EditText location;
	EditText teacher;
	Button confirm;
	Button cancel;
	LinearLayout classFromTo;
	LinearLayout weekFromTo;
	TextView _classFromTo;
	TextView _weekFromTo;
	String _weekday;
	String[] weeks;
	String[] term = new String[25];
	int classnum;
	int weekday;
	String classFrom;
	String classTo;
	String weekDay;
	String weeknumDelay;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.createlesson);
		
		lessonName = (EditText) findViewById(R.id.lesson_name);
		location = (EditText) findViewById(R.id.location);
		teacher = (EditText) findViewById(R.id.teacher);
		confirm = (Button) findViewById(R.id.Confirm);
		cancel = (Button) findViewById(R.id.Cancel);
		classFromTo = (LinearLayout) findViewById(R.id.ClassFromTo);
		weekFromTo = (LinearLayout) findViewById(R.id.WeekFromTo);
		_classFromTo = (TextView) findViewById(R.id.class_from_to);
		_weekFromTo = (TextView) findViewById(R.id.week_from_to);
		
		_weekday = this.getIntent().getStringExtra("_weekday");
		classnum = this.getIntent().getIntExtra("classnum", 1);
		weekday = this.getIntent().getIntExtra("weekday", 1);
		weekDay = weekday +"";
		classFrom = classnum +"";
		classTo = classnum + "";
		
		this.setTitle("添加课程");
		initEditText();
		initButton();
		
		weeknumDelay = _weekFromTo.getText().toString();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
	}
	
	private void initEditText(){
		_classFromTo.setText(_weekday +" " + classnum + "节");
		weeks = new String[25];
		for(int i = 0;i < 25;i++){
			weeks[i] = "false";
		}
		
		classFromTo.setOnClickListener(new OnClickListener(){

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				final PickclassesDialog dialog = new PickclassesDialog(CreateLesson.this,weekDay,classFrom);
				dialog.setButton("确定",new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						String[] weekdays = new String[] {"周一", "周二", "周三", "周四", "周五","周六","周日"};
						weekDay = (dialog.date + 1) + "";
						classFrom = (dialog.fromClass + 1) + "";
						classTo = (dialog.toClass + 1) + "";
						_classFromTo.setText(weekdays[dialog.date]+" "+(dialog.fromClass+1)+"-"+(dialog.toClass+1)+"节");
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
			
		});
		
		weekFromTo.setOnClickListener(new OnClickListener(){

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				equalString(term,weeks);
				final PickWeeksDialog dialog = new PickWeeksDialog(CreateLesson.this);
				final View view = dialog.view;
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
							Button week = (Button) view.findViewById(dialog.weeks[i]);
							week.setBackgroundResource(R.drawable.bg_weeks_choose_item_select);
							week.setTextColor(getResources().getColor(R.color.white));
							term[i] = "true";
						}
						for(int i = 1; i < 25;i = i + 2){
							Button week = (Button) view.findViewById(dialog.weeks[i]);
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
							Button week = (Button) view.findViewById(dialog.weeks[i]);
							week.setBackgroundResource(R.drawable.bg_weeks_choose_item_select);
							week.setTextColor(getResources().getColor(R.color.white));
							term[i] = "true";
						}
						for(int i = 0; i < 25;i = i + 2){
							Button week = (Button) view.findViewById(dialog.weeks[i]);
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
							Button week = (Button) view.findViewById(dialog.weeks[i]);
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
						equalString(weeks,term);
						int num = 0,j = 0;
						String string = "";
						if(danshuang() != 2||isAll()){
							for(int i = 0;i < 25;i++){
								if(weeks[i].equals("true")){
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
								if(weeks[i].equals("true")){
									string = string + (i+1) + " ";
								}
							}
							string = string + "周";
						}
						_weekFromTo.setText(string);
						weeknumDelay = string;
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
			
		});
	}
	
	private void initButton(){
		confirm.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				DBLesson db = new DBLesson(CreateLesson.this).open();
				db.addLesson(lessonName.getText().toString(), location.getText().toString(), teacher.getText().toString(), weekDay, classFrom,classTo, weeks,weeknumDelay);
				db.close();
				CreateLesson.this.finish();
			}
			
		});
		
		cancel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				CreateLesson.this.finish();
			}
			
		});
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
