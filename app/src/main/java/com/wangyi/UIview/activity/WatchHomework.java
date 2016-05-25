package com.wangyi.UIview.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wangyi.UIview.BaseActivity;
import com.wangyi.define.Homework;
import com.wangyi.reader.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by eason on 5/23/16.
 */
@ContentView(R.layout.watchhomework)
public class WatchHomework extends BaseActivity {
    @ViewInject(R.id.name)
    private TextView name;
    @ViewInject(R.id.date)
    private TextView date;
    @ViewInject(R.id.content)
    private TextView content;
    @ViewInject(R.id.uploader)
    private TextView uploader;

    private Homework homework;

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        homework = (Homework)getIntent().getSerializableExtra("homework");
        uploader.setText("上传者："+homework.uname);
        name.setText(homework.course);
        date.setText(homework.sdate+" ~ "+homework.fdate);
        content.setText(homework.message);
    }

    @Event(R.id.back)
    private void onBackClick(View view){
        this.finish();
    }
}
