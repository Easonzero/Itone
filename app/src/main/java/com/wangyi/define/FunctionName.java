package com.wangyi.define;

import com.wangyi.function.*;
import com.wangyi.function.funchelp.Function;

/**
 * Created by maxchanglove on 2016/3/8.
 */
public enum FunctionName {
    BOOK_MANAGER(BookManagerFunc.getInstance()),
    DOWNLOAD_MANAGER(DownloadManagerFunc.getInstance()),
    HTTPS(HttpsFunc.getInstance()),
    SCHEDULE(ScheduleFunc.getInstance()),
    SENSOR(SensorFunc.getInstance()),
    USER_MANAGER(UserManagerFunc.getInstance());
    private static final FunctionName[] VALUES = values();
    private static final int SIZE = VALUES.length;
    private Function mFunction;

    public static int size() {
        return SIZE;
    }

    public static FunctionName valueOf(int i) {
        return VALUES[i];
    }

    public static FunctionName[] getValues() {
        return VALUES;
    }

    FunctionName(Function function) {
        mFunction = function;
    }

    public Function getFunction() {
        return mFunction;
    }
}
