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
import com.wangyi.UIview.activity.SearchInfoActivity;
import com.wangyi.UIview.widget.dialog.PickDateDialog;
import com.wangyi.define.EventName;
import com.wangyi.function.HomeworkFunc;
import com.wangyi.function.HttpsFunc;
import com.wangyi.function.UserManagerFunc;
import com.wangyi.reader.R;
import com.wangyi.utils.ImagePicker;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.io.IOException;

/**
 * Created by eason on 7/24/16.
 */
@ContentView(R.layout.fragment_publishhomework)
public class PublishHomeworkFragment extends BaseFragment{
    @ViewInject(R.id.name)
    private TextView name;
    @ViewInject(R.id.date)
    private TextView date;
    @ViewInject(R.id.content)
    private EditText content;
    @ViewInject(R.id.addpic)
    private ImageButton addpic;

    private int courseNo;
    private boolean addPic =false;

    private final int PICKCOURSE = 4;

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

    }

    @Event(R.id.commit)
    private void onCommit(View view){
        HttpsFunc.getInstance().connect(handler).sendHomework(
                courseNo,content.getText().toString(),date.getText().toString(),addPic?"true":"false");
    }

    @Event(R.id.date)
    private void onDate(View view){
        final PickDateDialog dialog = new PickDateDialog(getContext());
        dialog.setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        date.setText(dialog.getDate());
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }

    @Event(R.id.name)
    private void onName(View view){
        Intent intent = new Intent(getContext(), SearchInfoActivity.class);
        intent.putExtra("scategory","course");
        String fromUniversity = UserManagerFunc.getInstance().getUserInfo().university;
        intent.putExtra("fromUniversity",fromUniversity);
        startActivityForResult(intent,PICKCOURSE);
    }

    /*
	 * 创建图片选择器，准备裁剪。
	 */

    private String[] items = new String[] { "图库","拍照" };
    private static final String IMAGE_FILE_NAME = "homework.jpg";

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
            switch (requestCode) {
                case EventName.ImagePicker.IMAGE_REQUEST_CODE:
                case EventName.ImagePicker.SELECT_PIC_KITKAT:
                case EventName.ImagePicker.CAMERA_REQUEST_CODE:
                case EventName.ImagePicker.RESULT_REQUEST_CODE:
                    if (data != null) {
                        Uri uri = data.getData();
                        Bitmap photo = null;
                        try {
                            photo = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                            addpic.setImageBitmap(photo);
                            ImagePicker.saveBitmap(photo, ImagePicker.SAVE_PATH_HOMEWORK);
                            addPic = true;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case PICKCOURSE:
                    name.setText(data.getStringExtra("result"));
                    courseNo = data.getIntExtra("id",-1);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
