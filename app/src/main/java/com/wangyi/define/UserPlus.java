package com.wangyi.define;

import com.wangyi.function.funchelp.JsonResponseParser;
import org.xutils.http.annotation.HttpResponse;

/**
 * Created by eason on 5/5/16.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class UserPlus {
    public String id;
    public int money;
    public int downloadNum;
}
