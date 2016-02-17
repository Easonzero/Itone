package com.wangyi.view.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.xutils.view.annotation.*;

import com.wangyi.utils.ImageUtil;
import com.wangyi.utils.MyHttps;
import com.wangyi.view.BaseActivity;
import com.wangyi.reader.R;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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
	private boolean isAddPic = false;
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
					MyHttps myHttpsre = new MyHttps(url);
					File file;
					if(isAddPic){
						file = new File("/sdcard/" + IMAGE_FILE_NAME);
					}
					else{
						file = null;
					}
					HttpResponse httpResponse = myHttpsre.postForm(userInfo, file);
					if(httpResponse != null){
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
					}
				}
			}.start();
		}
	}
	
	private String[] items = new String[] { "图库","拍照" };
	private static final String IMAGE_FILE_NAME = "headPic.jpg";
	private static final int IMAGE_REQUEST_CODE = 0;
	private static final int SELECT_PIC_KITKAT = 3;
	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int RESULT_REQUEST_CODE = 2;

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
								startActivityForResult(intent,SELECT_PIC_KITKAT);
							} else {
								startActivityForResult(intent,IMAGE_REQUEST_CODE);
							}
							break;
						case 1:
							Intent intentFromCapture = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
								intentFromCapture.putExtra(
										MediaStore.EXTRA_OUTPUT,
										Uri.fromFile(new File("/sdcard/" + IMAGE_FILE_NAME)));
							startActivityForResult(intentFromCapture,
									CAMERA_REQUEST_CODE);
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
			case IMAGE_REQUEST_CODE:
				startPhotoZoom(data.getData());
				break;
			case SELECT_PIC_KITKAT:
				startPhotoZoom(data.getData());
				break;
			case CAMERA_REQUEST_CODE:
				File tempFile = new File("/sdcard/" + IMAGE_FILE_NAME);
				startPhotoZoom(Uri.fromFile(tempFile));
				break;
			case RESULT_REQUEST_CODE:
				if (data != null) {
					setImageToView(data,picture);
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
			String url=getPath(RegisterActivity.this,uri);
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
		startActivityForResult(intent, RESULT_REQUEST_CODE);
	}
	
	private void setImageToView(Intent data,ImageView imageView) {
		Bundle extras = data.getExtras();
		isAddPic = true;
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			Bitmap roundBitmap=ImageUtil.toRoundBitmap(photo);
			imageView.setImageBitmap(roundBitmap);
			saveBitmap(photo);
		}
	}

	public void saveBitmap(Bitmap mBitmap) {
		File f = new File("/sdcard/" + IMAGE_FILE_NAME);
		try {
			f.createNewFile();
			FileOutputStream fOut = null;
			fOut = new FileOutputStream(f);
			mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
			fOut.flush();
			fOut.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressLint("NewApi")
	public static String getPath(final Context context, final Uri uri) {

	    final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

	    if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
	    	
	        if (isExternalStorageDocument(uri)) {
	            final String docId = DocumentsContract.getDocumentId(uri);
	            final String[] split = docId.split(":");
	            final String type = split[0];

	            if ("primary".equalsIgnoreCase(type)) {
	                return Environment.getExternalStorageDirectory() + "/" + split[1];
	            }

	        }
	        
	        else if (isDownloadsDocument(uri)) {
	            final String id = DocumentsContract.getDocumentId(uri);
	            final Uri contentUri = ContentUris.withAppendedId(
	                    Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

	            return getDataColumn(context, contentUri, null, null);
	        }
	        
	        else if (isMediaDocument(uri)) {
	            final String docId = DocumentsContract.getDocumentId(uri);
	            final String[] split = docId.split(":");
	            final String type = split[0];

	            Uri contentUri = null;
	            if ("image".equals(type)) {
	                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
	            } else if ("video".equals(type)) {
	                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
	            } else if ("audio".equals(type)) {
	                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
	            }

	            final String selection = "_id=?";
	            final String[] selectionArgs = new String[] {
	                    split[1]
	            };

	            return getDataColumn(context, contentUri, selection, selectionArgs);
	        }
	    }
	    
	    else if ("content".equalsIgnoreCase(uri.getScheme())) {
	        if (isGooglePhotosUri(uri))
	            return uri.getLastPathSegment();

	        return getDataColumn(context, uri, null, null);
	    }
	    
	    else if ("file".equalsIgnoreCase(uri.getScheme())) {
	        return uri.getPath();
	    }

	    return null;
	}
	
	public static String getDataColumn(Context context, Uri uri, String selection,
	        String[] selectionArgs) {

	    Cursor cursor = null;
	    final String column = "_data";
	    final String[] projection = {
	            column
	    };

	    try {
	        cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
	                null);
	        if (cursor != null && cursor.moveToFirst()) {
	            final int index = cursor.getColumnIndexOrThrow(column);
	            return cursor.getString(index);
	        }
	    } finally {
	        if (cursor != null)
	            cursor.close();
	    }
	    return null;
	}


	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
	    return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
	    return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
	    return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
	    return "com.google.android.apps.photos.content".equals(uri.getAuthority());
	}
	
	public boolean isNetworkConnected(Context context) {
		if (context != null) {
		ConnectivityManager mConnectivityManager = (ConnectivityManager) context
		.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
		if (mNetworkInfo != null) {
		return mNetworkInfo.isAvailable();
		}
		}
		return false;
	}
}
