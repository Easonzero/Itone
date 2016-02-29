package com.wangyi.UIview.activity;

import java.io.File;

import com.wangyi.UIview.widget.LoadingDialog;
import com.wangyi.define.UserInfo;
import com.wangyi.function.HttpsFunc;
import org.xutils.view.annotation.*;

import com.wangyi.utils.ImagePicker;
import com.wangyi.UIview.BaseActivity;
import com.wangyi.define.EventName;
import com.wangyi.reader.R;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import org.xutils.x;

@ContentView(R.layout.register)
public class RegisterActivity extends BaseActivity {
    @ViewInject(R.id.id)
    private EditText id;
    @ViewInject(R.id.userName)
    private EditText userName;
    @ViewInject(R.id.passWords)
    private EditText passWords;
    @ViewInject(R.id.province)
    private EditText province;
    @ViewInject(R.id.city)
    private EditText city;
    @ViewInject(R.id.university)
    private EditText university;
    @ViewInject(R.id.faculty)
    private EditText faculty;
    @ViewInject(R.id.occupation)
    private EditText occupation;
    @ViewInject(R.id.picture)
    private ImageView picture;
    private boolean isAddPic = false;
    private LoadingDialog loading;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case EventName.UI.FAULT:
                    loading.dismiss();
                    break;
                case EventName.UI.START:
                    loading.show();
                    break;
                case EventName.UI.SUCCESS:
                    loading.dismiss();
                    RegisterActivity.this.finish();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        loading = new LoadingDialog(this);
    }

    @Event(R.id.picture)
    private void onPictureClick(View view){
        showSettingFaceDialog();
    }

    @Event(R.id.back)
    private void onBackClick(View view){
        RegisterActivity.this.finish();
    }

    @Event(R.id.confirm)
    private void onConfirmClick(View view){
        if(passWords.getText().toString().equals("")||
                userName.getText().toString().equals("")||
                id.getText().toString().equals("")){
            Toast.makeText(RegisterActivity.this, "必填项填写不全", 5000).show();
        }
        else{
            UserInfo user = new UserInfo();
            user.id = id.getText().toString();
            user.userName = id.getText().toString();
            user.passWords = passWords.getText().toString();
            user.university = university.getText().toString();
            user.city = city.getText().toString();
            user.faculty = faculty.getText().toString();
            user.province = province.getText().toString();
            user.occupation = occupation.getText().toString();
            user.picture = isAddPic+"";
            HttpsFunc.getInstance().connect(handler).register(user);
        }
    }

	/*
	 * 创建图片选择器，准备裁剪。
	 */

    private String[] items = new String[] { "图库","拍照" };
    private static final String IMAGE_FILE_NAME = "headPic.jpg";

    private void showSettingFaceDialog() {
        new AlertDialog.Builder(this)
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
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
            String url=ImagePicker.getPath(RegisterActivity.this,uri);
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
