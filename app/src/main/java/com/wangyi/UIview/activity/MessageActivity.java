package com.wangyi.UIview.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.marshalchen.ultimaterecyclerview.CustomUltimateRecyclerview;
import com.marshalchen.ultimaterecyclerview.ItemTouchListenerAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.layoutmanagers.ScrollSmoothLineaerLayoutManager;
import com.wangyi.UIview.BaseActivity;
import com.wangyi.UIview.adapter.MessageListAdapter;
import com.wangyi.UIview.widget.LoadingDialog;
import com.wangyi.define.EventName;
import com.wangyi.define.Message;
import com.wangyi.function.MessageFunc;
import com.wangyi.reader.R;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import java.util.ArrayList;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;

/**
 * Created by eason on 5/3/16.
 */
@ContentView(value= R.layout.message)
public class MessageActivity extends BaseActivity{
    @ViewInject(value = R.id.messagelist)
    private CustomUltimateRecyclerview messagelist;
    private MessageListAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private int offset = 0;
    private LoadingDialog loading;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(android.os.Message msg){
            switch(msg.what){
                case EventName.UI.START:
                    loading.show();
                    break;
                case EventName.UI.FINISH:
                    offset += 10;
                    loading.dismiss();
                    break;
                case EventName.UI.SUCCESS:
                    linearLayoutManager.scrollToPosition(0);
                    messagelist.mPtrFrameLayout.refreshComplete();
                    break;
            }
        }
    };
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        loading = new LoadingDialog(this);
        String category = getIntent().getStringExtra("category");
        //MessageFunc.getInstance().connect(handler).visitRomoteMessage(category);
        messagelist.setCustomSwipeToRefresh();
        linearLayoutManager = new ScrollSmoothLineaerLayoutManager(
                this, LinearLayoutManager.VERTICAL, false, 300);
        messagelist.setLayoutManager(linearLayoutManager);
        messagelist.setEmptyView(R.layout.emptylist,UltimateRecyclerView.EMPTY_SHOW_LOADMORE_ONLY);
        adapter = new MessageListAdapter(MessageFunc.getInstance().connect(handler).getMessage(offset,EventName.Message.MESSAGE));
        //adapter.insert(MessageFunc.getInstance().getMessage(offset,EventName.Message.MESSAGE));
        messagelist.setAdapter(adapter);
        StoreHouseHeader storeHouseHeader = new StoreHouseHeader(this);
        storeHouseHeader.initWithString("Loading...");
        messagelist.mPtrFrameLayout.setHeaderView(storeHouseHeader);
        messagelist.mPtrFrameLayout.addPtrUIHandler(storeHouseHeader);
        messagelist.mPtrFrameLayout.autoRefresh(false);
        messagelist.mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout ptrFrameLayout, View view, View view2) {
                boolean canbePullDown = PtrDefaultHandler.checkContentCanBePulledDown(ptrFrameLayout, view, view2);
                return canbePullDown;
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                adapter.insert(MessageFunc.getInstance().connect(handler).getMessage(offset+=10,EventName.Message.MESSAGE));
            }
        });
        ItemTouchListenerAdapter itemTouchListenerAdapter = new ItemTouchListenerAdapter(messagelist.mRecyclerView,
                new ItemTouchListenerAdapter.RecyclerViewOnItemClickListener() {
                    @Override
                    public void onItemClick(RecyclerView parent, View clickedView, int position) {
                    }

                    @Override
                    public void onItemLongClick(RecyclerView recyclerView, View view, int i) {}
                });
        messagelist.mRecyclerView.addOnItemTouchListener(itemTouchListenerAdapter);
    }

    @Event(R.id.back)
    private void onBackClick(View view){
        this.finish();
    }
}
