package com.wangyi.widget;

import com.wangyi.reader.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LessonListLayout extends RelativeLayout {
	
	Context context;
	int WIDTH;
	OnClickListener listener;
	public Object obj = null;
	
	public LessonListLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		WIDTH = wm.getDefaultDisplay().getWidth();
	}
	
	public LessonListLayout(Context context, AttributeSet attrs) {  
	    super(context, attrs);  
	    // TODO Auto-generated constructor stub  
	    this.context = context;
	}  
	  
	      
	public LessonListLayout(Context context, AttributeSet attrs, int defStyle) {  
	    super(context, attrs, defStyle);  
	    // TODO Auto-generated constructor stub  
	    this.context = context;
	}  
	
	public void addLessons(String[] lessonNames,OnClickListener listener){
		this.listener = listener;
		TextView head = addLesson("È«²¿",1);
		head.setBackgroundResource(R.drawable.bg_class);
		head.setPadding(0, 0, 0, 0);
		obj = head;
		for(int i = 0;i < lessonNames.length;i++){
			addLesson(lessonNames[i],i+2);
		}
	}

	public TextView addLesson(String lessonName,int num){
		String NUM = "d" + "000" + num;
		TextView lesson = new TextView(context);
		lesson.setIncludeFontPadding(false);
		lesson.setText(lessonName);
		lesson.setTextSize(15);
		lesson.setId(num);
		lesson.setTag(NUM);
		LayoutParams lp = getLessonLayoutParams(num);
		lesson.setLayoutParams(lp);
		addView(lesson);
		lesson.setOnClickListener(listener);
		return lesson;
	}
	
	public LayoutParams getLessonLayoutParams(int num){
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		if(num!=1&&num%4==1){
			lp.addRule(RelativeLayout.BELOW, num-4);
			lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			lp.topMargin = 20;
			lp.leftMargin = 20;
		}
		else if(num != 1){
			lp.addRule(RelativeLayout.ALIGN_LEFT,num-1);
			lp.addRule(RelativeLayout.BELOW, num-4);
			lp.leftMargin = 255;
			lp.topMargin = 20;
		}
		else{
			lp.topMargin = 20;
			lp.leftMargin = 20;
		}
		return lp;
	}
}
