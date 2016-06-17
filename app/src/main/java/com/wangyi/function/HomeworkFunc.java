package com.wangyi.function;

import android.content.Context;
import android.os.Handler;
import com.wangyi.define.bean.Homework;
import com.wangyi.function.funchelp.Function;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eason on 6/11/16.
 */
public class HomeworkFunc implements Function {
    private static final HomeworkFunc INSTANCE = new HomeworkFunc();

    private HomeworkFunc(){}

    public static HomeworkFunc getInstance(){
        return INSTANCE;
    }

    private Handler handler;

    private DbManager db;

    @Override
    public void init(Context context) {
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                .setDbName("homework")
                .setDbVersion(1);
        db = x.getDb(daoConfig);
    }

    public void visitRomoteHomework(){
        if(handler!=null)
            HttpsFunc.getInstance().connect(handler).getHomework();
    }

    public List<Homework> getHomework(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new java.util.Date());
        List<Homework> dataList = new ArrayList<>();
        try {
            dataList = db.selector(Homework.class).where("sdate","<",date).and("fdate",">",date).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if(dataList == null){
            dataList = new ArrayList<>();
        }
        return dataList;
    }

    public void addHomeworks(List<Homework> homeworks){
        try {
            db.saveOrUpdate(homeworks);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public HomeworkFunc connect(Handler handler){
        this.handler = handler;
        return INSTANCE;
    }

    public void disconnect(){
        this.handler = null;
    }
}
