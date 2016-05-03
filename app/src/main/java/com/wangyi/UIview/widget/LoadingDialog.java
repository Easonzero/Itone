package com.wangyi.UIview.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import com.wangyi.reader.R;

/**
 * Created by maxchanglove on 2016/2/27.
 */
public class LoadingDialog {
    private Dialog mDialog;
    private LoadingView mLoadingView;
    private View mDialogContentView;

    public LoadingDialog(Context context){
        init(context);
    }

    public void init(Context context){
        mDialog = new Dialog(context,R.style.custom_dialog);
        mDialogContentView= LayoutInflater.from(context).inflate(R.layout.loadingview,null);
        mLoadingView= (LoadingView) mDialogContentView.findViewById(R.id.loadView);
        mDialog.setContentView(mDialogContentView);
        mLoadingView.setLoadingText("Loading...");
        mDialog.setCanceledOnTouchOutside(false);
    }

    public void show(){
        mDialog.show();
    }

    public void dismiss(){
        mDialog.dismiss();
    }
}
