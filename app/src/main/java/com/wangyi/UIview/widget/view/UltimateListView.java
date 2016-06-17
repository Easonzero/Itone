package com.wangyi.UIview.widget.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;

import com.marshalchen.ultimaterecyclerview.ItemTouchListenerAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.marshalchen.ultimaterecyclerview.layoutmanagers.ScrollSmoothLineaerLayoutManager;
import com.wangyi.reader.R;

/**
 * Created by eason on 6/11/16.
 */
public class UltimateListView {
    private LinearLayoutManager linearLayoutManager;
    private UltimateRecyclerView view;
    private UltimateViewAdapter adapter;
    private Context context;
    public UltimateListView(UltimateRecyclerView view,UltimateViewAdapter adapter,Context context){
        this.view = view;
        this.adapter = adapter;
        this.context = context;
    }

    public void beforeFuncset(){
        linearLayoutManager = new ScrollSmoothLineaerLayoutManager(
                context, LinearLayoutManager.VERTICAL, false, 300);
        view.setHasFixedSize(false);
        view.setLayoutManager(linearLayoutManager);
        view.setEmptyView(R.layout.emptylist,UltimateRecyclerView.EMPTY_KEEP_HEADER_AND_LOARMORE);
    }

    public void afterFuncset(){
        view.setAdapter(adapter);
    }

    protected boolean status_progress = false;

    public void enableLoadmore(UltimateRecyclerView.OnLoadMoreListener listener){
        view.setLoadMoreView(R.layout.process_foot_view);
        view.setOnLoadMoreListener(listener);
        view.reenableLoadmore();
    }

    public void enableRefresh(SwipeRefreshLayout.OnRefreshListener listener){
        view.setDefaultOnRefreshListener(listener);
    }

    public void enableItemClick(ItemTouchListenerAdapter itemTouchListenerAdapter){
        view.mRecyclerView.addOnItemTouchListener(itemTouchListenerAdapter);
    }

    public void enableAnimator(){
        view.getItemAnimator().setAddDuration(100);
        view.getItemAnimator().setRemoveDuration(100);
        view.getItemAnimator().setMoveDuration(200);
        view.getItemAnimator().setChangeDuration(100);
    }
}
