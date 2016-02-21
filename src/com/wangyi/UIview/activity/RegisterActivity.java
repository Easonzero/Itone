package com.wangyi.UIview.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.xutils.view.annotation.*;

import com.wangyi.utils.ImagePicker;
import com.wangyi.utils.ItOneUtils;
import com.wangyi.UIview.BaseActivity;
import com.wangyi.define.EventName;
import com.wangyi.reader.R;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

@ContentView(R.layout.register)
public class RegisterActivity extends BaseActivity {
	@ViewInject(R.id.userName)
	private EditText userName;
	@ViewInject(R.id.passWords)
	private EditText passWords;
	@ViewInject(R.id.confirmPassWords)
	private EditText confirmPassWords;
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
	private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            if(msg.what == 1){
            	Toast.makeText(RegisterActivity.this, "该用户名已注册", 5000).show();
            }
            else if(msg.what == 2){
            	Toast.makeText(RegisterActivity.this, "注册成功", 5000).show();
            	RegisterActivity.this.finish();
            }
            else if(msg.what == 3){
            	Toast.makeText(RegisterActivity.this, "服务器抽风中...", 5000).show();
            }
            super.handleMessage(msg);
        }
    };
	Map<String, String> userInfo = new HashMap<String, String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
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
		if(confirmPassWords.getText().toString().equals("")||
				passWords.getText().toString().equals("")||
				userName.getText().toString().equals("")){
			Toast.makeText(RegisterActivity.this, "必填项填写不全", 5000).show();
		}
		else if(!confirmPassWords.getText().toString().equals(passWords.getText().toString())){
			Toast.makeText(RegisterActivity.this, "前后密码输入不一致", 5000).show();
		}
		else{
			new Thread(){
				public void run(){
					String url = "/ItOne/OpenFormServlet";
					userInfo.put("userName", userName.getText().toString());
    				userInfo.put("passWords", passWords.getText().toString());
    				userInfo.put("province", province.getText().toString());
    				userInfo.put("city", city.getText().toString());
    				userInfo.put("university", university.getText().toString());
    				userInfo.put("faculty", faculty.getText().toString());
    				userInfo.put("occupation", occupation.getText().toString());
    				userInfo.put("imageUrl", isAddPic?"true":"false");
					//MyHttps myHttpsre = new MyHttps(url);
					//HttpResponse httpResponse = myHttpsre.postForm(userInfo, file);
					/*if(httpResponse != null){
						Header[] is = httpResponse.getHeaders("isChecked");
						if(is[0].getValue().equals("true")){
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
						}
						else{
							Message message = new Message();
        					message.what = 2;
        					handler.sendMessage(message);
						}
					}
					else{
						Message message = new Message();
    					message.what = 3;
    					handler.sendMessage(message);
					}*/
				}
			}.start();
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
