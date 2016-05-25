package com.wangyi.UIview.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.ItemTouchListenerAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.wangyi.UIview.adapter.SearchBookAdapter;
import com.wangyi.UIview.widget.dialog.LoadingDialog;
import com.wangyi.UIview.widget.view.SearchView;
import com.wangyi.define.BookData;
import com.wangyi.define.EventName;
import com.wangyi.function.HttpsFunc;
import com.wangyi.reader.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by eason on 5/20/16.
 */

@ContentView(R.layout.search)
public class SearchActivity extends AppCompatActivity {
    @ViewInject(R.id.search)
    private SearchView search;
    @ViewInject(R.id.booklist)
    private UltimateRecyclerView bookList;
    @ViewInject(R.id.label)
    private TextView label;

    private SearchBookAdapter adapter;
    private LoadingDialog loading;
    private ArrayList<BookData> books;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case EventName.UI.FINISH:
                    loading.dismiss();
                    break;
                case EventName.UI.SUCCESS:
                    ArrayList<BookData> bs = (ArrayList<BookData>) msg.obj;
                    adapter.insert(bs);
                    label.setText("共找到"+books.size()+"个相关课程");
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
        x.view().inject(this);
        loading = new LoadingDialog(this);

        books = new ArrayList<BookData>();
        adapter = new SearchBookAdapter(books);
        bookList.setAdapter(adapter);

        ItemTouchListenerAdapter itemTouchListenerAdapter = new ItemTouchListenerAdapter(bookList.mRecyclerView,
                new ItemTouchListenerAdapter.RecyclerViewOnItemClickListener() {
                    @Override
                    public void onItemClick(RecyclerView parent, View clickedView, int position) {
                        Intent intent = new Intent(SearchActivity.this,WatchBook.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("book", books.get(position));
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(RecyclerView recyclerView, View view, int i) {}
                });
        bookList.mRecyclerView.addOnItemTouchListener(itemTouchListenerAdapter);

        search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER){
                    InputMethodManager imm = (InputMethodManager)x.app().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if(imm.isActive()){
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    }
                    HttpsFunc.getInstance().connect(handler).searchBookByName(search.getText());
                    return true;
                }
                return false;
            }
        });
    }

    @Event(R.id.cancel)
    private void onCancelClick(View view){
        this.finish();
    }
}
