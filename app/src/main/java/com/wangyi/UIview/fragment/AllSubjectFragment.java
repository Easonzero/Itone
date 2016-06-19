package com.wangyi.UIview.fragment;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;
import com.marshalchen.ultimaterecyclerview.ItemTouchListenerAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.wangyi.UIview.activity.SearchActivity;
import com.wangyi.UIview.activity.WatchBook;
import com.wangyi.UIview.widget.view.UltimateListView;
import com.wangyi.define.bean.BookData;
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
import com.wangyi.define.bean.UserInfo;
import com.wangyi.function.BookManagerFunc;
import com.wangyi.function.HttpsFunc;
import com.wangyi.function.UserManagerFunc;
import com.wangyi.reader.R;
import com.wangyi.utils.ItOneUtils;

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

	private SearchBookAdapter adapter;

	protected int start = 0;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case EventName.UI.FINISH:
					break;
				case EventName.UI.SUCCESS:
					ArrayList<BookData> book = (ArrayList<BookData>) msg.obj;
					adapter.insert(book);
					if(book.size() != 0)
						start += book.size();
					else{
						ItOneUtils.showToast(getContext(), "已經加載全部資源");
						adapter.getCustomLoadMoreView().setVisibility(View.GONE);
					}
					break;
				case EventName.UI.START:
					break;
			}
		}

	};

	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);

		adapter = new SearchBookAdapter(new ArrayList<BookData>());

		initBookList();
    }

	private void initLesson(){
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
	}

	private void initBookList(){
		UltimateListView view = new UltimateListView(bookList,adapter,getContext());
		view.beforeFuncset();
		view.enableLoadmore(new UltimateRecyclerView.OnLoadMoreListener() {
			@Override
			public void loadMore(int itemsCount, final int maxLastVisiblePosition) {
				if(UserManagerFunc.getInstance().isLogin()){
					handler.postDelayed(new Runnable() {
						public void run() {
							UserInfo user = UserManagerFunc.getInstance().getUserInfo();
							HttpsFunc.getInstance().connect(handler).searchBooksBySubject("全部",user.university,start);
						}
					}, 500);
				}
			}
		});

		view.enableItemClick(
				new ItemTouchListenerAdapter(bookList.mRecyclerView,
				new ItemTouchListenerAdapter.RecyclerViewOnItemClickListener() {
					@Override
					public void onItemClick(RecyclerView parent, View clickedView, int position) {
						if(position > adapter.getItemCount()|| position < 0) return;
						Intent intent = new Intent(AllSubjectFragment.this.getActivity().getApplicationContext(),WatchBook.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable("book", adapter.getItemData(position));
						intent.putExtras(bundle);
						startActivity(intent);
					}

					@Override
					public void onItemLongClick(RecyclerView recyclerView, View view, int i) {}
				})
		);
		view.afterFuncset();
	}

    private int getWindowHeight(){
        return measure.getHeight();
    }

	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		if(!hidden){
			UserInfo user = UserManagerFunc.getInstance().getUserInfo();
			if(user != null&&adapter!=null&&adapter.isEmpty())
				HttpsFunc.getInstance().connect(handler).searchBooksBySubject("全部",user.university,start);
		}else{
			BookManagerFunc.getInstance().connect(handler).clear();
		}
	}

	@Event(R.id.search)
	private void onSearchClick(View view){
        Intent intent = new Intent(x.app(), SearchActivity.class);
        startActivity(intent);
	}
}
