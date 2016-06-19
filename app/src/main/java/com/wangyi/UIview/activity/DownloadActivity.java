package com.wangyi.UIview.activity;

import android.os.Handler;
import android.os.Message;
import android.widget.ListView;
import com.wangyi.UIview.adapter.DownloadListAdapter;
import org.xutils.view.annotation.*;
import com.wangyi.UIview.BaseActivity;
import com.wangyi.UIview.BaseFragment;
import com.wangyi.define.EventName;
import com.wangyi.reader.R;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

@ContentView(R.layout.download_me)
public class DownloadActivity extends BaseActivity{
	@ViewInject(R.id.control)
	private RelativeLayout control;
	@ViewInject(R.id.downloadlist)
	private ListView downloadList;

    private DownloadListAdapter dla;
	private int editTag = 0;

	private Handler handler = new Handler();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        dla = new DownloadListAdapter(this);
        downloadList.setAdapter(dla);
		run.run();

		control.setVisibility(View.GONE);
	}

	private Runnable run = new Runnable(){

		@Override
		public void run() {
			dla.notifyDataSetChanged();
			handler.postDelayed(this,1000);
		}
	};

	@Event(R.id.back)
	private void onBackClick(View view){
		handler.removeCallbacks(run);
		DownloadActivity.this.finish();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		handler.removeCallbacks(run);
	}

	@Event(R.id.edit)
	private void onEditClick(View view){
		if(editTag == 0){
			((Button)view).setText("完成");
			control.setVisibility(View.VISIBLE);
			dla.setEditMode();
			dla.notifyDataSetChanged();
            editTag = 1;
		}else{
			((Button)view).setText("编辑");
			control.setVisibility(View.GONE);
			dla.setEditMode();
			dla.clearSelect();
			dla.notifyDataSetChanged();
            editTag = 0;
		}
	}

	@Event(R.id.allselect)
	private void onAllSelectClick(View view){
        dla.allSelect();
		dla.notifyDataSetChanged();
	}

	@Event(R.id.delete)
	private void onDeleteClick(View view){
        dla.delect();
		dla.notifyDataSetChanged();
	}
}
