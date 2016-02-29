package com.wangyi;

import android.app.Application;
import org.xutils.x;

/**
 * Created by maxchanglove on 2016/2/29.
 */
public class Itone extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
    }
}
