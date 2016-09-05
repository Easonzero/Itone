package com.wangyi.UIview.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.marshalchen.ultimaterecyclerview.ItemTouchListenerAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.wangyi.UIview.BaseActivity;
import com.wangyi.UIview.adapter.MessageListAdapter;
import com.wangyi.UIview.widget.view.UltimateListView;
import com.wangyi.define.EventName;
import com.wangyi.define.bean.Message;
import com.wangyi.function.MessageFunc;
import com.wangyi.reader.R;
import com.wangyi.utils.ItOneUtils;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by eason on 5/3/16.
 */
@ContentView(value= R.layout.message)
public class MessageActivity extends BaseActivity{
    @ViewInject(value = R.id.messagelist)
    private UltimateRecyclerView messagelist;
    private MessageListAdapter adapter;
    private List<Message> messages;
    private int offset = 0;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(android.os.Message msg){
            switch(msg.what){
                case EventName.UI.FINISH:
                    offset = 0;
                    adapter.removeAll();
                    List<Message> messages = MessageFunc.getInstance().connect(handler).getMessage(offset);
                    adapter.insert(messages);
                    offset += messages.size();
                    break;
            }
        }
    };
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        messages = new ArrayList<Message>();
        adapter = new MessageListAdapter(messages);
        initMessagelist();

        MessageFunc.getInstance().connect(handler).visitRomoteMessage();
    }

    protected boolean status_progress = false;

    private void initMessagelist(){
        UltimateListView view = new UltimateListView(messagelist,adapter,this);
        view.beforeFuncset();
        view.enableRefresh(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        messagelist.setRefreshing(false);
                        MessageFunc.getInstance().connect(handler).visitRomoteMessage();
                    }
                }, 1000);
            }
        });
        view.enableLoadmore(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, final int maxLastVisiblePosition) {
                status_progress = true;
                handler.postDelayed(new Runnable() {
                    public void run() {
                        List<Message> messages = MessageFunc.getInstance().connect(handler).getMessage(offset);
                        adapter.insert(messages);
                        if(messages.size() != 0)
                            offset += messages.size();
                        else {
                            ItOneUtils.showToast(MessageActivity.this, "已经加载全部资源");
                            adapter.getCustomLoadMoreView().setVisibility(View.GONE);
                        }
                        status_progress = false;
                    }
                }, 500);
            }
        });
        view.enableItemClick(new ItemTouchListenerAdapter(messagelist.mRecyclerView,
                new ItemTouchListenerAdapter.RecyclerViewOnItemClickListener() {
                    @Override
                    public void onItemClick(RecyclerView parent, View clickedView, int position) {

                    }

                    @Override
                    public void onItemLongClick(RecyclerView recyclerView, View view, int i) {}
                }
        ));
        view.afterFuncset();
    }

    @Event(R.id.back)
    private void onBackClick(View view){
        this.finish();
    }
}
