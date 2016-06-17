package com.wangyi.UIview.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.ItemTouchListenerAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.wangyi.UIview.BaseActivity;
import com.wangyi.UIview.adapter.TextAdapter;
import com.wangyi.UIview.widget.dialog.LoadingDialog;
import com.wangyi.UIview.widget.view.UltimateListView;
import com.wangyi.define.EventName;
import com.wangyi.function.HttpsFunc;
import com.wangyi.reader.R;
import com.wangyi.utils.ItOneUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Date;

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
    private UltimateRecyclerView list;

    private LoadingDialog loading;
    private TextAdapter adapter;
    private ArrayList<String> source;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case EventName.UI.FINISH:
                    loading.dismiss();
                    break;
                case EventName.UI.SUCCESS:
                    source = (ArrayList<String>) msg.obj;
                    adapter.removeAll();
                    adapter.insert(source);
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

        source = new ArrayList<>();

        String scategory = getIntent().getStringExtra("scategory");
        if(scategory.equals("university")){
            HttpsFunc.getInstance().connect(handler).getUniversityList();
            title.setText("选择学校");
            search.setHint("输入你的学校");
        }else if(scategory.equals("class")){
            String fromUniversity = getIntent().getStringExtra("fromUniversity");
            if(fromUniversity.equals("")||fromUniversity == null) fromUniversity = "*";
            HttpsFunc.getInstance().connect(handler).getClassList(fromUniversity);
            title.setText("选择班级");
            search.setHint("输入你的班级");
        }else if(scategory.equals("grade")){
            initGrade();
            title.setText("选择年级");
            search.setHint("输入你的年级");
        }

        adapter = new TextAdapter(source);

        initInfo();
        initSearch();
    }

    private void initSearch(){
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
                    String param = search.getText().toString();
                    ArrayList<String> info = ItOneUtils.search(source,param);
                    adapter.removeAll();
                    adapter.insert(info);
                    return true;
                }
                return false;
            }
        });
    }

    private void initInfo(){
        UltimateListView view = new UltimateListView(list,adapter,this);
        view.beforeFuncset();
        view.enableItemClick(new ItemTouchListenerAdapter(list.mRecyclerView,
                new ItemTouchListenerAdapter.RecyclerViewOnItemClickListener() {
                    @Override
                    public void onItemClick(RecyclerView parent, View clickedView, int position) {
                        Intent intent = new Intent();
                        intent.putExtra("result",adapter.getItem(position));
                        SearchInfoActivity.this.setResult(Activity.RESULT_OK,intent);
                        SearchInfoActivity.this.finish();
                    }

                    @Override
                    public void onItemLongClick(RecyclerView recyclerView, View view, int i) {}
                }));
        view.afterFuncset();
    }

    @Event(R.id.back)
    private void onBackClick(View view){
        this.finish();
    }

    private void initGrade(){
        int year = new Date().getYear()+1900;
        for(int i=0;i<6;i++){
            source.add((year-i)+"级");
        }
    }
}
