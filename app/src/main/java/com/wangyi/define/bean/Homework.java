package com.wangyi.define.bean;

import com.wangyi.function.funchelp.JsonResponseParser;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;
import org.xutils.http.annotation.HttpResponse;

import java.io.Serializable;

/**
 * Created by eason on 5/21/16.
 */
@HttpResponse(parser = JsonResponseParser.class)
@Table(name = "homework")
public class Homework implements Serializable{
    @Column(name = "uname")
    public String uname;
    @Column(name = "picurl")
    public String picUrl;
    @Column(name = "message")
    public String message;
    @Column(name = "sdate")
    public String sdate;
    @Column(name = "fdate")
    public String fdate;
    @Column(name = "course",isId=true)
    public String course;
    @Column(name = "is")
    public int is;//1:必修，0：选修

    public String getSDate() {
        return sdate.substring(0,10);
    }

    public String getFDate() {
        return fdate.substring(0,10);
    }
}
