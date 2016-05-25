package com.wangyi.UIview.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.wangyi.UIview.BaseActivity;
import com.wangyi.UIview.adapter.HomeworkAdapter;
import com.wangyi.UIview.widget.dialog.LoadingDialog;
import com.wangyi.define.EventName;
import com.wangyi.define.Homework;
import com.wangyi.reader.R;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by eason on 5/21/16.
 */
@ContentView(R.layout.homework)
public class HomeWorkActivity extends BaseActivity {
    @ViewInject(R.id.homework)
    private UltimateRecyclerView homeworkList;
    private HomeworkAdapter adapter;
    private ArrayList<Homework> homework;
    private LinearLayoutManager linearLayoutManager;
    private LoadingDialog loading;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch(msg.what){
                case EventName.UI.START:
                    loading.show();
                    break;
                case EventName.UI.FAULT:
                    loading.dismiss();
                    break;
                case EventName.UI.SUCCESS:
                    ArrayList<Homework> homeworks = (ArrayList<Homework>) msg.obj;
                    loading.dismiss();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loading = new LoadingDialog(this);
        homework = new ArrayList<>();
        adapter = new HomeworkAdapter(this);
        adapter.addAll(HomeworkAdapter.getPreCodeMenu(test()), 0);
        linearLayoutManager = new LinearLayoutManager(this);
        homeworkList.setLayoutManager(linearLayoutManager);
        homeworkList.setEmptyView(R.layout.emptylist,UltimateRecyclerView.EMPTY_SHOW_LOADMORE_ONLY);
        homeworkList.setAdapter(adapter);

    }

    private Homework[] test(){
        int i=0;
        Homework[] homeworks = new Homework[10];
        boolean is = true;
        String course = "工数";
        while(i<10){
            Homework homework = new Homework();
            homework.message = "作业内容";
            homework.uname = "王祎";
            homework.fdate = new Date(16,5,28);
            homework.sdate = new Date(16,5,20);
            homework.course = course;
            homework.is = is;
            homeworks[i] = homework;
            if(i == 6){
                is = false;
                course = "英语";
            }
            i++;
        }
        return homeworks;
    }

    @Event(R.id.back)
    private void onBackClick(View view){
        this.finish();
    }
}
