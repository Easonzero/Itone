package com.wangyi.activity;

import com.wangyi.fragment.DownloadIngFragment;
import com.zreader.main.R;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

public class DownloadActivity extends FragmentActivity{
	
	public static Fragment[] mFragments;
	private Button ing;
	private Button ed;
	private Button edit;
	private Button back;
	private Button allselect;
	private Button delete;
	private RelativeLayout control;
	private static int mCurIndicator = 0;
	private static int mEditState = 0;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.download_me);
		
		ing = (Button) findViewById(R.id.ing);
		ed = (Button) findViewById(R.id.ed);
		edit = (Button) findViewById(R.id.edit);
		back = (Button) findViewById(R.id.back);
		control = (RelativeLayout) findViewById(R.id.control);
		allselect = (Button) control.findViewById(R.id.button1);
		delete = (Button) control.findViewById(R.id.button2);
		
		control.setVisibility(View.GONE);
		setFragmentIndicator(0);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
		
		allselect.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				if(mCurIndicator == 0){
					((DownloadIngFragment)mFragments[0]).allselect();
				}
			}
		
		});
		
		delete.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				if(mCurIndicator == 0){
					((DownloadIngFragment)mFragments[0]).delete();
				}
			}
			
		});
		
		edit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
					if(mEditState == 0){
						((Button)view).setText("Íê³É");
						control.setVisibility(View.VISIBLE);
						((DownloadIngFragment)mFragments[0]).edit();
						mEditState = 1;
					}
					else{
						((Button)view).setText("±à¼­");
						control.setVisibility(View.GONE);
						((DownloadIngFragment)mFragments[0]).finish();
						mEditState = 0;
					}
			}
			
		});
		
		back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				DownloadActivity.this.finish();
			}
			
		});
	}
	
	private void setFragmentIndicator(int whichIsDefault) {
		mFragments = new Fragment[2];
		mFragments[0] = getSupportFragmentManager().findFragmentById(R.id.downloading);
		mFragments[1] = getSupportFragmentManager().findFragmentById(R.id.downloaded);

		getSupportFragmentManager().beginTransaction().hide(mFragments[0])
				.hide(mFragments[1]).show(mFragments[whichIsDefault]).commit();

		setIndicator(whichIsDefault);
		
		ing.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getSupportFragmentManager().beginTransaction()
				.hide(mFragments[0]).hide(mFragments[1]).show(mFragments[0]).commit();
				setIndicator(0);
			}
			
		});
		
		
		ed.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getSupportFragmentManager().beginTransaction()
				.hide(mFragments[0]).hide(mFragments[1]).show(mFragments[1]).commit();
				setIndicator(1);
			}
			
		});
	}
	
	public void setIndicator(int which) {
		switch (mCurIndicator) {
		case 0:
			ing.setBackgroundResource(R.drawable.download_ing_normal);
			break;
		case 1:
			ed.setBackgroundResource(R.drawable.download_ed_normal);
			break;
		}
		
		switch (which) {
		case 0:
			ing.setBackgroundResource(R.drawable.download_ing_select);
			break;
		case 1:
			ed.setBackgroundResource(R.drawable.download_ed_select);
			break;
		}

		mCurIndicator = which;
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
