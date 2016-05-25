package com.wangyi.UIview.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.marshalchen.ultimaterecyclerview.ItemTouchListenerAdapter;
import com.marshalchen.ultimaterecyclerview.ObservableScrollState;
import com.marshalchen.ultimaterecyclerview.ObservableScrollViewCallbacks;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.layoutmanagers.ScrollSmoothLineaerLayoutManager;
import com.wangyi.UIview.activity.SearchActivity;
import com.wangyi.UIview.activity.WatchBook;
import com.wangyi.UIview.widget.dialog.LoadingDialog;
import com.wangyi.define.BookData;

import org.xutils.view.annotation.*;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.wangyi.UIview.BaseFragment;
import com.wangyi.UIview.adapter.SearchBookAdapter;
import com.wangyi.UIview.widget.container.LessonListLayout;
import com.wangyi.define.EventName;
import com.wangyi.define.UserInfo;
import com.wangyi.function.BookManagerFunc;
import com.wangyi.function.HttpsFunc;
import com.wangyi.function.UserManagerFunc;
import com.wangyi.reader.R;
import org.xutils.x;

import java.util.ArrayList;

@ContentView(R.layout.fragment_allsubject)
public class AllSubjectFragment extends BaseFragment {
	public String[] keywords = {"工科数学","大学物理","大学英语","俄语","离散数学",
			"西方建筑史","茶文化欣赏","音乐鉴赏","思修","马哲","形式与政治",
			"交流技巧","数字逻辑","matlab","博弈论","西方文学","欧洲史",
			"西方美术史","人机交互"};
	@ViewInject(R.id.lessons)
	private LessonListLayout lessons;
	@ViewInject(R.id.booklist)
	private UltimateRecyclerView bookList;
    @ViewInject(R.id.measure)
    private RelativeLayout measure;
	SearchBookAdapter adapter;
    LoadingDialog loading;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case EventName.UI.FINISH:
					loading.dismiss();
					break;
				case EventName.UI.SUCCESS:
					ArrayList<BookData> books = (ArrayList<BookData>) msg.obj;
					adapter.insert(books);
					break;
				case EventName.UI.START:
					loading.show();
					break;
			}
		}

	};

	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
        loading = new LoadingDialog(x.app());

		lessons.addLessons(keywords,new OnClickListener(){

			@Override
			public void onClick(final View view) {
				// TODO Auto-generated method stub
				if(lessons.obj != null){
					((TextView)lessons.obj).setBackground(null);
				}
				view.setBackgroundResource(R.drawable.bg_subjecttab);
				lessons.obj = view;
				String subject = ((TextView)view).getText().toString();
				UserInfo user = UserManagerFunc.getInstance().getUserInfo();
				if(user != null)
					HttpsFunc.getInstance().connect(handler).searchBooksBySubject(subject,user.university,0);
			}

		});

		adapter = new SearchBookAdapter(new ArrayList<BookData>());
        bookList.setLayoutManager(new ScrollSmoothLineaerLayoutManager(
                getActivity().getBaseContext(), LinearLayoutManager.VERTICAL, false, 300));
		bookList.setAdapter(adapter);
        bookList.setEmptyView(R.layout.emptylist,UltimateRecyclerView.EMPTY_SHOW_LOADMORE_ONLY);
        adapter.setCustomLoadMoreView(createListBottomView(this.getContext()));
        bookList.setScrollViewCallbacks(new ObservableScrollViewCallbacks(){
            @Override
            public void onScrollChanged(int i, boolean b, boolean b1) {
            }

            @Override
            public void onDownMotionEvent() {
            }

            @Override
            public void onUpOrCancelMotionEvent(ObservableScrollState observableScrollState) {
                if (observableScrollState == ObservableScrollState.UP) {
                    bookList.hideView(lessons,bookList,getWindowHeight());
                }else if (observableScrollState == ObservableScrollState.DOWN) {
                    bookList.showView(lessons,bookList,getWindowHeight());
                }
            }
        });

        ItemTouchListenerAdapter itemTouchListenerAdapter = new ItemTouchListenerAdapter(bookList.mRecyclerView,
                new ItemTouchListenerAdapter.RecyclerViewOnItemClickListener() {
                    @Override
                    public void onItemClick(RecyclerView parent, View clickedView, int position) {
						Intent intent = new Intent(AllSubjectFragment.this.getActivity().getApplicationContext(),WatchBook.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable("book", BookManagerFunc.getInstance().getBookData(position));
						intent.putExtras(bundle);
						startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(RecyclerView recyclerView, View view, int i) {}
                });
        bookList.mRecyclerView.addOnItemTouchListener(itemTouchListenerAdapter);
    }

    private int getWindowHeight(){
        return measure.getHeight();
    }

    private View createListBottomView(Context context){
        View view = LayoutInflater.from(context)
                .inflate(R.layout.list_item_text, null);
        ((TextView)view.findViewById(R.id.tv_list_item)).setText("加载更多...");
        return view;
    }

	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		if(!hidden){
			UserInfo user = UserManagerFunc.getInstance().getUserInfo();
			if(user != null)
				HttpsFunc.getInstance().connect(handler).searchBooksBySubject("全部",user.university,0);
		}else{
			BookManagerFunc.getInstance().connect(handler).clear();
		}
	}

    @Event(value=R.id.booklist,type=UltimateRecyclerView.OnLoadMoreListener.class)
    private void loadMore(int itemsCount, final int maxLastVisiblePosition){
        UserInfo user = UserManagerFunc.getInstance().getUserInfo();
        if(user != null){
            HttpsFunc.getInstance().connect(handler).searchBooksBySubject(
                        ((TextView)lessons.obj).getText().toString(),
                        user.university,
                        itemsCount);
            bookList.disableLoadmore();
        }
    }

	@Event(R.id.search)
	private void onSearchClick(View view){
        Intent intent = new Intent(x.app(), SearchActivity.class);
        startActivity(intent);
	}
}
