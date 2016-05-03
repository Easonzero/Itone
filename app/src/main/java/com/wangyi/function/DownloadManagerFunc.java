package com.wangyi.function;

import android.content.Context;
import com.wangyi.UIview.widget.DownloadViewHolder;
import com.wangyi.define.DownloadInfo;
import com.wangyi.define.EventName;
import com.wangyi.function.funchelp.Function;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.common.task.PriorityExecutor;
import org.xutils.common.util.LogUtil;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;
import com.wangyi.function.funchelp.DownloadCallback;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

/**
 * Created by maxchanglove on 2016/2/24.
 */
public class DownloadManagerFunc implements Function {
    private static DownloadManagerFunc instance;

    private final static int MAX_DOWNLOAD_THREAD = 2; // 有效的值范围[1, 3], 设置为3时, 可能阻塞图片加载.

    private final DbManager db;
    private final Executor executor = new PriorityExecutor(MAX_DOWNLOAD_THREAD, true);
    private final List<DownloadInfo> downloadInfoList = new ArrayList<DownloadInfo>();
    private final ConcurrentHashMap<DownloadInfo, DownloadCallback>
            callbackMap = new ConcurrentHashMap<DownloadInfo, DownloadCallback>(5);

    private DownloadManagerFunc() {
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                .setDbName("download")
                .setDbVersion(1);
        db = x.getDb(daoConfig);
        try {
            List<DownloadInfo> infoList = db.selector(DownloadInfo.class).findAll();
            if (infoList != null) {
                for (DownloadInfo info : infoList) {
                    if (info.getState() < EventName.Download.FINISHED) {
                        info.setState(EventName.Download.STOPPED);
                    }
                    downloadInfoList.add(info);
                }
            }
        } catch (DbException ex) {
            LogUtil.e(ex.getMessage(), ex);
        }
    }

    public static DownloadManagerFunc getInstance() {
        if (instance == null) {
            synchronized (DownloadManagerFunc.class) {
                if (instance == null) {
                    instance = new DownloadManagerFunc();
                }
            }
        }
        return instance;
    }

    @Override
    public void init(Context context) {

    }

    public void updateDownloadInfo(DownloadInfo info) throws DbException {
        db.update(info);
    }

    public int getDownloadListCount() {
        return downloadInfoList.size();
    }

    public DownloadInfo getDownloadInfo(int index) {
        return downloadInfoList.get(index);
    }

    public synchronized void startDownload(String url, String label, String savePath,
                                           boolean autoResume, boolean autoRename,
                                           DownloadViewHolder viewHolder) throws DbException {

        String fileSavePath = new File(savePath).getAbsolutePath();
        DownloadInfo downloadInfo = db.selector(DownloadInfo.class)
                .where("label", "=", label)
                .and("fileSavePath", "=", fileSavePath)
                .findFirst();
        if (downloadInfo != null) {
            DownloadCallback callback = callbackMap.get(downloadInfo);
            if (callback != null) {
                if (viewHolder == null) {
                    viewHolder = new DownloadViewHolder(null, downloadInfo);
                }
                if (callback.switchViewHolder(viewHolder)) {
                    return;
                } else {
                    callback.cancel();
                }
            }
        }

        // create download info
        if (downloadInfo == null) {
            downloadInfo = new DownloadInfo();
            downloadInfo.setUrl(url);
            downloadInfo.setAutoRename(autoRename);
            downloadInfo.setAutoResume(autoResume);
            downloadInfo.setLabel(label);
            downloadInfo.setFileSavePath(fileSavePath);
            db.saveBindingId(downloadInfo);
        }

        // start downloading
        if (viewHolder == null) {
            viewHolder = new DownloadViewHolder(null, downloadInfo);
        } else {
            viewHolder.update(downloadInfo);
        }
        DownloadCallback callback = new DownloadCallback(viewHolder);
        callback.setDownloadManager(this);
        callback.switchViewHolder(viewHolder);
        RequestParams params = new RequestParams(url);
        params.setAutoResume(downloadInfo.isAutoResume());
        params.setAutoRename(downloadInfo.isAutoRename());
        params.setSaveFilePath(downloadInfo.getFileSavePath());
        params.setExecutor(executor);
        params.setCancelFast(true);
        Callback.Cancelable cancelable = x.http().get(params, callback);
        callback.setCancelable(cancelable);
        callbackMap.put(downloadInfo, callback);

        if (downloadInfoList.contains(downloadInfo)) {
            int index = downloadInfoList.indexOf(downloadInfo);
            downloadInfoList.remove(downloadInfo);
            downloadInfoList.add(index, downloadInfo);
        } else {
            downloadInfoList.add(downloadInfo);
        }
    }

    public void stopDownload(int index) {
        DownloadInfo downloadInfo = downloadInfoList.get(index);
        stopDownload(downloadInfo);
    }

    public void stopDownload(DownloadInfo downloadInfo) {
        Callback.Cancelable cancelable = callbackMap.get(downloadInfo);
        if (cancelable != null) {
            cancelable.cancel();
        }
    }

    public void stopAllDownload() {
        for (DownloadInfo downloadInfo : downloadInfoList) {
            Callback.Cancelable cancelable = callbackMap.get(downloadInfo);
            if (cancelable != null) {
                cancelable.cancel();
            }
        }
    }

    public void removeDownload(int index) throws DbException {
        DownloadInfo downloadInfo = downloadInfoList.get(index);
        db.delete(downloadInfo);
        stopDownload(downloadInfo);
        downloadInfoList.remove(index);
    }

    public void finishDownload(int index){
        downloadInfoList.remove(index);
    }

    public void removeDownload(DownloadInfo downloadInfo) throws DbException {
        db.delete(downloadInfo);
        stopDownload(downloadInfo);
        downloadInfoList.remove(downloadInfo);
    }
}
