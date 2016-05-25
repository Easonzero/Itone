package com.wangyi.define;

import com.wangyi.function.funchelp.JsonResponseParser;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;
import org.xutils.http.annotation.HttpResponse;

import java.io.Serializable;
import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Created by eason on 5/21/16.
 */
@HttpResponse(parser = JsonResponseParser.class)
@Table(name = "homework")
public class Homework implements Serializable{
    @Column(name = "id",isId=true,autoGen = true)
    public String id;
    @Column(name = "uname")
    public String uname;
    @Column(name = "picurl")
    public String picUrl;
    @Column(name = "message")
    public String message;
    @Column(name = "sdate",property = "date")
    public Date sdate;
    @Column(name = "fdate",property = "date")
    public Date fdate;
    @Column(name = "course")
    public String course;
    @Column(name = "is")
    public boolean is;//true:必修，false：选修

    public String getSDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
        return sdf.format(sdate);
    }

    public String getFDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
        return sdf.format(fdate);
    }
}
