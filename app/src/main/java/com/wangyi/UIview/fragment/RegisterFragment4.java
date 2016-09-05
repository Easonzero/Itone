package com.wangyi.UIview.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wangyi.Itone;
import com.wangyi.UIview.BaseFragment;
import com.wangyi.UIview.activity.SearchInfoActivity;
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

/**
 * Created by eason on 5/24/16.
 */

@ContentView(R.layout.fragment_register4)
public class RegisterFragment4 extends BaseFragment {
    public final int UNIVERSITY = 4;
    public final int FACULTY = 5;
    public final int CLASS = 6;
    public final int GRADE = 7;
    @ViewInject(R.id.headpic)
    private ImageView picture;
    @ViewInject(R.id.university)
    private TextView university;
    @ViewInject(R.id.faculty)
    private TextView faculty;
    @ViewInject(R.id.Class)
    private TextView Class;
    @ViewInject(R.id.grade)
    private TextView grade;
    @ViewInject(R.id.userName)
    private EditText userName;

    private boolean isAddPic = false;

    public void register(){
        if(university.getText().toString().equals("")||
                faculty.getText().toString().equals("")||
                Class.getText().toString().equals("")||
                grade.getText().toString().equals("")||
                userName.getText().toString().equals("")){
            ItOneUtils.showToast(getContext(),"有信息项未填写");
            return;
        }

        String[] params = ItOneUtils.parseMessage(getMessage());
        UserInfo user = new UserInfo();
        user.id = params[0];
        user.passWords = params[1];
        user.Class = Class.getText().toString();
        user.faculty = faculty.getText().toString();
        user.picture = isAddPic?"true":"false";
        user.university = university.getText().toString();
        user.grade = grade.getText().toString();
        user.userName = userName.getText().toString();
        HttpsFunc.getInstance().connect(handler).commitForm(user,"register");
    }

    public void setData(){
        UserInfo userInfo = UserManagerFunc.getInstance().getUserInfo();
        userName.setText(userInfo.userName);
        Class.setText(userInfo.Class);
        faculty.setText(userInfo.faculty);
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
                picture, HttpsFunc.host +
                        userInfo.picture +
                        "headPic.jpg",options
        );
    }

    public void modify(){
        if(university.getText().toString().equals("")||
                faculty.getText().toString().equals("")||
                Class.getText().toString().equals("")||
                grade.getText().toString().equals("")||
                userName.getText().toString().equals("")){
            ItOneUtils.showToast(getContext(),"有信息项未填写");
            return;
        }

        UserInfo userInfo = new UserInfo();
        userInfo.id = UserManagerFunc.getInstance().getUserInfo().id;
        userInfo.Class = Class.getText().toString();
        userInfo.faculty = faculty.getText().toString();
        userInfo.picture = isAddPic?"true":"false";
        userInfo.university = university.getText().toString();
        userInfo.grade = grade.getText().toString();
        userInfo.userName = userName.getText().toString();

        HttpsFunc.getInstance().connect(handler).commitForm(
                userInfo,"modify");
    }

    @Event(R.id.headpic)
    private void onPicClick(View view){
        showSettingFaceDialog();
    }

    @Event(R.id.university)
    private void onUniversityClick(View view){
        Intent intent = new Intent(getContext(), SearchInfoActivity.class);
        intent.putExtra("scategory","university");
        startActivityForResult(intent,UNIVERSITY);
    }

    @Event(R.id.faculty)
    private void onFacultyClick(View view){
        Intent intent = new Intent(getContext(), SearchInfoActivity.class);
        intent.putExtra("scategory","faculty");
        String fromUniversity = university.getText().toString();
        intent.putExtra("fromUniversity",fromUniversity);
        startActivityForResult(intent,FACULTY);
    }

    @Event(R.id.Class)
    private void onClassClick(View view){
        Intent intent = new Intent(getContext(), SearchInfoActivity.class);
        intent.putExtra("scategory","class");
        String fromUniversity = university.getText().toString();
        intent.putExtra("fromUniversity",fromUniversity);
        String fromFaculty = faculty.getText().toString();
        intent.putExtra("fromFaculty",fromFaculty);
        startActivityForResult(intent,CLASS);
    }

    @Event(R.id.grade)
    private void onGradeClick(View view){
        Intent intent = new Intent(getContext(), SearchInfoActivity.class);
        intent.putExtra("scategory","grade");
        startActivityForResult(intent,GRADE);
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
                case UNIVERSITY:
                    university.setText(data.getStringExtra("result"));
                    faculty.setText("");
                    Class.setText("");
                    break;
                case FACULTY:
                    faculty.setText(data.getStringExtra("result"));
                    Class.setText("");
                    break;
                case CLASS:
                    Class.setText(data.getStringExtra("result"));
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
