package com.wangyi.UIview.adapter;

import java.util.ArrayList;

import org.xutils.x;

import com.wangyi.reader.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DownloadedAdapter extends BaseAdapter {
	Context context;
	ArrayList<String> bookName;
	LayoutInflater inflater;
	public DownloadedAdapter(Context context,ArrayList<String> bookName){
		this.bookName = bookName;
		this.context = context;
		inflater = LayoutInflater.from(x.app());
	}
	
	private class ViewHolder{
		private TextView bookName;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return bookName.size();
	}

	@Override
	public String getItem(int pos) {
		// TODO Auto-generated method stub
		return bookName.get(pos);
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
        	view = inflater.inflate(R.layout.download_ed_listitem,parent,false);
            holder = new ViewHolder();
            holder.bookName = (TextView) view.findViewById(R.id.book_name1);
            view.setTag(holder);
        }
		else{
			holder = (ViewHolder)view.getTag();
		}
		holder.bookName.setText(bookName.get(pos));
		return view;
	}

}
