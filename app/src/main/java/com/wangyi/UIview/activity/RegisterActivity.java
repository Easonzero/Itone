package com.wangyi.UIview.activity;

import org.xutils.view.annotation.*;
import org.xutils.x;
import com.wangyi.UIview.BaseFragment;
import com.wangyi.UIview.fragment.RegisterFragment2;
import com.wangyi.UIview.fragment.RegisterFragment4;
import com.wangyi.define.EventName;
import com.wangyi.reader.R;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

@ContentView(R.layout.register)
public class RegisterActivity extends AppCompatActivity {
    @ViewInject(R.id.title)
    private TextView title;
    @ViewInject(R.id.save)
    private Button save;
    private BaseFragment[] mFragments;

    private String ckn = "";

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what<=EventName.UI.SUCCESS){
                switch (msg.what){
                    case EventName.UI.FINISH:
                        RegisterActivity.this.finish();
                        break;
                    case EventName.UI.START:
                        break;
                    case EventName.UI.SUCCESS:
                        break;
                }
                return;
            }
            else if(msg.what>=7){
                int id = msg.what-7;
                switch (id){
                    case EventName.UI.SUCCESS:
                        ckn = (String)msg.obj;
                        break;
                }
                return;
            }
            int id = msg.what-3;
            if(id == 2){
                if(ckn==null||ckn.equals(""))
                    return;
                else if(!((RegisterFragment2)mFragments[1]).check(ckn))
                    return;
            }
            else if(id == 3) {
                save.setVisibility(View.VISIBLE);
            }
            getSupportFragmentManager().beginTransaction().hide(mFragments[0])
                    .hide(mFragments[1]).hide(mFragments[2]).hide(mFragments[3])
                    .show(mFragments[id]).commit();
            title.setText("("+(id+1)+"/4)"+mFragments[id].getTitle());
            clearAndSet(id,(String)msg.obj);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initFragment();
    }

    private void initFragment(){
        mFragments = new BaseFragment[4];
        mFragments[0] = (BaseFragment)getSupportFragmentManager().findFragmentById(R.id.fragment1);
        mFragments[1] = (BaseFragment)getSupportFragmentManager().findFragmentById(R.id.fragment2);
        mFragments[2] = (BaseFragment)getSupportFragmentManager().findFragmentById(R.id.fragment3);
        mFragments[3] = (BaseFragment)getSupportFragmentManager().findFragmentById(R.id.fragment4);

        mFragments[0].setTitle("输入手机号");
        mFragments[1].setTitle("提交验证码");
        mFragments[2].setTitle("设置密码");
        mFragments[3].setTitle("完善个人信息");

        handler.sendEmptyMessage(3);
    }

    private void clearAndSet(int i,String msg){
        if(i!=0){
            mFragments[i-1].disconnect();
        }
        mFragments[i].connect(handler);
        mFragments[i].setMessage(msg);
    }

    @Event(R.id.back)
    private void onBackClick(View view){
        this.finish();
    }

    @Event(R.id.save)
    private void onConfirmClick(View view){
        ((RegisterFragment4)mFragments[3]).register();
    }
}
