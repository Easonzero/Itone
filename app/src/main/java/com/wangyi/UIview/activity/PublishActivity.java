package com.wangyi.UIview.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.wangyi.UIview.BaseActivity;
import com.wangyi.UIview.BaseFragment;
import com.wangyi.reader.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by eason on 7/24/16.
 */
@ContentView(R.layout.publish)
public class PublishActivity extends BaseActivity {
    private BaseFragment[] mFragments;
    @ViewInject(R.id.switch_contain)
    private LinearLayout switchContainer;
    @ViewInject(R.id.bt_homework)
    private Button homeworkBt;
    @ViewInject(R.id.bt_message)
    private Button messageBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        initFragment(0);
    }

    private void initFragment(int which){
        mFragments = new BaseFragment[2];
        mFragments[0] = (BaseFragment)getSupportFragmentManager().findFragmentById(R.id.message);
        mFragments[1] = (BaseFragment)getSupportFragmentManager().findFragmentById(R.id.homework);

        getSupportFragmentManager().beginTransaction().hide(mFragments[0])
                .hide(mFragments[1]).show(mFragments[which]).commit();
    }

    @Event(R.id.bt_message)
    private void onMessageClick(View view){
        getSupportFragmentManager().beginTransaction().hide(mFragments[0])
                .hide(mFragments[1]).show(mFragments[0]).commit();
        switchContainer.setBackgroundResource(R.drawable.bg_publish_message);
        messageBt.setTextColor(0xffffffff);
        homeworkBt.setTextColor(0xff616161);
    }

    @Event(R.id.bt_homework)
    private void onHomeworkClick(View view){
        getSupportFragmentManager().beginTransaction().hide(mFragments[0])
                .hide(mFragments[1]).show(mFragments[1]).commit();
        switchContainer.setBackgroundResource(R.drawable.bg_publish_homework);
        homeworkBt.setTextColor(0xffffffff);
        messageBt.setTextColor(0xff616161);
    }

    @Event(R.id.back)
    private void onBackClick(View view){
        this.finish();
    }
}
