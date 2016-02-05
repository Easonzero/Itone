package com.wangyi.widget;


import com.wangyi.reader.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class PickWeeksDialog extends AlertDialog {
	public int[] weeks;
	public View view;
	public PickWeeksDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		LayoutInflater factory = LayoutInflater.from(context);
		view = factory.inflate(R.layout.pick_weeks_dialog, null);
		weeks = new int[]{R.id.week1,R.id.week2,R.id.week3,R.id.week4,R.id.week5,
				R.id.week6,R.id.week7,R.id.week8,R.id.week9,R.id.week10,R.id.week11,R.id.week12,
				R.id.week13,R.id.week14,R.id.week15,R.id.week16,R.id.week17,R.id.week18,R.id.week19,
				R.id.week20,R.id.week21,R.id.week22,R.id.week23,R.id.week24,R.id.week25};
		this.setView(view);
	}

}
