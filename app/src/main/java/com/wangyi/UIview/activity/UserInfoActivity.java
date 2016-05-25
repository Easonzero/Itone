package com.wangyi.UIview.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wangyi.UIview.BaseActivity;
import com.wangyi.define.UserInfo;
import com.wangyi.function.HttpsFunc;
import com.wangyi.function.UserManagerFunc;
import com.wangyi.reader.R;

import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by eason on 5/20/16.
 */
@ContentView(R.layout.activity_userinfo)
public class UserInfoActivity extends BaseActivity{
    @ViewInject(R.id.name)
    private EditText name;
    @ViewInject(R.id.university)
    private TextView university;
    @ViewInject(R.id.faculty)
    private TextView faculty;
    @ViewInject(R.id.grade)
    private TextView grade;
    @ViewInject(R.id.headpic)
    private ImageView headPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(UserManagerFunc.getInstance().isLogin()){
            UserInfo userInfo = UserManagerFunc.getInstance().getUserInfo();
            name.setClickable(false);
            name.setFocusable(false);
            name.setText(userInfo.userName);
            faculty.setText(userInfo.faculty);
            grade.setText(userInfo.grade);
            university.setText(userInfo.university);
            ImageOptions options=new ImageOptions.Builder()
                    .setLoadingDrawableId(R.drawable.ic_me)
                    .setFailureDrawableId(R.drawable.ic_me)
                    .setUseMemCache(true)
                    .setCircular(true)
                    .setIgnoreGif(false)
                    .build();
            x.image().bind(
                    headPic, HttpsFunc.host +
                            userInfo.picture +
                            "headPic.jpg",options
            );
        }
    }
    
    @Event(R.id.back)
    private void onBackClick(View view){
        this.finish();
    }
}
