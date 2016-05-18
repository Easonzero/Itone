package com.wangyi.define;

import com.wangyi.function.funchelp.JsonResponseParser;

import org.xutils.http.annotation.HttpResponse;

/**
 * Created by eason on 5/9/16.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class UserRank {
    public String userName;
    public String downloadNum;
    public String from;
    public String rank;
    public String url;
}
