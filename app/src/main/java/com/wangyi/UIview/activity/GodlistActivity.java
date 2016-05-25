package com.wangyi.UIview.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.layoutmanagers.ScrollSmoothLineaerLayoutManager;
import com.wangyi.UIview.BaseActivity;
import com.wangyi.UIview.adapter.GodlistAdapter;
import com.wangyi.UIview.widget.dialog.LoadingDialog;
import com.wangyi.define.EventName;
import com.wangyi.define.UserRank;
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
    private LoadingDialog loading;
    private ArrayList<UserRank> users;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case EventName.UI.FINISH:
                    loading.dismiss();
                    break;
                case EventName.UI.START:
                    loading.show();
                    break;
                case EventName.UI.SUCCESS:
                    users = (ArrayList<UserRank>)msg.obj;
                    if(!adapter.isEmpty()) adapter.removeAll();
                    adapter.insert(users);
                    loading.dismiss();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        loading = new LoadingDialog(this);
        users = new ArrayList<UserRank>();
        adapter = new GodlistAdapter(test());
        byorderlist.setLayoutManager(new ScrollSmoothLineaerLayoutManager(
                getBaseContext(), LinearLayoutManager.VERTICAL, false, 300));
        byorderlist.setAdapter(adapter);
        HttpsFunc.getInstance().connect(handler).getRankByOrder();
    }

    private ArrayList<UserRank> test(){
        int i=1;
        ArrayList<UserRank> users = new ArrayList<UserRank>();
        String category = "ppt";
        while(i<11){
            UserRank user = new UserRank();
            user.url = "/res/user/ximin/";
            user.downloadNum = 1000 - i*10;
            user.rank = i;
            user.university = "哈尔滨工业大学";
            user.userName = "王祎";
            users.add(user);
            i++;
        }
        return users;
    }

    @Event(R.id.back)
    private void onBackClick(View view){
        this.finish();
    }
}