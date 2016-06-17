package com.wangyi.UIview.activity;

import android.widget.ListView;
import com.wangyi.UIview.adapter.DownloadListAdapter;
import org.xutils.view.annotation.*;
import com.wangyi.UIview.BaseActivity;
import com.wangyi.UIview.BaseFragment;
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

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        dla = new DownloadListAdapter(this);
        downloadList.setAdapter(dla);

		control.setVisibility(View.GONE);
	}

	@Event(R.id.back)
	private void onBackClick(View view){
		DownloadActivity.this.finish();
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
