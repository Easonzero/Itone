package com.wangyi.UIview.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsMessage;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wangyi.UIview.BaseFragment;
import com.wangyi.define.EventName;
import com.wangyi.function.HttpsFunc;
import com.wangyi.reader.R;
import com.wangyi.utils.ItOneUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by eason on 5/24/16.
 */
@ContentView(R.layout.fragment_register2)
public class RegisterFragment2 extends BaseFragment {
    @ViewInject(R.id.tel)
    private TextView tel;
    @ViewInject(R.id.checknum)
    private EditText checknum;
    @ViewInject(R.id.info)
    private  TextView info;

    private int time = 60;
    private String id;

    private String fuck = "none";

    private BroadcastReceiver myReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            Bundle bundle = intent.getExtras();
            SmsMessage msg = null;
            if (null != bundle) {
                Object[] smsObj = (Object[]) bundle.get("pdus");
                for (Object object : smsObj) {
                    msg = SmsMessage.createFromPdu((byte[]) object);
                    String content = msg.getDisplayMessageBody();
                    if (content.contains("【")) {
                        //TODO
                        fuck = content.substring(content.indexOf("：")+1,content.indexOf("，"));
                    }
                }
            }
        }

    };

    @Override
    public void onHiddenChanged(boolean hidden) {
        if(!hidden){
            fuck = "none";
            id = getMessage();
            tel.setText("您的手机号："+id);
            run.run();
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.provider.Telephony.SMS_RECEIVED");
            getActivity().registerReceiver(myReceiver, filter);
        }else{
            time = 60;
            handler.removeCallbacks(run);
            try {

                getActivity().unregisterReceiver(myReceiver);
            } catch (IllegalArgumentException e) {
                if (!e.getMessage().contains("Receiver not registered")) {
                    throw e;
                }
            }
        }
    }

    private Handler handler = new Handler();
    private Runnable run = new Runnable(){

        @Override
        public void run() {
            if(time == 0){
                info.setText("重新发送");
                info.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        time = 60;
                        info.setOnClickListener(null);
                        HttpsFunc.getInstance().connect(handler).SMS(id);
                        run.run();
                    }
                });
            }else{
                time--;
                SpannableString str = new SpannableString(time+"秒后重新发送");
                str.setSpan(new ForegroundColorSpan(Color.parseColor("#69B2F0")),0, (time+"").length(),
                        Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                info.setText(str);
                handler.postDelayed(this,1000);
            }
        }
    };

    @Event(R.id.commit)
    private void onCommitClick(View view){
        sendMessage(2+3,id);
    }

    public boolean check(String ckn){
        return fuck.equals(checknum.getText().toString());
    }
}
