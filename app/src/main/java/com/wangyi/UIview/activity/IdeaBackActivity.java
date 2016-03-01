package com.wangyi.UIview.activity;

import android.os.Bundle;
import android.view.View;
import com.wangyi.UIview.BaseActivity;
import com.wangyi.reader.R;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

/**
 * Created by maxchanglove on 2016/2/29.
 */
@ContentView(R.layout.ideaback_me)
public class IdeaBackActivity extends BaseActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Event(R.id.back)
    private void onBackClick(View view){
        IdeaBackActivity.this.finish();
    }
}
