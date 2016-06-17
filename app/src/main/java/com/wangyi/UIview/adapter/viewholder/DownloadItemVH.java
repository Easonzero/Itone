package com.wangyi.UIview.adapter.viewholder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wangyi.UIview.adapter.template.DownloadViewHolder;
import com.wangyi.define.bean.DownloadInfo;
import com.wangyi.define.EventName;
import com.wangyi.function.DownloadManagerFunc;
import com.wangyi.reader.R;
import com.wangyi.utils.ItOneUtils;

import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;

/**
 * Created by eason on 5/11/16.
 */
public class DownloadItemVH extends DownloadViewHolder {
    @ViewInject(R.id.book_name)
    public TextView bookname;
    @ViewInject(R.id.count)
    public TextView progress;
    @ViewInject(R.id.select_state)
    public Button selectBtn;
    @ViewInject(R.id.download_state)
    public Button stopBtn;

    private DownloadManagerFunc downloadManager;

    public DownloadItemVH(View view, DownloadInfo downloadInfo, DownloadManagerFunc downloadManager) {
        super(view, downloadInfo);
        this.downloadManager = downloadManager;
        refresh();
    }

    @Event(R.id.select_state)
    private void selectEvent(View view){
        downloadInfo.setSelect();
        refresh();
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
                         ""+downloadInfo.getId(),
                            downloadInfo.getUid(),
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
        progress.setText(downloadInfo.getProgress()+"%");

        if(selectBtn.isShown()){
            if(downloadInfo.getSelect()){
                selectBtn.setBackgroundResource(R.drawable.download_select);
            }else{
                selectBtn.setBackgroundResource(R.drawable.download_unselect);
            }
        }

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
                stopBtn.setVisibility(View.GONE);
                break;
            default:
                stopBtn.setBackgroundResource(R.drawable.download_stop);
                break;
        }
    }
}