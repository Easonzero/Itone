package com.wangyi.UIview.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.wangyi.UIview.BaseActivity;
import com.wangyi.UIview.adapter.WatchUserAdapter;
import com.wangyi.UIview.widget.LoadingDialog;
import com.wangyi.UIview.widget.NumberView;
import com.wangyi.define.BookData;
import com.wangyi.define.EventName;
import com.wangyi.function.HttpsFunc;
import com.wangyi.function.UserManagerFunc;
import com.wangyi.reader.R;

import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by eason on 5/3/16.
 */
@ContentView(value= R.layout.watchuser)
public class WatchUser extends BaseActivity {
    @ViewInject(R.id.uploadlist)
    private UltimateRecyclerView uploadlist;
    @ViewInject(R.id.name)
    private TextView name;
    @ViewInject(R.id.downloadnum)
    private TextView downloadnum;
    @ViewInject(R.id.from)
    private TextView from;
    @ViewInject(R.id.pic)
    private ImageView pic;
    @ViewInject(R.id.number)
    private NumberView number;

    private WatchUserAdapter adapter = null;
    private LinearLayoutManager linearLayoutManager;
    private LoadingDialog loading;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch(msg.what){
                case EventName.UI.START:
                    loading.show();
                    break;
                case EventName.UI.FAULT:
                    loading.dismiss();
                    break;
                case EventName.UI.SUCCESS:
                    ArrayList<BookData> books = (ArrayList<BookData>) msg.obj;
                    adapter.addAll(WatchUserAdapter.getPreCodeMenu(
                            books.toArray(new BookData[books.size()])), 0);
                    loading.dismiss();
                    break;
            }
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        loading = new LoadingDialog(this);
        initUserInfo();
        initExpList();
    }

    private void initUserInfo(){
        name.setText("王祎");//UserManagerFunc.getInstance().getUserInfo().userName);
        downloadnum.setText("总下载量：500");//UserManagerFunc.getInstance().getUserPlus().downloadNum);
        from.setText("哈尔滨工业大学");//UserManagerFunc.getInstance().getUserInfo().university);
        //int rank = UserManagerFunc.getInstance().getRank();
        number.setMessage("55");//rank+rank<100?"":"/n以内");
        ImageOptions options=new ImageOptions.Builder()
                .setLoadingDrawableId(R.drawable.ic_me)
                .setFailureDrawableId(R.drawable.ic_me)
                .setUseMemCache(true)
                .setCircular(true)
                .setIgnoreGif(true)
                .build();
        x.image().bind(
                pic, HttpsFunc.host +
                        "/res/user/ximin/" +
                        "headPic.jpg",options
        );
    }

    private void initExpList(){
        uploadlist.setHasFixedSize(false);
        adapter = new WatchUserAdapter(this);
        adapter.addAll(WatchUserAdapter.getPreCodeMenu(test()), 0);
        linearLayoutManager = new LinearLayoutManager(this);
        uploadlist.setLayoutManager(linearLayoutManager);
        uploadlist.setAdapter(adapter);
        addExpandableFeatures();
        //HttpsFunc.getInstance().connect(handler).searchBookByUser();
    }

    private BookData[] test(){
        int i=0;
        BookData[] books = new BookData[10];
        String category = "ppt";
        while(i<10){
            BookData book = new BookData();
            book.downloadNumber = 200;
            book.bookName = "工科数学";
            book.fromUniversity = "哈尔滨工业大学";
            book.occupation = "数学";
            book.uploader = "王祎";
            book.category = category;
            books[i] = book;
            if(i == 2){
                category = "note";
            }else if(i == 7){
                category = "review";
            }
            i++;
        }
        return books;
    }

    private void addExpandableFeatures() {
        uploadlist.getItemAnimator().setAddDuration(100);
        uploadlist.getItemAnimator().setRemoveDuration(100);
        uploadlist.getItemAnimator().setMoveDuration(200);
        uploadlist.getItemAnimator().setChangeDuration(100);
    }

    @Event(R.id.back)
    private void onBackClick(View view){
        HttpsFunc.getInstance().disconnect();
        this.finish();
    }
}
