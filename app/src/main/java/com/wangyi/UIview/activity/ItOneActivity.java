package com.wangyi.UIview.activity;

import com.wangyi.function.*;
import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import com.wangyi.UIview.BaseActivity;
import com.wangyi.UIview.BaseFragment;
import com.wangyi.UIview.widget.FragmentIndicator;
import com.wangyi.UIview.widget.FragmentIndicator.OnIndicateListener;
import com.wangyi.reader.R;
import android.os.Bundle;
import android.view.View;

@ContentView(R.layout.activity_main)
public class ItOneActivity extends BaseActivity{
	private BaseFragment[] mFragments;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		x.Ext.init(getApplication());

		ScheduleFunc.getInstance().init(this);
		BookManagerFunc.getInstance().init(this);
		UserManagerFunc.getInstance().init(this);
		HttpsFunc.getInstance().init(this);
		//以下context必须为application context
		SensorFunc.getInstance().init(getApplication());

		setFragmentIndicator(0);
	}

	private void setFragmentIndicator(int whichIsDefault) {
		mFragments = new BaseFragment[3];
		mFragments[0] = (BaseFragment)getSupportFragmentManager().findFragmentById(R.id.home);
		mFragments[1] = (BaseFragment)getSupportFragmentManager().findFragmentById(R.id.allsubject);
		mFragments[2] = (BaseFragment)getSupportFragmentManager().findFragmentById(R.id.me);

		getSupportFragmentManager().beginTransaction().hide(mFragments[0])
				.hide(mFragments[1]).hide(mFragments[2])
				.show(mFragments[whichIsDefault]).commit();

		FragmentIndicator mIndicator = (FragmentIndicator) findViewById(R.id.indicator);
		FragmentIndicator.setIndicator(whichIsDefault);
		mIndicator.setOnIndicateListener(new OnIndicateListener() {

			@Override
			public void onIndicate(View v, int which) {
				getSupportFragmentManager().beginTransaction()
						.hide(mFragments[0]).hide(mFragments[1])
						.hide(mFragments[2]).show(mFragments[which]).commit();
			}
		});
	}
}
