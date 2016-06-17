package com.wangyi.define.bean;

import com.wangyi.function.funchelp.JsonResponseParser;

import org.xutils.http.annotation.HttpResponse;

/**
 * Created by eason on 6/14/16.
 */
@HttpResponse(parser = JsonResponseParser.class)
public class University {
    public long id;
    public String name;
}
