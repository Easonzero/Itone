package com.wangyi.function;

import android.content.Context;
import android.os.Handler;

import com.wangyi.define.bean.Message;
import com.wangyi.function.funchelp.Function;
import com.wangyi.utils.PreferencesReader;

import org.xutils.DbManager;
import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.db.table.DbModel;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.sql.Date;
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

    public void visitRomoteMessage(){
        String date = null;
        try {
            DbModel model = db.selector(Message.class).select("MAX(date) as date").findFirst();
            if(model != null){
                date = model.getString("date");
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        if(date==null) {
            date = "2016-05-10";
        }
        if(handler!=null)
            HttpsFunc.getInstance().connect(handler).getMessage(date);
    }

    public List<Message> getMessage(int offset){
        List<Message> dataList = new ArrayList<>();
        try {
            dataList = db.selector(Message.class).limit(10).offset(offset).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if(dataList == null){
            dataList = new ArrayList<>();
        }
        return dataList;
    }

    public void updateMessage(Message msg){
        KeyValue keyValue = new KeyValue("isvisited",msg.isvisited());
        try {
            db.update(Message.class, WhereBuilder.b("id","=",msg.getId()),keyValue);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void addMessages(List<Message> messages){
        try {
            db.saveOrUpdate(messages);
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
