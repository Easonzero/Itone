package com.wangyi.UIview.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wangyi.UIview.BaseActivity;
import com.wangyi.UIview.widget.dialog.LoadingDialog;
import com.wangyi.define.EventName;
import com.wangyi.define.bean.UserInfo;
import com.wangyi.function.HttpsFunc;
import com.wangyi.function.UserManagerFunc;
import com.wangyi.reader.R;
import com.wangyi.utils.ImagePicker;
import com.wangyi.utils.ItOneUtils;

import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.lang.reflect.Modifier;

/**
 * Created by eason on 5/20/16.
 */
@ContentView(R.layout.activity_userinfo)
public class UserInfoActivity extends BaseActivity{
    public final int UNIVERSITY = 4;
    public final int CLASS = 5;
    public final int GRADE = 6;

    @ViewInject(R.id.name)
    private EditText name;
    @ViewInject(R.id.university)
    private TextView university;
    @ViewInject(R.id.Class)
    private TextView Class;
    @ViewInject(R.id.grade)
    private TextView grade;
    @ViewInject(R.id.headpic)
    private ImageView headPic;

    private LoadingDialog loading;

    private boolean isModify = false;
    private boolean isAddPic = false;
    private String fromFauclty;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch(msg.what){
                case EventName.UI.START:
                    loading.show();
                    break;
                case EventName.UI.FINISH:
                    loading.dismiss();
                    HttpsFunc.getInstance().connect(handler).visitUserInfo();
                    break;
                case EventName.UI.SUCCESS:
                    loading.dismiss();
                    setData();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loading = new LoadingDialog(this);
        if(UserManagerFunc.getInstance().isLogin()){
            setData();
        }
    }

    private void setData(){
        UserInfo userInfo = UserManagerFunc.getInstance().getUserInfo();
        changeMode();
        name.setText(userInfo.userName);
        Class.setText(userInfo.Class);
        grade.setText(userInfo.grade);
        university.setText(userInfo.university);
        ImageOptions options=new ImageOptions.Builder()
                .setLoadingDrawableId(R.drawable.headpic)
                .setFailureDrawableId(R.drawable.headpic)
                .setUseMemCache(true)
                .setCircular(true)
                .setIgnoreGif(false)
                .build();
        x.image().bind(
                headPic, HttpsFunc.host +
                        userInfo.picture +
                        "headPic.jpg",options
        );
    }

    private void changeMode(){
        name.setClickable(isModify);
        name.setFocusable(isModify);
        Class.setClickable(isModify);
        grade.setClickable(isModify);
        university.setClickable(isModify);
        headPic.setClickable(isModify);
    }

    private void modify(){
        if(university.getText().toString().equals("")||
                Class.getText().toString().equals("")||
                grade.getText().toString().equals("")||
                name.getText().toString().equals("")){
            ItOneUtils.showToast(this,"有信息项未填写");
            return;
        }

        UserInfo userInfo = new UserInfo();
        userInfo.id = UserManagerFunc.getInstance().getUserInfo().id;
        userInfo.Class = Class.getText().toString();
        userInfo.faculty = fromFauclty;
        userInfo.picture = isAddPic?"true":"false";
        userInfo.university = university.getText().toString();
        userInfo.grade = grade.getText().toString();
        userInfo.userName = name.getText().toString();

        HttpsFunc.getInstance().connect(handler).commitForm(
                userInfo,"modify");
    }

    @Event(R.id.save)
    private void onModifyClick(View view){
        if(!isModify){
            ((Button)view).setText("保存");
        }else{
            ((Button)view).setText("修改");
            modify();
        }
        isModify = !isModify;
        changeMode();
    }

    @Event(R.id.back)
    private void onBackClick(View view){
        this.finish();
    }

    @Event(R.id.headpic)
    private void onPicClick(View view){
        showSettingFaceDialog();
    }

    @Event(R.id.university)
    private void onUniversityClick(View view){
        Intent intent = new Intent(this, SearchInfoActivity.class);
        intent.putExtra("scategory","university");
        startActivityForResult(intent,UNIVERSITY);
    }

    @Event(R.id.Class)
    private void onClassClick(View view){
        Intent intent = new Intent(this, SearchInfoActivity.class);
        intent.putExtra("scategory","class");
        String fromUniversity = university.getText().toString();
        intent.putExtra("fromUniversity",fromUniversity);
        startActivityForResult(intent,CLASS);
    }

    @Event(R.id.grade)
    private void onGradeClick(View view){
        Intent intent = new Intent(this, SearchInfoActivity.class);
        intent.putExtra("scategory","grade");
        startActivityForResult(intent,GRADE);
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
                        ImagePicker.setImageToView(data,headPic);
                        isAddPic = true;
                    }
                    break;
                case UNIVERSITY:
                    university.setText(data.getStringExtra("result"));
                    break;
                case CLASS:
                    String[] results = ItOneUtils.parseMessage(data.getStringExtra("result"));
                    Class.setText(results[0]);
                    fromFauclty = results[1];
                    break;
                case GRADE:
                    grade.setText(data.getStringExtra("result"));
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
            String url= ImagePicker.getPath(this,uri);
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
