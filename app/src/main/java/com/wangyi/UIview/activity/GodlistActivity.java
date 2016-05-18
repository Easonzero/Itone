package com.wangyi.UIview.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.marshalchen.ultimaterecyclerview.ItemTouchListenerAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.layoutmanagers.ScrollSmoothLineaerLayoutManager;
import com.wangyi.UIview.BaseActivity;
import com.wangyi.UIview.adapter.GodlistAdapter;
import com.wangyi.UIview.widget.LoadingDialog;
import com.wangyi.define.EventName;
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
                    adapter = new GodlistAdapter((ArrayList)msg.obj);
                    byorderlist.setLayoutManager(new ScrollSmoothLineaerLayoutManager(
                            getBaseContext(), LinearLayoutManager.VERTICAL, false, 300));
                    byorderlist.setAdapter(adapter);
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
        HttpsFunc.getInstance().connect(handler).getRankByOrder();
    }

    @Event(R.id.back)
    private void onBackClick(View view){
        this.finish();
    }
}