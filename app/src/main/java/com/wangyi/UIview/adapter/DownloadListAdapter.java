package com.wangyi.UIview.adapter;

import android.widget.*;
import com.wangyi.UIview.adapter.viewholder.DownloadItemVH;
import com.wangyi.define.bean.DownloadInfo;
import com.wangyi.define.EventName;
import com.wangyi.function.DownloadManagerFunc;
import com.wangyi.utils.ItOneUtils;
import org.xutils.ex.DbException;
import org.xutils.x;
import com.wangyi.reader.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DownloadListAdapter extends BaseAdapter {
    private boolean selectState = false;
	private LayoutInflater inflater;
    private DownloadManagerFunc downloadManager;
    private Context contxt;
    private int listSize;
	
	public DownloadListAdapter(Context context){
        downloadManager = DownloadManagerFunc.getInstance();
		inflater = LayoutInflater.from(context);
        if (downloadManager == null) listSize =  0;
        else listSize = downloadManager.getDownloadListCount();
        this.contxt = context;
	}
	
	public void allSelect(){
        for(int i = 0;i < listSize;i++){
            downloadManager.getDownloadInfo(i).setSelect(true);
        }
	}

    public void clearSelect(){
        for(int i = 0;i < listSize;i++){
            downloadManager.getDownloadInfo(i).setSelect(false);
        }
    }

    public void delect(){
        try {
            for(int pos=0;pos<listSize;pos++){
                if(downloadManager.getDownloadInfo(pos).getSelect()){
                    downloadManager.removeDownload(getItem(pos));
                }
            }
            if (downloadManager == null) listSize =  0;
            else listSize = downloadManager.getDownloadListCount();
        } catch (DbException e) {
        }
    }

    public void setEditMode(){
        selectState = !selectState;
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
        DownloadItemVH holder = null;
        DownloadInfo downloadInfo = downloadManager.getDownloadInfo(pos);
		if (null == view){
        	view = inflater.inflate(R.layout.download_ing_listitem,parent,false);
            holder = new DownloadItemVH(view,downloadInfo,downloadManager);
            view.setTag(holder);
            holder.refresh();
        }
		else{
			holder = (DownloadItemVH)view.getTag();
            holder.update(downloadInfo);
		}
        if (downloadInfo.getState() < EventName.Download.FINISHED) {
            try {
                downloadManager.startDownload(
                        downloadInfo.getUrl(),
                        ""+downloadInfo.getId(),
                        downloadInfo.getUid(),
                        downloadInfo.getLabel(),
                        downloadInfo.getFileSavePath(),
                        downloadInfo.isAutoResume(),
                        downloadInfo.isAutoRename(),
                        holder);
            } catch (DbException ex) {
                ItOneUtils.showToast(x.app(),"下载失败");
            }
        }
		
		if(selectState){
            holder.stopBtn.setVisibility(View.GONE);
			holder.selectBtn.setVisibility(View.VISIBLE);
		}else{
            holder.selectBtn.setVisibility(View.GONE);
            holder.stopBtn.setVisibility(View.VISIBLE);
        }
		return view;
	}
}
