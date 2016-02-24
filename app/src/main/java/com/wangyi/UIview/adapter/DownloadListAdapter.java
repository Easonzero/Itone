package com.wangyi.UIview.adapter;

import java.io.File;
import java.util.ArrayList;

import android.widget.*;
import com.wangyi.UIview.widget.DownloadViewHolder;
import com.wangyi.define.DownloadInfo;
import com.wangyi.define.EventName;
import com.wangyi.function.DownloadManagerFunc;
import com.wangyi.utils.ItOneUtils;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
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

public class DownloadListAdapter extends BaseAdapter {
    private int SELECT = 1;
    private int UNSELECT = 0;
	private int[] selectInfo;
    private int state;
	private LayoutInflater inflater;
    private DownloadManagerFunc downloadManager;
    private Context contxt;
    private int listSize;
	
	public DownloadListAdapter(Context context){
        downloadManager = DownloadManagerFunc.getInstance();
		inflater = LayoutInflater.from(context);
        if (downloadManager == null) listSize =  0;
        else listSize = downloadManager.getDownloadListCount();
        selectInfo = new int[listSize];
		for(int i = 0;i < listSize;i++){
			selectInfo[i] = UNSELECT;
		}
        this.contxt = context;
	}
	
	public void allSelect(){
        for(int i = 0;i < listSize;i++){
            selectInfo[i] = SELECT;
        }
	}

    public void delect(){
        try {
            for(int pos=0;pos<selectInfo.length;pos++){
                if(SELECT == selectInfo[pos]){
                    downloadManager.removeDownload(getItem(pos));
                }
            }
            this.notifyDataSetChanged();
        } catch (DbException e) {
        }
    }

    public void setEditMode(){
        state = ~state;
    }
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listSize;
	}

	@Override
	public DownloadInfo getItem(int pos) {
		// TODO Auto-generated method stub
		return downloadManager.getDownloadInfo(pos);
	}

	@Override
	public long getItemId(int pos) {
		// TODO Auto-generated method stub
		return pos;
	}

	@Override
	public View getView(int pos, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
        DownloadItemViewHolder holder = null;
        DownloadInfo downloadInfo = downloadManager.getDownloadInfo(pos);
		if (null == view){
        	view = inflater.inflate(R.layout.download_ing_listitem,parent,false);
            holder = new DownloadItemViewHolder(view,downloadInfo,pos);
            view.setTag(holder);
            holder.refresh();
        }
		else{
			holder = (DownloadItemViewHolder)view.getTag();
            holder.update(downloadInfo);
		}
        if (downloadInfo.getState() < EventName.Download.FINISHED) {
            try {
                downloadManager.startDownload(
                        downloadInfo.getUrl(),
                        downloadInfo.getLabel(),
                        downloadInfo.getFileSavePath(),
                        downloadInfo.isAutoResume(),
                        downloadInfo.isAutoRename(),
                        holder);
            } catch (DbException ex) {
                ItOneUtils.showToast(x.app(),"下载失败");
            }
        }
		
		if(state == SELECT){
			holder.selectBtn.setVisibility(View.VISIBLE);
			if(selectInfo[pos] == SELECT){
				holder.selectBtn.setBackgroundResource(R.drawable.download_select);
			}else{
				holder.selectBtn.setBackgroundResource(R.drawable.download_unselect);
			}
		}else{
            holder.stopBtn.setVisibility(View.VISIBLE);
        }
		return view;
	}

    public class DownloadItemViewHolder extends DownloadViewHolder {
        @ViewInject(R.id.book_name)
        TextView bookname;
        @ViewInject(R.id.count)
        TextView progress;
        @ViewInject(R.id.select_state)
        Button selectBtn;
        @ViewInject(R.id.download_state)
        Button stopBtn;

        private int pos;

        public DownloadItemViewHolder(View view, DownloadInfo downloadInfo,int pos) {
            super(view, downloadInfo);
            this.pos = pos;
            refresh();
        }

        @Event(R.id.select_state)
        private void selectEvent(View view){
            selectInfo[pos] = ~selectInfo[pos];
        }

        @Event(R.id.download_state)
        private void toggleEvent(View view) {
            int state = downloadInfo.getState();
            switch (state) {
                case EventName.Download.WAITING:
                case EventName.Download.STARTED:
                    downloadManager.stopDownload(downloadInfo);
                    break;
                case EventName.Download.ERROR:
                case EventName.Download.STOPPED:
                    try {
                        downloadManager.startDownload(
                                downloadInfo.getUrl(),
                                downloadInfo.getLabel(),
                                downloadInfo.getFileSavePath(),
                                downloadInfo.isAutoResume(),
                                downloadInfo.isAutoRename(),
                                this);
                    } catch (DbException ex) {
                        ItOneUtils.showToast(x.app(),downloadInfo.getLabel()+"开始下载失败");
                    }
                    break;
                case EventName.Download.FINISHED:
                    ItOneUtils.showToast(x.app(),downloadInfo.getLabel()+"下载完成");
                    break;
                default:
                    break;
            }
        }

        @Override
        public void update(DownloadInfo downloadInfo) {
            super.update(downloadInfo);
            refresh();
        }

        @Override
        public void onWaiting() {
            refresh();
        }

        @Override
        public void onStarted() {
            refresh();
        }

        @Override
        public void onLoading(long total, long current) {
            refresh();
        }

        @Override
        public void onSuccess(File result) {
            refresh();
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            refresh();
        }

        @Override
        public void onCancelled(Callback.CancelledException cex) {
            refresh();
        }

        public void refresh() {
            bookname.setText(downloadInfo.getLabel());
            progress.setText(downloadInfo.getProgress());

            stopBtn.setVisibility(View.VISIBLE);
            int state = downloadInfo.getState();
            switch (state) {
                case EventName.Download.WAITING:
                case EventName.Download.STARTED:
                    stopBtn.setBackgroundResource(R.drawable.download_ing);
                    break;
                case EventName.Download.ERROR:
                case EventName.Download.STOPPED:
                    stopBtn.setBackgroundResource(R.drawable.download_stop);
                    break;
                case EventName.Download.FINISHED:
                    stopBtn.setVisibility(View.INVISIBLE);
                    break;
                default:
                    stopBtn.setBackgroundResource(R.drawable.download_stop);
                    break;
            }
        }
    }

}
