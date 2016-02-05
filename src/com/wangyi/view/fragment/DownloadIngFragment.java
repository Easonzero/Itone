package com.wangyi.view.fragment;

import java.util.ArrayList;
import com.wangyi.adapter.DownloadingAdapter;
import com.wangyi.define.BookData;
import com.wangyi.utils.PreferencesItOne;
import com.wangyi.reader.R;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class DownloadIngFragment extends Fragment{
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}
	
	private ArrayList<BookData> list;
	private ListView download_list;
	private DownloadingAdapter download_date;
	private Activity act;
	private int[] stateList;
    private Context context;
    private int SELECT = 0;
    private int DOWNLOAD = 0;
    private int allselect = 0;

	private Handler handler = new Handler(){
		public void dispatchMessage(Message msg){
			Intent intent = (Intent) msg.obj;
			String name = intent.getStringExtra("name");
			long count = intent.getLongExtra("count", 0);
			long fileLength = intent.getLongExtra("fileLength", 0);
			int pos;
			for(pos = 0; pos < list.size();pos++){
				BookData item = list.get(pos);
				if(item.bookName.equals(name)){
					break;
				}
			}
			
			if(count == fileLength){
				download_date.removeDate(pos);
				PreferencesItOne.deleteDownloadList(act, pos);
			}
			else{
				download_date.changeDate(pos, count, fileLength);
			}
			
			download_date.notifyDataSetChanged();
		}
	};
	
	private class mBroadcastReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Message msg = new Message();
			msg.obj = intent;
			handler.sendMessage(msg);
		}
    
    };
    
    private mBroadcastReceiver mbr;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.download_ing, container, false);
		context = this.getActivity().getApplicationContext();
		act = this.getActivity();
		list = PreferencesItOne.getDownloadList(act);
		
		BookData book = new BookData();
		book.bookName = "工科数学";
		book.url = "xxx.xxxx.xxx";
		list.add(book);
		
		download_date = new DownloadingAdapter(context, list);
		download_list = (ListView)view.findViewById(R.id.download_ing_list);
		download_list.setAdapter(download_date);
		stateList = new int[list.size()];
		
		IntentFilter filter = new IntentFilter();
	    filter.addAction("download");
	    mbr = new mBroadcastReceiver();
	    context.registerReceiver(mbr, filter);
	    
	    download_list.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				if(download_date.state){
					if(download_date.selectInfo.get(pos).select){
						download_date.setSelect(pos, false);
						stateList[pos] = 0;
					}
					else{
						download_date.setSelect(pos, true);
						stateList[pos] = 1;
					}
				}
				else{
					if(download_date.selectInfo.get(pos).download){
						download_date.setDownload(pos, false);
					}
					else{
						download_date.setDownload(pos, true);
					}
				}
				download_date.notifyDataSetChanged();
			}
	    	
	    });
		
		return view;
	}
	
	public void edit(){
		download_date.setState(true);
		download_date.notifyDataSetChanged();
		for(int i = 0;i < stateList.length;i++){
			stateList[i] = 0;
		}
	}
	
	public void finish(){
		download_date.setState(false);
		download_date.notifyDataSetChanged();
	}
	
	public void allselect(){
		if(allselect == 1){
			download_date.allSelect(false);
			allselect = 0;
		}
		else if(allselect == 0){
			allselect = 1;
			download_date.allSelect(true);
		}
		download_date.notifyDataSetChanged();
		for(int i = 0;i < stateList.length;i++){
			stateList[i] = 1;
		}
	}
	
	public void delete(){
		for(int i = 0;i < stateList.length;i++){
			if(stateList[i] == 1){
				
			}
		}
	}
	
	@Override
	public void onStop() {
		context.unregisterReceiver(mbr);
		super.onStop();
	}
}
