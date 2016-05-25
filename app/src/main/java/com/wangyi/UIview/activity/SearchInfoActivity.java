package com.wangyi.UIview.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.wangyi.UIview.BaseActivity;
import com.wangyi.UIview.widget.dialog.LoadingDialog;
import com.wangyi.define.EventName;
import com.wangyi.function.HttpsFunc;
import com.wangyi.reader.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * Created by eason on 5/21/16.
 */
@ContentView(R.layout.search_set)
public class SearchInfoActivity extends BaseActivity{
    @ViewInject(R.id.title)
    private TextView title;
    @ViewInject(R.id.search)
    private EditText search;
    @ViewInject(R.id.info)
    private UltimateRecyclerView info;

    private LoadingDialog loading;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case EventName.UI.FINISH:
                    loading.dismiss();
                    break;
                case EventName.UI.SUCCESS:
                    ArrayList<String> bs = (ArrayList<String>) msg.obj;
                    break;
                case EventName.UI.START:
                    loading.show();
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loading = new LoadingDialog(this);

        title.setText(getIntent().getStringExtra("title"));

        search.setMaxEms(20);
        search.setImeOptions(EditorInfo.IME_ACTION_DONE);

        search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER){
                    InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if(imm.isActive()){
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0 );
                    }

                    return true;
                }
                return false;
            }
        });
    }
}
