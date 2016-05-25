package com.wangyi.define;

import com.wangyi.function.funchelp.JsonResponseParser;

import org.xutils.http.annotation.HttpResponse;

/**
 * Created by eason on 5/9/16.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class UserRank {
    public String userName;
    public int downloadNum;
    public String university;
    public int rank;
    public String url;
}
