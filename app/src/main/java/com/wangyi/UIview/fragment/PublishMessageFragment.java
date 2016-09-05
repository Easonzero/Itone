package com.wangyi.UIview.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wangyi.UIview.BaseFragment;
import com.wangyi.define.EventName;
import com.wangyi.function.HttpsFunc;
import com.wangyi.reader.R;
import com.wangyi.utils.ImagePicker;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.io.IOException;

import com.afollestad.materialdialogs.MaterialDialog;;

/**
 * Created by eason on 7/24/16.
 */
@ContentView(R.layout.fragment_publishmessage)
public class PublishMessageFragment extends BaseFragment {
    @ViewInject(R.id.name)
    private TextView name;
    @ViewInject(R.id.content)
    private EditText content;
    @ViewInject(R.id.addpic)
    private ImageButton addpic;

    private boolean addPic = false;

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

    }

    @Event(R.id.commit)
    private void onCommit(View view){
        HttpsFunc.getInstance().connect(handler).sendMessage(
                name.getText().toString(),content.getText().toString(),addPic?"true":"false");
    }

    @Event(R.id.addpic)
    private void onPicClick(View view){
        showSettingFaceDialog();
    }

    @Event(R.id.name)
    private void onNameClick(View view){
        MaterialDialog chooseProperty = new MaterialDialog.Builder(getContext())
                .title("选择消息类型")
                .items("教室变更","课堂信息","活动信息","放假通知","其他")
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog materialDialog, View view, int which, CharSequence text) {
                        name.setText(text);
                        materialDialog.dismiss();
                    }
                }).show();
    }

    /*
	 * 创建图片选择器，准备裁剪。
	 */

    private String[] items = new String[] { "图库","拍照" };
    private static final String IMAGE_FILE_NAME = "message.jpg";

    private void showSettingFaceDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("图片来源")
                .setCancelable(true)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                                intent.addCategory(Intent.CATEGORY_OPENABLE);
                                intent.setType("image/*");
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                                    startActivityForResult(intent, EventName.ImagePicker.SELECT_PIC_KITKAT);
                                } else {
                                    startActivityForResult(intent,EventName.ImagePicker.IMAGE_REQUEST_CODE);
                                }
                                break;
                            case 1:
                                Intent intentFromCapture = new Intent(
                                        MediaStore.ACTION_IMAGE_CAPTURE);
                                intentFromCapture.putExtra(
                                        MediaStore.EXTRA_OUTPUT,
                                        Uri.fromFile(new File("/sdcard/" + IMAGE_FILE_NAME)));
                                startActivityForResult(intentFromCapture,
                                        EventName.ImagePicker.CAMERA_REQUEST_CODE);
                                break;
                        }
                    }
                })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        }).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_CANCELED) {
            if (data != null) {
                Uri uri = data.getData();
                Bitmap photo = null;
                try {
                    photo = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                    addpic.setImageBitmap(photo);
                    ImagePicker.saveBitmap(photo,ImagePicker.SAVE_PATH_MESSAGE);
                    addPic = true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
