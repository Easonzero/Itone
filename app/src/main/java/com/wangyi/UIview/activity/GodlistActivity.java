package com.wangyi.UIview.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.wangyi.UIview.BaseActivity;
import com.wangyi.UIview.adapter.GodlistAdapter;
import com.wangyi.UIview.widget.dialog.LoadingDialog;
import com.wangyi.UIview.widget.view.UltimateListView;
import com.wangyi.define.EventName;
import com.wangyi.define.bean.UserRank;
import com.wangyi.function.HttpsFunc;
import com.wangyi.reader.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * Created by eason on 5/3/16.
 */
@ContentView(R.layout.byorderlist)
public class GodlistActivity extends BaseActivity {
    @ViewInject(value = R.id.byorderlist)
    private UltimateRecyclerView byorderlist;

    private GodlistAdapter adapter;
    private ArrayList<UserRank> users;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case EventName.UI.SUCCESS:
                    ArrayList<UserRank> user = (ArrayList<UserRank>)msg.obj;
                    if(!adapter.isEmpty()) adapter.removeAll();
                    adapter.insert(user);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        users = new ArrayList<UserRank>();
        adapter = new GodlistAdapter(users);
        initByorderlist();
        HttpsFunc.getInstance().connect(handler).getRankByOrder();
    }

    private void initByorderlist(){
        UltimateListView view = new UltimateListView(byorderlist,adapter,this);
        view.beforeFuncset();
        view.enableRefresh(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        byorderlist.setRefreshing(false);
                        HttpsFunc.getInstance().connect(handler).getRankByOrder();
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