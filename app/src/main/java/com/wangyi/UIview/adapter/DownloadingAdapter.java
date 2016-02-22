package com.wangyi.UIview.adapter;

import java.util.ArrayList;

import org.xutils.x;

import com.wangyi.define.BookData;
import com.wangyi.reader.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

public class DownloadingAdapter extends BaseAdapter {
	ArrayList<BookData> books;
	public ArrayList<SelectInfo> selectInfo;
	LayoutInflater inflater;
	public boolean state;
	
	public class SelectInfo{
		public boolean select;
		public boolean download;
	}
	
	public DownloadingAdapter(Context context,ArrayList<BookData> books){
		this.books = books;
		inflater = LayoutInflater.from(context);
		selectInfo = new ArrayList<SelectInfo>();
		for(int i = 0;i < books.size();i++){
			SelectInfo info = new SelectInfo();
			info.download = false;
			info.select = false;
			selectInfo.add(i, info);
		}
	}
	
	private class ViewHolder{
		private TextView bookName;
		private TextView count;
		private Button select;
		private Button download;
	}
	
	public void setState(boolean state){
		this.state = state;
	}
	
	public void allSelect(boolean is){
		for(SelectInfo info : selectInfo){
			info.select = is;
		}
	}
	
	public void setSelect(int pos,boolean is){
		SelectInfo info = new SelectInfo();
		info.select = is;
		info.download = selectInfo.get(pos).download;
		selectInfo.remove(pos);
		selectInfo.add(pos, info);
	}
	
	public void setDownload(int pos,boolean is){
		
	}
	
	public void changeDate(int pos,long count,long fileLength){
	}
	
	public void removeDate(int pos){
		books.remove(pos);
		selectInfo.remove(pos);
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
        	view = inflater.inflate(R.layout.download_ing_listitem,parent,false);
            holder = new ViewHolder();
            holder.bookName = (TextView) view.findViewById(R.id.book_name);
            holder.count = (TextView) view.findViewById(R.id.count);
            holder.select = (Button) view.findViewById(R.id.button1);
            holder.download = (Button) view.findViewById(R.id.button2);
            view.setTag(holder);
        }
		else{
			holder = (ViewHolder)view.getTag();
		}
		holder.bookName.setText(books.get(pos).bookName);
		
		if(state){
			holder.select.setVisibility(View.VISIBLE);
			if(selectInfo.get(pos).select){
				holder.select.setBackgroundResource(R.drawable.download_select);
			}
			else{
				holder.select.setBackgroundResource(R.drawable.download_unselect);
			}
		}
		else{
			holder.select.setVisibility(View.GONE);
			if(selectInfo.get(pos).download){
				holder.download.setBackgroundResource(R.drawable.download_ing);
			}
			else{
				holder.download.setBackgroundResource(R.drawable.download_stop);
			}
		}
		return view;
	}

}
