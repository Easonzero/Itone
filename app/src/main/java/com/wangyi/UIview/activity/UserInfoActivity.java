package com.wangyi.UIview.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.wangyi.UIview.fragment.RegisterFragment4;
import com.wangyi.define.EventName;
import com.wangyi.reader.R;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.x;

/**
 * Created by eason on 5/20/16.
 */
@ContentView(R.layout.activity_userinfo)
public class UserInfoActivity extends AppCompatActivity {
    private RegisterFragment4 mFragment;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch(msg.what){
                case EventName.UI.FINISH:
                    x.image().clearMemCache();
                    x.image().clearCacheFiles();
                    UserInfoActivity.this.finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        mFragment = (RegisterFragment4) getSupportFragmentManager().findFragmentById(R.id.fragment4);
        mFragment.setData();
        mFragment.connect(handler);
    }

    @Event(R.id.back)
    private void onBackClick(View view){
        this.finish();
    }

    @Event(R.id.save)
    private void onConfirmClick(View view){
        mFragment.modify();
    }
}
