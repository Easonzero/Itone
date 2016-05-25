package com.wangyi.UIview.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import com.wangyi.UIview.BaseFragment;
import com.wangyi.reader.R;
import com.wangyi.utils.ItOneUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by eason on 5/24/16.
 */
@ContentView(R.layout.fragment_register1)
public class RegisterFragment1 extends BaseFragment {
    @ViewInject(R.id.checkBox)
    private CheckBox checkBox;
    @ViewInject(R.id.tel)
    private EditText tel;

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        SpannableString str = new SpannableString("我同意 '服务条框' 和 '隐私保护和个人信息利用政策'");
        str.setSpan(new ForegroundColorSpan(Color.parseColor("#69B2F0")), 4, 10,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        str.setSpan(new ForegroundColorSpan(Color.parseColor("#69B2F0")), 13, 27,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        checkBox.append("\n");
        checkBox.append(str);
    }


    @Event(R.id.commit)
    private void onCommitClick(View view){
        if(checkBox.isChecked()){
            String str = tel.getText().toString();
            if(str.length() != 11) ItOneUtils.showToast(getContext(),"请输入正确的手机号");
            else{
                sendMessage(1,str);
            }
        }
    }
}
