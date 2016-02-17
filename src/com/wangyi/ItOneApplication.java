package com.wangyi;

import org.xutils.x;

import com.wangyi.function.BookManagerFunc;
import com.wangyi.function.ScheduleFunc;

import android.app.Application;

public class ItOneApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(true);
    }
}