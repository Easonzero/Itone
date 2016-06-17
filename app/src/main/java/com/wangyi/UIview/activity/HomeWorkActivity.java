package com.wangyi.UIview.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.wangyi.UIview.BaseActivity;
import com.wangyi.UIview.adapter.HomeworkAdapter;
import com.wangyi.UIview.widget.view.UltimateListView;
import com.wangyi.define.EventName;
import com.wangyi.define.bean.Homework;
import com.wangyi.function.HomeworkFunc;
import com.wangyi.reader.R;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eason on 5/21/16.
 */
@ContentView(R.layout.homework)
public class HomeWorkActivity extends BaseActivity {
    @ViewInject(R.id.homework)
    private UltimateRecyclerView homeworkList;
    private HomeworkAdapter adapter;
    private List<Homework> homework;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch(msg.what){
                case EventName.UI.FINISH:
                    homework = HomeworkFunc.getInstance().getHomework();
                    adapter.removeAll();
                    adapter.addAll(HomeworkAdapter.getPreCodeMenu(
                            homework.toArray(new Homework[homework.size()])), 0);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        homework = new ArrayList<>();
        adapter = new HomeworkAdapter(this);

        initExpList();

        HomeworkFunc.getInstance().connect(handler).visitRomoteHomework();
    }

    private void initExpList(){
        UltimateListView view = new UltimateListView(homeworkList,adapter,this);
        view.beforeFuncset();
        view.enableAnimator();
        view.enableRefresh(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        homeworkList.setRefreshing(false);
                        HomeworkFunc.getInstance().connect(handler).visitRomoteHomework();
                    }
                }, 1000);
            }
        });
        view.afterFuncset();
    }

    @Event(R.id.back)
    private void onBackClick(View view){
        this.finish();
    }
}
