package com.wangyi.UIview.fragment;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
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
@ContentView(R.layout.fragment_register3)
public class RegisterFragment3 extends BaseFragment {
    @ViewInject(R.id.edit_passwords)
    private EditText edit;

    @Event(R.id.bt_passwords)
    private void onButtonClick(View view){
        String tag = (String)view.getTag();
        if(tag.equals("show")){
            edit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            view.setTag("hide");
            ((Button)view).setText("隱藏");
        }else{
            edit.setTransformationMethod(PasswordTransformationMethod.getInstance());
            view.setTag("show");
            ((Button)view).setText("显示");
        }
    }

    @Event(R.id.commit)
    private void onCommitClick(View view){
        sendMessage(3+3, ItOneUtils.generateMessage(getMessage(),edit.getText().toString()));
    }

}
