package com.wangyi.UIview.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.wangyi.UIview.adapter.WatchUserAdapter;
import com.wangyi.UIview.widget.dialog.LoadingDialog;
import com.wangyi.UIview.widget.view.UltimateListView;
import com.wangyi.define.bean.BookData;
import com.wangyi.define.EventName;
import com.wangyi.define.bean.UserInfo;
import com.wangyi.define.bean.UserPlus;
import com.wangyi.function.HttpsFunc;
import com.wangyi.function.UserManagerFunc;
import com.wangyi.reader.R;

import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by eason on 5/3/16.
 */
@ContentView(value= R.layout.watchuser)
public class WatchUser extends AppCompatActivity {
    @ViewInject(R.id.uploadlist)
    private UltimateRecyclerView uploadlist;
    @ViewInject(R.id.userName)
    private TextView name;
    @ViewInject(R.id.downloadNum)
    private TextView downloadNum;
    @ViewInject(R.id.money)
    private TextView money;
    @ViewInject(R.id.pic)
    private ImageView pic;
    @ViewInject(R.id.rank)
    private TextView rank;
    @ViewInject(R.id.info)
    private TextView info;

    private WatchUserAdapter adapter = null;
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
                    adapter.removeAll();
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
        x.view().inject(this);

        adapter = new WatchUserAdapter(this, new WatchUserAdapter.OnSubItemClickListener(){

            @Override
            public void onClick(BookData book) {
                Intent intent = new Intent(WatchUser.this, WatchBook.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("book", book);
                intent.putExtras(bundle);
                WatchUser.this.startActivity(intent);
            }
        });

        initExpList();

        HttpsFunc.getInstance().connect(handler).searchBookByUser();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initUserInfo();
    }

    private void initUserInfo(){
        UserInfo userInfo = UserManagerFunc.getInstance().getUserInfo();
        UserPlus userPlus = UserManagerFunc.getInstance().getUserPlus();
        name.setText(userInfo.userName);
        downloadNum.setText(""+userPlus.downloadNum);
        money.setText(""+userPlus.money);
        info.setText(userInfo.university+"/"+
                userInfo.faculty+"/"+
                userInfo.Class+"/"+
                userInfo.grade);
        int number = UserManagerFunc.getInstance().getRank();
        rank.setText((number+1)+(number<100?"":"\n以内"));
        ImageOptions options=new ImageOptions.Builder()
                .setLoadingDrawableId(R.drawable.headpic)
                .setFailureDrawableId(R.drawable.headpic)
                .setUseMemCache(true)
                .setCircular(true)
                .setIgnoreGif(false)
                .build();
        x.image().bind(
                pic, HttpsFunc.host +
                        userInfo.picture +
                        "headPic.jpg",options
        );
    }

    private void initExpList(){
        UltimateListView view = new UltimateListView(uploadlist,adapter,this);
        view.beforeFuncset();
        view.enableAnimator();
        view.afterFuncset();
    }

    @Event(R.id.modify)
    private void onModifyClick(View view){
        Intent intent = new Intent(this,UserInfoActivity.class);
        startActivity(intent);
    }

    @Event(R.id.back)
    private void onBackClick(View view){
        HttpsFunc.getInstance().disconnect();
        this.finish();
    }
}
