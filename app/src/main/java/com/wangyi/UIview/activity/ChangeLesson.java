package com.wangyi.UIview.activity;

import org.xutils.view.annotation.*;

import com.wangyi.UIview.BaseActivity;
import com.wangyi.UIview.widget.PickWeeksDialog;
import com.wangyi.UIview.widget.PickclassesDialog;
import com.wangyi.define.EventName;
import com.wangyi.function.ScheduleFunc;
import com.wangyi.reader.R;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
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
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		initData();
	}

	private void initData(){
		this.setTitle("修改课程");
		_classFromTo.setText(ScheduleFunc.getInstance().getWeekStr(Integer.parseInt(ScheduleFunc.getInstance().lesson.getWeekDay()))
				+" " + ScheduleFunc.getInstance().lesson.getFromClass() + " - " + ScheduleFunc.getInstance().lesson.getToClass() + "节");
		_weekFromTo.setText(ScheduleFunc.getInstance().lesson.getWeeknumDelay());
		lessonName.setText(ScheduleFunc.getInstance().lesson.getLessonName());
		location.setText(ScheduleFunc.getInstance().lesson.getClassRoom());
		teacher.setText(ScheduleFunc.getInstance().lesson.getTeacher());
	}

	@Event(R.id.ClassFromTo)
	private void onClassClick(View view){
		final PickclassesDialog dialog = new PickclassesDialog(ChangeLesson.this,ScheduleFunc.getInstance().lesson.getWeekDay(),ScheduleFunc.getInstance().lesson.getFromClass());
		dialog.setTitle("上课节数选择")
		.setPositiveButton("确定", new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						ScheduleFunc.getInstance().lesson.setWeekDay((dialog.date + 1) + "");
						ScheduleFunc.getInstance().lesson.setFromClass((dialog.fromClass + 1) + "");
						ScheduleFunc.getInstance().lesson.setToClass((dialog.toClass + 1) + "");
						_classFromTo.setText(ScheduleFunc.getInstance().getWeekStr(Integer.parseInt(ScheduleFunc.getInstance().lesson.getWeekDay()))
								+" "+ScheduleFunc.getInstance().lesson.getFromClass()+"-"+ScheduleFunc.getInstance().lesson.getToClass()+"节");
						dialog.dismiss();
					}
				})
				.setNegativeButton("取消", new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
		dialog.show();
	}

	@Event(R.id.confirm)
	private void onConfirmClick(View view){
		if(lessonName.getText().toString().equals("")) return;
        ScheduleFunc.getInstance().lesson.setClassRoom(location.getText().toString());
        ScheduleFunc.getInstance().lesson.setTeacher(teacher.getText().toString());
        ScheduleFunc.getInstance().lesson.setLessonName(lessonName.getText().toString());
        ScheduleFunc.getInstance().update();
		ChangeLesson.this.finish();
	}

	@Event(R.id.Cancel)
	private void onCancelClick(View view){
		ChangeLesson.this.finish();
	}
}
