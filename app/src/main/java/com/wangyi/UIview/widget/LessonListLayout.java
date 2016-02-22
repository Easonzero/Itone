package com.wangyi.UIview.widget;

import com.wangyi.reader.R;
import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LessonListLayout extends RelativeLayout {

	private Context context;
	private int PADDING;
	private int SCREEN_WIDTH;
	private int LESSON_WIDTH;
	private int columnNum;
	private OnClickListener listener;
	public Object obj = null;

	public LessonListLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public LessonListLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public LessonListLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init(context);
	}

	private void init(Context context){
		this.context = context;
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		SCREEN_WIDTH = getScreenWidth(windowManager);
		PADDING = SCREEN_WIDTH/40;
		columnNum = 4;
		LESSON_WIDTH = (SCREEN_WIDTH-PADDING*2)/columnNum;
		this.setPadding(0, 0, 0, PADDING);
	}

	private int getScreenWidth(WindowManager manager) {
		Display dis = manager.getDefaultDisplay();
		Point screenSize = new Point();
		dis.getSize(screenSize);
		return screenSize.x;
	}

	public void addLessons(String[] lessonNames,OnClickListener listener){
		this.listener = listener;
		TextView head = addLesson("全部",1);
		head.setBackgroundResource(R.drawable.bg_class);
		head.setPadding(0, 0, 0, 0);
		obj = head;
		for(int i = 0;i < lessonNames.length;i++){
			addLesson(lessonNames[i],i+2);
		}
	}

	private TextView addLesson(String lessonName,int num){
		TextView lesson = new TextView(context);
		lesson.setIncludeFontPadding(false);
		lesson.setText(lessonName);
		lesson.setTextSize(15);
		lesson.setId(num);
		LayoutParams lp = getLessonLayoutParams(num);
		lesson.setLayoutParams(lp);
		addView(lesson);
		lesson.setOnClickListener(listener);
		return lesson;
	}

	public LayoutParams getLessonLayoutParams(int num){
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		if(num!=1&&num%columnNum==1){
			lp.addRule(RelativeLayout.BELOW, num-columnNum);
			lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			lp.topMargin = PADDING;
			lp.leftMargin = PADDING;
		}
		else if(num != 1){
			lp.addRule(RelativeLayout.ALIGN_LEFT,num-1);
			lp.addRule(RelativeLayout.BELOW, num-columnNum);
			lp.leftMargin = LESSON_WIDTH;
			lp.topMargin = PADDING;
		}
		else{
			lp.topMargin = PADDING;
			lp.leftMargin = PADDING;
		}
		return lp;
	}
}
