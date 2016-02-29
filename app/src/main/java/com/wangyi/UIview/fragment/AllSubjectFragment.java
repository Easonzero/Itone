package com.wangyi.UIview.fragment;

import com.wangyi.UIview.widget.LoadingDialog;
import com.wangyi.define.BookData;
import org.xutils.ex.DbException;
import org.xutils.x;
import org.xutils.view.annotation.*;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.wangyi.UIview.BaseFragment;
import com.wangyi.UIview.adapter.DLBookListAdapter;
import com.wangyi.UIview.widget.KeywordsFlow;
import com.wangyi.UIview.widget.LessonListLayout;
import com.wangyi.define.EventName;
import com.wangyi.define.UserInfo;
import com.wangyi.function.BookManagerFunc;
import com.wangyi.function.HttpsFunc;
import com.wangyi.function.SensorFunc;
import com.wangyi.function.UserManagerFunc;
import com.wangyi.reader.R;

@ContentView(R.layout.fragment_allsubject)
public class AllSubjectFragment extends BaseFragment {
	public String[] keywords = {"工科数学","大学物理","大学英语","俄语","离散数学",
			"西方建筑史","茶文化欣赏","音乐鉴赏","思修","马哲","形式与政治",
			"交流技巧","数字逻辑","matlab","博弈论","西方文学","欧洲史",
			"西方美术史","人机交互"};

	@ViewInject(R.id.keywordsflow)
	private KeywordsFlow keywordsFlow;
	@ViewInject(R.id.search_edit)
	private EditText editSearch;
	@ViewInject(R.id.lessons)
	private LessonListLayout lessons;
	@ViewInject(R.id.booklist)
	private ListView bookList;
	DLBookListAdapter adapter;
    LoadingDialog loading;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case EventName.UI.FINISH:
					if(msg.obj.equals(EventName.SensorFunc.SENSOR)){
						keywordsFlow.showKeywordsFlow(keywords);
					}else{
						loading.dismiss();
					}
					break;
				case EventName.UI.SUCCESS:
					adapter.notifyDataSetChanged();
					break;
				case EventName.UI.START:
					loading.show();
					break;
			}
		}

	};

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
        loading = new LoadingDialog(this.getContext());
		editSearch.addTextChangedListener( new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(s.toString().equals("")&&start - before != 0){
					keywordsFlow.showKeywordsFlow(keywords);
					SensorFunc.getInstance().connect(handler).registerListener();
					HttpsFunc.getInstance().connect(handler).searchBookByName(s.toString(),UserManagerFunc.getInstance().getUserInfo().university);
				}
				else{
					keywordsFlow.hideKeywordsFlow();
					SensorFunc.getInstance().connect(handler).unregisterListener();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(final Editable s) {

			}
		});

		lessons.addLessons(keywords,new OnClickListener(){

			@Override
			public void onClick(final View view) {
				// TODO Auto-generated method stub
				if(lessons.obj != null){
					((TextView)lessons.obj).setBackground(null);
					((TextView)lessons.obj).setPadding(0, 0, 0, 0);
				}
				view.setBackgroundResource(R.drawable.bg_class);
				((TextView)view).setPadding(0, 0, 0, 0);
				lessons.obj = view;
				String subject = ((TextView)view).getText().toString();
				UserInfo user = UserManagerFunc.getInstance().getUserInfo();
				if(user != null)
					HttpsFunc.getInstance().connect(handler).searchBooksBySubject(subject,user.university);
			}

		});

		keywordsFlow.setOnItemClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				UserInfo user = UserManagerFunc.getInstance().getUserInfo();
				if(user != null)
					HttpsFunc.getInstance().connect(handler).searchBooksBySubject(((TextView)view).getText().toString(),user.university);
			}
		});

		adapter = new DLBookListAdapter(this.getActivity().getBaseContext());
		bookList.setAdapter(adapter);
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		if(!hidden){
			UserInfo user = UserManagerFunc.getInstance().getUserInfo();
			if(user != null)
				HttpsFunc.getInstance().connect(handler).searchBooksBySubject("全部",user.university);
		}else{
			BookManagerFunc.getInstance().connect(handler).clear();
			keywordsFlow.hideKeywordsFlow();
			SensorFunc.getInstance().connect(handler).unregisterListener();
		}
	}

	@Event(value=R.id.booklist,type=ListView.OnItemClickListener.class)
	private void onListItemClick(AdapterView<?> adapter, View view, int pos,long arg3){
		try {
			BookData book = BookManagerFunc.getInstance().getBookData(pos);
			HttpsFunc.getInstance().connect(handler).download(book.url,book.bookName);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	@Event(R.id.keywordsflow)
	private void onKeywordsClick(View view){
		editSearch.clearFocus();
	}

	@Event(value=R.id.search_edit,type=View.OnFocusChangeListener.class)
	private void onEditSearchFocusChange(View view, boolean is){
		if(is){
			keywordsFlow.showKeywordsFlow(keywords);
			SensorFunc.getInstance().connect(handler).registerListener();
		}
		else{
			keywordsFlow.hideKeywordsFlow();
			SensorFunc.getInstance().connect(handler).unregisterListener();
			editSearch.setText("");
		}
	}
}
