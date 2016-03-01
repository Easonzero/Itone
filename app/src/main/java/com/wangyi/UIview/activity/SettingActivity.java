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
@ContentView(R.layout.set_me)
public class SettingActivity extends BaseActivity {
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Event(R.id.back)
    private void onBackClick(View view){
        SettingActivity.this.finish();
    }
}
