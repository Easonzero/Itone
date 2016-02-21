package com.wangyi.UIview.adapter;

import java.util.ArrayList;

import com.wangyi.define.BookData;
import com.wangyi.reader.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DLBookListAdapter extends BaseAdapter {
	Context context;
	ArrayList<BookData> books;
	LayoutInflater inflater;
	public DLBookListAdapter(Context context ,ArrayList<BookData> books){
		this.books = books;
		this.context = context;
		inflater = LayoutInflater.from(context);
	}
	
	private class ViewHolder{
		private TextView bookName;
		private TextView bookSubject;
		private TextView downloadNUM;
		private TextView bookNameInPic;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return books.size();
	}

	@Override
	public BookData getItem(int pos) {
		// TODO Auto-generated method stub
		return books.get(pos);
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
            holder.bookName = (TextView) view.findViewById(R.id.title);
            holder.bookNameInPic = (TextView) view.findViewById(R.id.item_icontitle);
            holder.bookSubject = (TextView) view.findViewById(R.id.subject);
            holder.downloadNUM = (TextView) view.findViewById(R.id.downloadNUM);
            view.setTag(holder);
        }
		else{
			holder = (ViewHolder)view.getTag();
		}
		holder.bookName.setText(books.get(pos).bookName);
		holder.bookNameInPic.setText(books.get(pos).bookName);
		holder.bookSubject.setText("科目:"+books.get(pos).subject);
		holder.downloadNUM.setText("下载量:"+books.get(pos).downloadNumber);
		return view;
	}

}
