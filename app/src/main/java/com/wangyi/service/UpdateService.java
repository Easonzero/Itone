package com.wangyi.service;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;

import com.wangyi.define.SettingName;
import com.wangyi.function.UserManagerFunc;

import java.io.File;

/**
 * Created by eason on 6/23/16.
 */
public class UpdateService extends Service {
    /** 安卓系统下载类 **/
    DownloadManager manager;

    /** 接收下载完的广播 **/
    DownloadCompleteReceiver receiver;

    private String appFileName = "学派.apk";

    /** 初始化下载器 **/
    private void initDownManager(String url) {

        manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

        receiver = new DownloadCompleteReceiver();

        DownloadManager.Request down = new DownloadManager.Request(
                Uri.parse(url));


        if(!UserManagerFunc.getInstance().getSetting(SettingName.NOWIFIDOWNLOAD)){
            down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        }else{
            down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
                    | DownloadManager.Request.NETWORK_WIFI);
        }

        down.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);

        down.setVisibleInDownloadsUi(true);

        down.setDestinationInExternalFilesDir(this,
                Environment.DIRECTORY_DOWNLOADS, appFileName);

        manager.enqueue(down);

        registerReceiver(receiver, new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String url = intent.getStringExtra("url");

        initDownManager(url);

        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        if (receiver != null)
            unregisterReceiver(receiver);

        super.onDestroy();
    }

    // 接受下载完成后的intent
    class DownloadCompleteReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(
                    DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                long downId = intent.getLongExtra(
                        DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                installAPK(manager.getUriForDownloadedFile(downId));
                UpdateService.this.stopSelf();

            }
        }

        /**
         * 安装apk文件
         */
        private void installAPK(Uri apk) {
            Intent intents = new Intent();
            intents.setAction("android.intent.action.VIEW");
            intents.addCategory("android.intent.category.DEFAULT");
            intents.setType("application/vnd.android.package-archive");
            intents.setData(apk);
            intents.setDataAndType(apk,"application/vnd.android.package-archive");
            intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intents);
            android.os.Process.killProcess(android.os.Process.myPid());
        }

    }
}
