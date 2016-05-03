package com.wangyi.UIview.widget;

import com.wangyi.reader.R;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;

public class TurnToDialog extends AlertDialog {
	public View view;  
	public EditText editText; 
	public TurnToDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		LayoutInflater factory = LayoutInflater.from(context);
		view = factory.inflate(R.layout.turntodialog, null);
		editText = (EditText)view.findViewById(R.id.editTextBookName);
		this.setView(view);
	}

}
