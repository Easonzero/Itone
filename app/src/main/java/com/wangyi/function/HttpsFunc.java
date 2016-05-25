package com.wangyi.function;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.wangyi.define.UserPlus;
import com.wangyi.define.UserRank;
import com.wangyi.function.funchelp.Function;
import com.wangyi.utils.ImagePicker;
import org.xutils.common.Callback;

import com.wangyi.define.BookData;
import com.wangyi.define.EventName;
import com.wangyi.define.UserInfo;
import com.wangyi.utils.HttpsUtils;
import com.wangyi.utils.ItOneUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;

import org.xutils.ex.DbException;
import org.xutils.view.annotation.Event;

public class HttpsFunc implements Function {
	public static String host = "http://itone.azurewebsites.net";

	private static final HttpsFunc INSTANCE = new HttpsFunc();

	private Handler handler = null;
	private Context context;
	private void HttpsFunc(){}

	public static HttpsFunc getInstance(){
		return INSTANCE;
	}

	@Override
	public void init(Context context){
		this.context = context;
	}

	public HttpsFunc connect(Handler handler){
        if(this.handler != handler)
            this.handler = handler;
        return INSTANCE;
	}

	public void disconnect(){
		this.handler = null;
	}

	public void Login(String userID,String passWords){
		if(!isNetworkConnected()){
			ItOneUtils.showToast(context,"网络未连接");
			return;
		}
		if(userID.equals("")||passWords.equals("")){
			ItOneUtils.showToast(context,"账号密码不能为空");
            return;
		}
		if(handler!=null) handler.sendEmptyMessage(EventName.UI.START);
		Map<String,Object> map=new HashMap<>();
		map.put("id", userID);
		map.put("passWords", passWords);
		HttpsUtils.Post(host+"/users/login", map, new Callback.CommonCallback<String>(){

			@Override
			public void onCancelled(CancelledException arg0) {}

			@Override
			public void onError(Throwable ex, boolean isCheck) {
				ItOneUtils.showToast(context,"服务器抽风中...");
				if(handler!=null) handler.sendEmptyMessage(EventName.UI.FAULT);
			}

			@Override
			public void onFinished() {}

			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub
				if(result.equals(EventName.Https.OK)){
					ItOneUtils.showToast(context,"登陆成功");
					visitUserInfo();
					visitUserElseInfo();
					visitUserRank();
					UserManagerFunc.getInstance().setLoginStatus(true);
				}else if(result.equals(EventName.Https.ERROR)){
					ItOneUtils.showToast(context,"用户名或密码错误");
					if(handler!=null) handler.sendEmptyMessage(EventName.UI.FAULT);
				}
			}

		});
	}

	public void commitForm(UserInfo user,String formkind){
		if(!isNetworkConnected()){
			ItOneUtils.showToast(context,"网络未连接");
			return;
		}
		Map<String,Object> map=new HashMap<>();
		map.put("userinfo",new Gson().toJson(user));
		if(user.picture.equals("true"))
			map.put("file", new File(ImagePicker.SAVE_PATH));
		if(handler!=null) handler.sendEmptyMessage(EventName.UI.START);
		HttpsUtils.UpLoadFile(host+"/users/"+formkind, map, new Callback.CommonCallback<String>(){

			@Override
			public void onCancelled(CancelledException arg0) {}

			@Override
			public void onError(Throwable ex, boolean isCheck) {
				ItOneUtils.showToast(context,"服务器抽风中...");
				if(handler!=null) handler.sendEmptyMessage(EventName.UI.FAULT);
			}

			@Override
			public void onFinished() {}

			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub
				if(result.equals(EventName.Https.OK)){
					ItOneUtils.showToast(context,"成功");
					if(handler!=null) handler.sendEmptyMessage(EventName.UI.SUCCESS);
				}else if(result.equals(EventName.Https.ERROR)){
					ItOneUtils.showToast(context,"用户名已经存在");
					if(handler!=null) handler.sendEmptyMessage(EventName.UI.FAULT);
				}
			}

		});
	}

	public void searchBookByUser(){
		if(!isNetworkConnected()){
			ItOneUtils.showToast(context,"网络未连接");
			return;
		}
		if(!UserManagerFunc.getInstance().isLogin()) return;
		if(handler!=null) handler.sendEmptyMessage(EventName.UI.START);
		Map<String,String> map=new HashMap<>();
		map.put("uid", UserManagerFunc.getInstance().getUserInfo().id);
		HttpsUtils.Get(host+"/books/userbooks", map, new Callback.CommonCallback<ArrayList<BookData>>(){

			@Override
			public void onCancelled(CancelledException arg0) {}

			@Override
			public void onError(Throwable ex, boolean isCheck) {
				ItOneUtils.showToast(context,"服务器抽风中...");
			}

			@Override
			public void onFinished() {
				if(handler!=null) handler.sendEmptyMessage(EventName.UI.FINISH);
			}

			@Override
			public void onSuccess(ArrayList<BookData> result) {
				// TODO Auto-generated method stub
				handler.obtainMessage(EventName.UI.SUCCESS,result).sendToTarget();
			}

		});
	}

	public void searchBookByName(String bookName){
		if(!isNetworkConnected()) {
			ItOneUtils.showToast(context, "网络未连接");
			return;
		}

		if(!UserManagerFunc.getInstance().isLogin()) {
			ItOneUtils.showToast(context, "请先登录");
			return;
		}

		if(handler!=null) handler.sendEmptyMessage(EventName.UI.START);
		String university = UserManagerFunc.getInstance().getUserInfo().university;

		Map<String,String> map=new HashMap<>();
		map.put("bookName", bookName);
		map.put("university",university);
		HttpsUtils.Get(host+"/books/search", map, new Callback.CommonCallback<ArrayList<BookData>>(){

			@Override
			public void onCancelled(CancelledException arg0) {}

			@Override
			public void onError(Throwable ex, boolean isCheck) {
				ItOneUtils.showToast(context,"服务器抽风中...");
			}

			@Override
			public void onFinished() {
				if(handler!=null) handler.sendEmptyMessage(EventName.UI.FINISH);
			}

			@Override
			public void onSuccess(ArrayList<BookData> result) {
				// TODO Auto-generated method stub
				handler.obtainMessage(EventName.UI.SUCCESS,result).sendToTarget();
			}

		});
	}

	public void searchBooksBySubject(String subject,String university,int count){
		if(!isNetworkConnected()){
			ItOneUtils.showToast(context,"网络未连接");
			return;
		}
		if(handler!=null) handler.sendEmptyMessage(EventName.UI.START);
		Map<String,String> map=new HashMap<>();
		map.put("subject", subject);
		map.put("university", university);
		HttpsUtils.Get(host+"/books/booklist", map, new Callback.CommonCallback<ArrayList<BookData>>(){

			@Override
			public void onCancelled(CancelledException arg0) {}

			@Override
			public void onError(Throwable ex, boolean isCheck) {
				ItOneUtils.showToast(context,"服务器抽风中...");
			}

			@Override
			public void onFinished() {
				if(handler!=null) handler.sendEmptyMessage(EventName.UI.FINISH);
			}

			@Override
			public void onSuccess(ArrayList<BookData> result) {
				handler.obtainMessage(EventName.UI.SUCCESS,result).sendToTarget();
			}

		});
	}

	private void visitUserInfo(){
		HttpsUtils.Post(host+"/users/userbaseinfo", null, new Callback.CommonCallback<UserInfo>(){

			@Override
			public void onCancelled(CancelledException arg0) {}

			@Override
			public void onError(Throwable ex, boolean isCheck) {
				// TODO Auto-generated method stub
				if(handler!=null) ItOneUtils.showToast(context,"服务器抽风中...");
			}

			@Override
			public void onFinished() {}

			@Override
			public void onSuccess(UserInfo result) {
				// TODO Auto-generated method stub
				if(result != null){
					UserManagerFunc.getInstance().setUserInfo(result);
                    if(handler!=null) handler.sendEmptyMessage(EventName.UI.SUCCESS);
				}
			}

		});
	}

	private void visitUserElseInfo(){
		HttpsUtils.Post(host+"/users/userelseinfo", null, new Callback.CommonCallback<UserPlus>(){

			@Override
			public void onCancelled(CancelledException arg0) {}

			@Override
			public void onError(Throwable ex, boolean isCheck) {
				// TODO Auto-generated method stub
				if(handler!=null) ItOneUtils.showToast(context,"服务器抽风中...");
			}

			@Override
			public void onFinished() {}

			@Override
			public void onSuccess(UserPlus result) {
				// TODO Auto-generated method stub
				if(result != null){
					UserManagerFunc.getInstance().setUserPlus(result);
					if(handler!=null) handler.sendEmptyMessage(EventName.UI.SUCCESS);
				}
			}

		});
	}

	private void visitUserRank(){
		HttpsUtils.Get(host+"/users/getrank", null, new Callback.CommonCallback<Integer>(){

			@Override
			public void onCancelled(CancelledException arg0) {}

			@Override
			public void onError(Throwable ex, boolean isCheck) {
				// TODO Auto-generated method stub
				if(handler!=null) ItOneUtils.showToast(context,"服务器抽风中...");
			}

			@Override
			public void onFinished() {}

			@Override
			public void onSuccess(Integer result) {
				// TODO Auto-generated method stub
				if(result != null){
					UserManagerFunc.getInstance().setRank(result);
					if(handler!=null) handler.sendEmptyMessage(EventName.UI.SUCCESS);
				}
			}

		});
	}

	public void getRankByOrder(){
		if(!isNetworkConnected()){
			ItOneUtils.showToast(context,"网络未连接");
			return;
		}
		handler.sendEmptyMessage(EventName.UI.START);
		HttpsUtils.Get(host+"/users/usersbyorder",null, new Callback.CommonCallback<ArrayList<UserRank>>(){

			@Override
			public void onCancelled(CancelledException arg0) {}

			@Override
			public void onError(Throwable ex, boolean isCheck) {
				ItOneUtils.showToast(context,"服务器抽风中...");
			}

			@Override
			public void onFinished() {
				if(handler!=null) handler.sendEmptyMessage(EventName.UI.FINISH);
			}

			@Override
			public void onSuccess(ArrayList<UserRank> result) {
				handler.obtainMessage(EventName.UI.SUCCESS,result).sendToTarget();
			}

		});
	}

	public void download(long id,String label) throws DbException {
		if(!isNetworkConnected()){
			ItOneUtils.showToast(context,"网络未连接");
			return;
		}
        DownloadManagerFunc.getInstance().startDownload(
                host+"/download?id="+id, label,
               BookManagerFunc.FILEPATH + label + ".pdf", true, false, null);
	}

	public void commitIdea(String advice){
        if(!isNetworkConnected()){
            ItOneUtils.showToast(context,"网络未连接");
            return;
        }
        handler.sendEmptyMessage(EventName.UI.START);
        Map<String,Object> map=new HashMap<>();
		String id = UserManagerFunc.getInstance().isLogin()?
				UserManagerFunc.getInstance().getUserInfo().id:null;
		map.put("id", id);
        map.put("advice", advice);
        HttpsUtils.Post(host+"/advice", map, new Callback.CommonCallback<String>(){

            @Override
            public void onCancelled(CancelledException arg0) {}

            @Override
            public void onError(Throwable ex, boolean isCheck) {
                ItOneUtils.showToast(context,"服务器抽风中...");
            }

            @Override
            public void onFinished() {
                if(handler!=null) handler.sendEmptyMessage(EventName.UI.FINISH);
            }

            @Override
            public void onSuccess(String result) {
                // TODO Auto-generated method stub
                ItOneUtils.showToast(context,"提交成功");
                if(handler!=null) handler.sendEmptyMessage(EventName.UI.SUCCESS);
            }

        });
	}

	public void getMessage(String date){
		if(!isNetworkConnected()){
			ItOneUtils.showToast(context,"网络未连接");
			return;
		}
		if(handler != null) handler.sendEmptyMessage(EventName.UI.START);
		Map<String,Object> map=new HashMap<>();
		String id = UserManagerFunc.getInstance().isLogin()?
				UserManagerFunc.getInstance().getUserInfo().id:null;
		map.put("id", id);
		map.put("date", date);
		HttpsUtils.Post(host+"/message", map, new Callback.CommonCallback<List<com.wangyi.define.Message>>(){

			@Override
			public void onCancelled(CancelledException arg0) {}

			@Override
			public void onError(Throwable ex, boolean isCheck) {
			}

			@Override
			public void onFinished() {
				if(handler != null) handler.sendEmptyMessage(EventName.UI.FINISH);
			}

			@Override
			public void onSuccess(List<com.wangyi.define.Message> result) {
				// TODO Auto-generated method stub
				if(handler != null) handler.sendEmptyMessage(EventName.UI.SUCCESS);
				MessageFunc.getInstance().addMessages(result);
			}

		});
	}

	public void getHomework(String date){
		if(!isNetworkConnected()){
			ItOneUtils.showToast(context,"网络未连接");
			return;
		}
		if(handler != null) handler.sendEmptyMessage(EventName.UI.START);
		Map<String,Object> map=new HashMap<>();
		String id = UserManagerFunc.getInstance().isLogin()?
				UserManagerFunc.getInstance().getUserInfo().id:null;
		map.put("id", id);
		map.put("date", date);
		HttpsUtils.Post(host+"/homework", map, new Callback.CommonCallback<List<com.wangyi.define.Message>>(){

			@Override
			public void onCancelled(CancelledException arg0) {}

			@Override
			public void onError(Throwable ex, boolean isCheck) {
			}

			@Override
			public void onFinished() {
				if(handler != null) handler.sendEmptyMessage(EventName.UI.FINISH);
			}

			@Override
			public void onSuccess(List<com.wangyi.define.Message> result) {
				// TODO Auto-generated method stub
				if(handler != null) handler.sendEmptyMessage(EventName.UI.SUCCESS);
				MessageFunc.getInstance().addMessages(result);
			}

		});
	}

	private boolean isNetworkConnected() {
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
