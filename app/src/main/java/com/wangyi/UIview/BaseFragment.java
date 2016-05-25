package com.wangyi.UIview;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import org.xutils.x;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {

    private boolean injected = false;
    private Handler handler = null;
    private String title = "";
    private String message = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        injected = true;
        return x.view().inject(this, inflater, container);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!injected) {
            x.view().inject(this, this.getView());
        }
    }

    public void connect(Handler handler){
        this.handler = handler;
    }

    public void disconnect(){
        handler = null;
        message = null;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }

    public void setMessage(String message){
        this.message = message;
    }

    protected void sendMessage(int what,Object obj){
        handler.obtainMessage(what,obj).sendToTarget();
    }

    protected void sendMessage(int what){
        handler.sendEmptyMessage(what);
    }

    protected String getMessage(){
        return message;
    }
}
