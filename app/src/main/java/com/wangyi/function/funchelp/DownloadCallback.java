package com.wangyi.function.funchelp;

import com.wangyi.UIview.adapter.template.DownloadViewHolder;
import com.wangyi.define.bean.DownloadInfo;
import com.wangyi.define.EventName;
import com.wangyi.function.DownloadManagerFunc;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.ex.DbException;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * Created by maxchanglove on 2016/2/24.
 */
public class DownloadCallback implements
        Callback.CommonCallback<File>,
        Callback.ProgressCallback<File>,
        Callback.Cancelable {

    private DownloadInfo downloadInfo;
    private WeakReference<DownloadViewHolder> viewHolderRef;
    private DownloadManagerFunc downloadManager;
    private boolean cancelled = false;
    private Cancelable cancelable;

    public DownloadCallback(DownloadViewHolder viewHolder) {
        this.switchViewHolder(viewHolder);
    }

    public boolean switchViewHolder(DownloadViewHolder viewHolder) {
        if (viewHolder == null) return false;

        synchronized (DownloadCallback.class) {
            if (downloadInfo != null) {
                if (this.isStopped()) {
                    return false;
                }
            }
            this.downloadInfo = viewHolder.getDownloadInfo();
            this.viewHolderRef = new WeakReference<DownloadViewHolder>(viewHolder);
        }
        return true;
    }

    public void setDownloadManager(DownloadManagerFunc downloadManager) {
        this.downloadManager = downloadManager;
    }

    public void setCancelable(Cancelable cancelable) {
        this.cancelable = cancelable;
    }

    private DownloadViewHolder getViewHolder() {
        if (viewHolderRef == null) return null;
        DownloadViewHolder viewHolder = viewHolderRef.get();
        if (viewHolder != null) {
            DownloadInfo downloadInfo = viewHolder.getDownloadInfo();
            if (this.downloadInfo != null && this.downloadInfo.equals(downloadInfo)) {
                return viewHolder;
            }
        }
        return viewHolder;
    }

    @Override
    public void onWaiting() {
        try {
            downloadInfo.setState(EventName.Download.WAITING);
            downloadManager.updateDownloadInfo(downloadInfo);
        } catch (DbException ex) {
            LogUtil.e(ex.getMessage(), ex);
        }
        DownloadViewHolder viewHolder = this.getViewHolder();
        if (viewHolder != null) {
            viewHolder.onWaiting();
        }
    }

    @Override
    public void onStarted() {
        try {
            downloadInfo.setState(EventName.Download.STARTED);
            downloadManager.updateDownloadInfo(downloadInfo);
        } catch (DbException ex) {
            LogUtil.e(ex.getMessage(), ex);
        }
        DownloadViewHolder viewHolder = this.getViewHolder();
        if (viewHolder != null) {
            viewHolder.onStarted();
        }
    }

    @Override
    public void onLoading(long total, long current, boolean isDownloading) {
        if (isDownloading) {
            try {
                downloadInfo.setState(EventName.Download.STARTED);
                downloadInfo.setFileLength(total);
                downloadInfo.setProgress((int) (current * 100 / total));
                downloadManager.updateDownloadInfo(downloadInfo);
            } catch (DbException ex) {
                LogUtil.e(ex.getMessage(), ex);
            }
            DownloadViewHolder viewHolder = this.getViewHolder();
            if (viewHolder != null) {
                viewHolder.onLoading(total, current);
            }
        }
    }

    @Override
    public void onSuccess(File result) {
        synchronized (DownloadCallback.class) {
            try {
                downloadInfo.setState(EventName.Download.FINISHED);
                downloadManager.updateDownloadInfo(downloadInfo);
            } catch (DbException ex) {
                LogUtil.e(ex.getMessage(), ex);
            }
            DownloadViewHolder viewHolder = this.getViewHolder();
            if (viewHolder != null) {
                viewHolder.onSuccess(result);
            }
        }
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        synchronized (DownloadCallback.class) {
            try {
                downloadInfo.setState(EventName.Download.ERROR);
                downloadManager.updateDownloadInfo(downloadInfo);
            } catch (DbException e) {
                LogUtil.e(e.getMessage(), e);
            }
            DownloadViewHolder viewHolder = this.getViewHolder();
            if (viewHolder != null) {
                viewHolder.onError(ex, isOnCallback);
            }
        }
    }

    @Override
    public void onCancelled(CancelledException cex) {
        synchronized (DownloadCallback.class) {
            try {
                downloadInfo.setState(EventName.Download.STOPPED);
                downloadManager.updateDownloadInfo(downloadInfo);
            } catch (DbException ex) {
                LogUtil.e(ex.getMessage(), ex);
            }
            DownloadViewHolder viewHolder = this.getViewHolder();
            if (viewHolder != null) {
                viewHolder.onCancelled(cex);
            }
        }
    }

    @Override
    public void onFinished() {
        cancelled = false;
    }

    private boolean isStopped() {
        int state = downloadInfo.getState();
        return isCancelled() || state > EventName.Download.STARTED;
    }

    @Override
    public void cancel() {
        cancelled = true;
        if (cancelable != null) {
            cancelable.cancel();
        }
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }
}