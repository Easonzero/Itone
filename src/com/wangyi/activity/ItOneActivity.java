package com.wangyi.activity;

import com.wangyi.database.DBLesson;
import com.wangyi.widget.FragmentIndicator;
import com.wangyi.widget.FragmentIndicator.OnIndicateListener;
import com.zreader.main.R;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class ItOneActivity extends FragmentActivity{
	public static Fragment[] mFragments;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
		setFragmentIndicator(0);
	}

	private void setFragmentIndicator(int whichIsDefault) {
		mFragments = new Fragment[3];
		mFragments[0] = getSupportFragmentManager().findFragmentById(R.id.home);
		mFragments[1] = getSupportFragmentManager().findFragmentById(R.id.allsubject);
		mFragments[2] = getSupportFragmentManager().findFragmentById(R.id.me);

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
