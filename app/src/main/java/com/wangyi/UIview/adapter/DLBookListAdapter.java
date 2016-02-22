package com.wangyi.UIview.adapter;

import java.util.ArrayList;

import org.xutils.x;
import org.xutils.view.annotation.ViewInject;

import com.wangyi.define.BookData;
import com.wangyi.function.BookManagerFunc;
import com.wangyi.reader.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DLBookListAdapter extends BaseAdapter {
	LayoutInflater inflater;
	public DLBookListAdapter(Context context){
		inflater = LayoutInflater.from(context);
	}

	private class ViewHolder{
		@ViewInject(R.id.title)
		private TextView bookName;
		@ViewInject(R.id.subject)
		private TextView bookSubject;
		@ViewInject(R.id.downloadNUM)
		private TextView downloadNUM;
		@ViewInject(R.id.item_icontitle)
		private TextView bookNameInPic;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return BookManagerFunc.getInstance().getBooksNum();
	}

	@Override
	public BookData getItem(int pos) {
		// TODO Auto-generated method stub
		return BookManagerFunc.getInstance().getBookData(pos);
	}

	@Override
	public long getItemId(int pos) {
		// TODO Auto-generated method stub
		return pos;
	}

	@Override
	public View getView(int pos, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (null == view){
			view = inflater.inflate(R.layout.book_list_item,parent,false);
			holder = new ViewHolder();
			x.view().inject(holder,view);
			view.setTag(holder);
		}
		else{
			holder = (ViewHolder)view.getTag();
		}
		holder.bookName.setText(BookManagerFunc.getInstance().getBookData(pos).bookName);
		holder.bookNameInPic.setText(BookManagerFunc.getInstance().getBookData(pos).bookName);
		holder.bookSubject.setText("科目:"+BookManagerFunc.getInstance().getBookData(pos).subject);
		holder.downloadNUM.setText("下载量:"+BookManagerFunc.getInstance().getBookData(pos).downloadNumber);
		return view;
	}

}
