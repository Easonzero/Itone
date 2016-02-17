package com.wangyi.view.activity;

import org.xutils.view.annotation.ContentView;

import com.wangyi.view.BaseActivity;
import com.wangyi.widget.FragmentIndicator;
import com.wangyi.widget.FragmentIndicator.OnIndicateListener;
import com.wangyi.function.BookManagerFunc;
import com.wangyi.function.ScheduleFunc;
import com.wangyi.reader.R;

import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

@ContentView(R.layout.activity_main)
public class ItOneActivity extends BaseActivity{
	public static Fragment[] mFragments;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ScheduleFunc.getInstance().init(this);
        BookManagerFunc.getInstance().init(this);
        
		setFragmentIndicator(0);
	}

	private void setFragmentIndicator(int whichIsDefault) {
		mFragments = new Fragment[3];
		mFragments[0] = getFragmentManager().findFragmentById(R.id.home);
		mFragments[1] = getFragmentManager().findFragmentById(R.id.allsubject);
		mFragments[2] = getFragmentManager().findFragmentById(R.id.me);

		getFragmentManager().beginTransaction().hide(mFragments[0])
				.hide(mFragments[1]).hide(mFragments[2])
				.show(mFragments[whichIsDefault]).commit();

		FragmentIndicator mIndicator = (FragmentIndicator) findViewById(R.id.indicator);
		FragmentIndicator.setIndicator(whichIsDefault);
		mIndicator.setOnIndicateListener(new OnIndicateListener() {

			@Override
			public void onIndicate(View v, int which) {
				getFragmentManager().beginTransaction()
						.hide(mFragments[0]).hide(mFragments[1])
						.hide(mFragments[2]).show(mFragments[which]).commit();
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		BookManagerFunc.getInstance().finish();
		super.onDestroy();
	}
}
