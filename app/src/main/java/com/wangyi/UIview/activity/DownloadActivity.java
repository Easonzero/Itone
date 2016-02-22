package com.wangyi.UIview.activity;

import org.xutils.view.annotation.*;
import com.wangyi.UIview.BaseActivity;
import com.wangyi.UIview.BaseFragment;
import com.wangyi.UIview.fragment.DownloadIngFragment;
import com.wangyi.reader.R;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

@ContentView(R.layout.download_me)
public class DownloadActivity extends BaseActivity{

	public BaseFragment[] mFragments;
	@ViewInject(R.id.ing)
	private Button ing;
	@ViewInject(R.id.ed)
	private Button ed;
	@ViewInject(R.id.control)
	private RelativeLayout control;
	private static int mCurIndicator = 0;
	private static int mEditState = 0;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		control.setVisibility(View.GONE);
		mFragments = new BaseFragment[2];
		mFragments[0] = (BaseFragment)getFragmentManager().findFragmentById(R.id.downloading);
		mFragments[1] = (BaseFragment)getFragmentManager().findFragmentById(R.id.downloaded);
		setFragmentIndicator(0);
	}

	@Event(R.id.back)
	private void onBackClick(View view){
		DownloadActivity.this.finish();
	}

	@Event(R.id.edit)
	private void onEditClick(View view){
		if(mEditState == 0){
			((Button)view).setText("完成");
			control.setVisibility(View.VISIBLE);
			((DownloadIngFragment)mFragments[0]).edit();
			mEditState = 1;
		}
		else{
			((Button)view).setText("编辑");
			control.setVisibility(View.GONE);
			((DownloadIngFragment)mFragments[0]).finish();
			mEditState = 0;
		}
	}

	@Event(R.id.button1)
	private void onAllSelectClick(View view){
		if(mCurIndicator == 0){
			((DownloadIngFragment)mFragments[0]).allselect();
		}
	}

	@Event(R.id.button2)
	private void onDeleteClick(View view){
		if(mCurIndicator == 0){
			((DownloadIngFragment)mFragments[0]).delete();
		}
	}

	@Event(R.id.ing)
	private void onIngClick(View view){
		setFragmentIndicator(0);
	}

	@Event(R.id.ed)
	private void onEdClick(View view){
		setFragmentIndicator(1);
	}

	private void setFragmentIndicator(int whichIsDefault) {
		getFragmentManager().beginTransaction().hide(mFragments[0])
				.hide(mFragments[1]).show(mFragments[whichIsDefault]).commit();
		setIndicator(whichIsDefault);
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
}
