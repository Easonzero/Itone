package com.wangyi.function;

import android.content.Context;
import android.os.Handler;

import com.wangyi.define.EventName;
import com.wangyi.define.Message;
import com.wangyi.function.funchelp.Function;
import com.wangyi.utils.PreferencesReader;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eason on 5/8/16.
 */

public class MessageFunc implements Function {
    private static final MessageFunc INSTANCE = new MessageFunc();

    private MessageFunc(){}

    public static MessageFunc getInstance(){
        return INSTANCE;
    }

    private Handler handler;

    private DbManager db;

    @Override
    public void init(Context context) {
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                .setDbName("message")
                .setDbVersion(1);
        db = x.getDb(daoConfig);
    }

    public void visitRomoteMessage(String category){
        String date = PreferencesReader.getMessageDate();
        if(date==null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date = sdf.format(new java.util.Date());
        }
        if(handler!=null)
            HttpsFunc.getInstance().connect(handler).getMessage(date,category);
    }

    public List<Message> getMessage(int offset,String category){
        List<Message> dataList = new ArrayList<>();
        //test
        int i = 10;
        while(i>0){
            Message msg = new Message();
            msg.setCategory(category);
            msg.setMessage("教室改为b203");
            msg.setDate(new Date(2016,4,i+1));
            msg.setPicUrl("/res/user/ximin/");
            msg.setIsvisited(false);
            msg.setUid("西西");
            dataList.add(msg);
            i--;
        }
        /*try {
            dataList = db.selector(Message.class).where("category","=",category).limit(10).offset(offset).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }*/
        handler.sendEmptyMessage(EventName.UI.SUCCESS);
        return dataList;
    }

    public void addMessages(List<Message> messages){
        try {
            db.save(messages);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public MessageFunc connect(Handler handler){
        if(this.handler != null)
            this.handler = null;
        this.handler = handler;
        return INSTANCE;
    }

    public void disconnect(){
        this.handler = null;
    }
}
