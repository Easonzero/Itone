package com.wangyi.UIview.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;

import com.wangyi.UIview.BaseFragment;
import com.wangyi.define.EventName;
import com.wangyi.reader.R;
import com.wangyi.utils.ImagePicker;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.File;

/**
 * Created by eason on 5/24/16.
 */

@ContentView(R.layout.fragment_register4)
public class RegisterFragment4 extends BaseFragment {
    @ViewInject(R.id.headpic)
    private ImageView picture;

    private boolean isAddPic = false;

    @Event(R.id.headpic)
    private void onPicClick(View view){
        showSettingFaceDialog();
    }

    /*
	 * 创建图片选择器，准备裁剪。
	 */

    private String[] items = new String[] { "图库","拍照" };
    private static final String IMAGE_FILE_NAME = "headPic.jpg";

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
                                    startActivityForResult(intent,EventName.ImagePicker.SELECT_PIC_KITKAT);
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
            switch (requestCode) {
                case EventName.ImagePicker.IMAGE_REQUEST_CODE:
                    startPhotoZoom(data.getData());
                    break;
                case EventName.ImagePicker.SELECT_PIC_KITKAT:
                    startPhotoZoom(data.getData());
                    break;
                case EventName.ImagePicker.CAMERA_REQUEST_CODE:
                    File tempFile = new File("/sdcard/" + IMAGE_FILE_NAME);
                    startPhotoZoom(Uri.fromFile(tempFile));
                    break;
                case EventName.ImagePicker.RESULT_REQUEST_CODE:
                    if (data != null) {
                        ImagePicker.setImageToView(data,picture);
                        isAddPic = true;
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void startPhotoZoom(Uri uri) {
        if (uri == null) {
            return;
        }

        Intent intent = new Intent("com.android.camera.action.CROP");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            String url= ImagePicker.getPath(getContext(),uri);
            intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
        }else{
            intent.setDataAndType(uri, "image/*");
        }
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, EventName.ImagePicker.RESULT_REQUEST_CODE);
    }
}
