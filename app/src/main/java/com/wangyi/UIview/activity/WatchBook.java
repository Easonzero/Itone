package com.wangyi.UIview.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wangyi.UIview.BaseActivity;
import com.wangyi.define.bean.BookData;
import com.wangyi.define.SettingName;
import com.wangyi.function.BookManagerFunc;
import com.wangyi.function.HttpsFunc;
import com.wangyi.function.UserManagerFunc;
import com.wangyi.reader.R;
import com.wangyi.utils.ItOneUtils;

import org.xutils.ex.DbException;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;

/**
 * Created by eason on 5/3/16.
 */
@ContentView(value= R.layout.watchbooks)
public class WatchBook extends BaseActivity {
    @ViewInject(R.id.name)
    private TextView name;
    @ViewInject(R.id.upload)
    private TextView upload;
    @ViewInject(R.id.from)
    private TextView from;
    @ViewInject(R.id.download)
    private TextView download;
    @ViewInject(R.id.money)
    private TextView money;
    @ViewInject(R.id.pic)
    private ImageView pic;
    private BookData book;
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        book = (BookData)getIntent().getSerializableExtra("book");
        name.setText(book.bookName);
        download.setText("下载量："+book.downloadNumber);
        upload.setText("上传者："+book.uploader);
        from.setText("出版社："+book.fromUniversity);
        money.setText(book.money+"人民π");
        if(book.pic != null){
            ImageOptions options=new ImageOptions.Builder()
                    .setUseMemCache(true)
                    .setIgnoreGif(true)
                    .build();
            x.image().bind(
                    pic, HttpsFunc.host +
                            book.pic,options
            );
        }
    }

    @Event(R.id.downloadbt)
    private void onDownloadClick(View view){
        if(!UserManagerFunc.getInstance().isLogin()){
            ItOneUtils.showToast(x.app(),"请先登陆");
            return;
        }
        if(!UserManagerFunc.getInstance().getSetting(SettingName.NOWIFIDOWNLOAD)){
            if(!ItOneUtils.getWifiState(x.app())){
                ItOneUtils.showToast(x.app(),"已设置流量状态下不可下载图书");
                return;
            }
        }
        if(UserManagerFunc.getInstance().getUserPlus().money < book.money){
            ItOneUtils.showToast(x.app(),"剩余积分不足");
            return;
        }
        File file = new File(BookManagerFunc.FILEPATH + book.bookName+".pdf");
        if(file.exists()){
            ItOneUtils.showToast(x.app(),"已经下载该图书，将重新下载");
            file.delete();
        }
        try {
            ItOneUtils.showToast(x.app(),"开始下载...");
            HttpsFunc.getInstance().download(book.id+"",book.uid,book.bookName+".pdf");
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Event(R.id.correctbt)
    private void onCorrectClick(View view){
        Intent intent = new Intent(this,IdeaBackActivity.class);
        startActivity(intent);
    }

    @Event(R.id.back)
    private void onBackClick(View view){
        this.finish();
    }
}
