package com.wangyi.define.bean;

import com.wangyi.function.funchelp.JsonResponseParser;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;
import org.xutils.http.annotation.HttpResponse;

import java.text.SimpleDateFormat;
import java.sql.Date;

/**
 * Created by eason on 5/5/16.
 */
@HttpResponse(parser = JsonResponseParser.class)
@Table(name = "message")
public class Message {
    @Column(name = "id",isId=true)
    private String id;
    @Column(name = "uname")
    private String uname;
    @Column(name = "uPicurl")
    private String uPicUrl;
    @Column(name = "picurl")
    private String picUrl;
    @Column(name = "message")
    private String message;
    @Column(name = "date")
    private String date;
    @Column(name = "category")
    private String category;
    @Column(name = "isvisited")
    private boolean isvisited = false;

    public String getShortContent(){
        StringBuffer m = new StringBuffer(message);
        if(m.length()<15) return m.toString();
        return m.substring(0,15);
    }

    public void setuPicUrl(String uPicUrl) {
        this.uPicUrl = uPicUrl;
    }

    public String getuPicUrl() {
        return uPicUrl;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public boolean isvisited() {
        return isvisited;
    }

    public void setIsvisited(boolean isvisited) {
        this.isvisited = isvisited;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date.substring(0,10);
    }
}
