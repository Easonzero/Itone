package com.wangyi.function;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.wangyi.define.bean.ClassInfo;
import com.wangyi.define.bean.Homework;
import com.wangyi.define.bean.University;
import com.wangyi.define.bean.UserPlus;
import com.wangyi.define.bean.UserRank;
import com.wangyi.function.funchelp.Function;
import com.wangyi.utils.ImagePicker;
import org.xutils.common.Callback;

import com.wangyi.define.bean.BookData;
import com.wangyi.define.EventName;
import com.wangyi.define.bean.UserInfo;
import com.wangyi.utils.HttpsUtils;
import com.wangyi.utils.ItOneUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

import org.xutils.ex.DbException;
import org.xutils.x;

public class HttpsFunc implements Function {
	public static String host = "http://119.29.229.214:3000";

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
		Map<String,String> map=new HashMap<>();
		map.put("id", userID);
		map.put("passWords", passWords);
		HttpsUtils.Post(host+"/users/login", map, new Callback.CommonCallback<String>(){

			@Override
			public void onCancelled(CancelledException arg0) {}

			@Override
			public void onError(Throwable ex, boolean isCheck) {
				ItOneUtils.showToast(context,"服务器抽风中ing");
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
		map.put("id",user.id);
		map.put("passWords",user.passWords);
		map.put("userName",user.userName);
		map.put("university",user.university);
		map.put("faculty",user.faculty);
		map.put("class",user.Class);
		map.put("grade",user.grade);
		map.put("picture",user.picture);
		if(user.picture.equals("true"))
			map.put("file", new File(ImagePicker.SAVE_PATH));
		if(handler!=null) handler.sendEmptyMessage(EventName.UI.START);
		HttpsUtils.UpLoadFile(host+"/users/"+formkind, map, new Callback.CommonCallback<String>(){

			@Override
			public void onCancelled(CancelledException arg0) {}

			@Override
			public void onError(Throwable ex, boolean isCheck) {
				ItOneUtils.showToast(x.app(),"服务器抽风中...");
				if(handler!=null) handler.sendEmptyMessage(EventName.UI.FAULT);
			}

			@Override
			public void onFinished() {}

			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub
				if(result.equals(EventName.Https.OK)){
					ItOneUtils.showToast(x.app(),"成功");
				}else if(result.equals(EventName.Https.ERROR)){
					ItOneUtils.showToast(x.app(),"用户名已经存在");
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
		HttpsUtils.Post(host+"/books/userbooks", map, new Callback.CommonCallback<List<BookData>>(){

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
			public void onSuccess(List<BookData> result) {
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
		map.put("fromUniversity",university);
		HttpsUtils.Post(host+"/books/search", map, new Callback.CommonCallback<List<BookData>>(){

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
			public void onSuccess(List<BookData> result) {
				// TODO Auto-generated method stub
				handler.obtainMessage(EventName.UI.SUCCESS,result).sendToTarget();
			}

		});
	}

	public void searchBooksBySubject(String subject,String university,int start){
		if(!isNetworkConnected()){
			ItOneUtils.showToast(context,"网络未连接");
			return;
		}
		if(handler!=null) handler.sendEmptyMessage(EventName.UI.START);
		Map<String,String> map=new HashMap<>();
		map.put("subject", subject.equals("全部")?"*":subject);
		map.put("fromUniversity", university);
		map.put("start",start+"");
		HttpsUtils.Post(host+"/books/booklist", map, new Callback.CommonCallback<List<BookData>>(){

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
			public void onSuccess(List<BookData> result) {
				handler.obtainMessage(EventName.UI.SUCCESS,result).sendToTarget();
			}

		});
	}

	private void visitUserInfo(){
		HttpsUtils.Get(host+"/users/userbaseinfo", null, new Callback.CommonCallback<UserInfo>(){

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
		HttpsUtils.Get(host+"/users/userelseinfo", null, new Callback.CommonCallback<UserPlus>(){

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
		HttpsUtils.Get(host+"/users/getrank", null, new Callback.CommonCallback<String>(){

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
			public void onSuccess(String result) {
				// TODO Auto-generated method stub
				if(result != null){
					UserManagerFunc.getInstance().setRank(Integer.parseInt(result));
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
		HttpsUtils.Get(host+"/users/usersbyorder",null, new Callback.CommonCallback<List<UserRank>>(){

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
			public void onSuccess(List<UserRank> result) {
				handler.obtainMessage(EventName.UI.SUCCESS,result).sendToTarget();
			}

		});
	}

	public void download(final String id, final String uid, final String bookName) throws DbException {
		if(!isNetworkConnected()){
			ItOneUtils.showToast(context,"网络未连接");
			return;
		}
		DownloadManagerFunc.getInstance().startDownload(
				host+"/books/download", id,uid,bookName,
				BookManagerFunc.FILEPATH + bookName, true, false, null);
	}

	public void commitIdea(String advice){
        if(!isNetworkConnected()){
            ItOneUtils.showToast(context,"网络未连接");
            return;
        }
        handler.sendEmptyMessage(EventName.UI.START);
        Map<String,String> map=new HashMap<>();
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
		Map<String,String> map=new HashMap<>();
		String id = UserManagerFunc.getInstance().isLogin()?
				UserManagerFunc.getInstance().getUserInfo().id:null;
		map.put("id", id);
		map.put("date", date);
		HttpsUtils.Post(host+"/message/getMessage", map, new Callback.CommonCallback<List<com.wangyi.define.bean.Message>>(){

			@Override
			public void onCancelled(CancelledException arg0) {}

			@Override
			public void onError(Throwable ex, boolean isCheck) {}

			@Override
			public void onFinished() {if(handler != null) handler.sendEmptyMessage(EventName.UI.FINISH);}

			@Override
			public void onSuccess(List<com.wangyi.define.bean.Message> result) {
				// TODO Auto-generated method stub
				MessageFunc.getInstance().addMessages(result);
			}

		});
	}

	public void getHomework(){
		if(!isNetworkConnected()){
			ItOneUtils.showToast(context,"网络未连接");
			return;
		}
		if(handler != null) handler.sendEmptyMessage(EventName.UI.START);
		Map<String,String> map=new HashMap<>();
		String id = UserManagerFunc.getInstance().isLogin()?
				UserManagerFunc.getInstance().getUserInfo().id:null;
		map.put("id", id);
		HttpsUtils.Post(host+"/homework/getHomework", map, new Callback.CommonCallback<List<Homework>>(){

			@Override
			public void onCancelled(CancelledException arg0) {
			}
			@Override
			public void onError(Throwable ex, boolean isCheck) {
			}

			@Override
			public void onFinished() {
				if(handler != null) handler.sendEmptyMessage(EventName.UI.FINISH);
			}

			@Override
			public void onSuccess(List<Homework> result) {
				// TODO Auto-generated method stub
				HomeworkFunc.getInstance().addHomeworks(result);
			}

		});
	}

	public void getUniversityList(){
		if(!isNetworkConnected()){
			ItOneUtils.showToast(context,"网络未连接");
			return;
		}
		if(handler != null) handler.sendEmptyMessage(EventName.UI.START);
		HttpsUtils.Get(host+"/base/university", null, new Callback.CommonCallback<List<University>>(){

			@Override
			public void onCancelled(CancelledException arg0) {
			}
			@Override
			public void onError(Throwable ex, boolean isCheck) {
			}

			@Override
			public void onFinished() {
				if(handler != null) handler.sendEmptyMessage(EventName.UI.FINISH);
			}

			@Override
			public void onSuccess(List<University> result) {
				// TODO Auto-generated method stub
				ArrayList<String> temp = new ArrayList<String>();
				for(University info:result){
					temp.add(info.name);
				}
				handler.obtainMessage(EventName.UI.SUCCESS,temp).sendToTarget();
			}

		});
	}

	public void getClassList(String fromUniversity){
		if(!isNetworkConnected()){
			ItOneUtils.showToast(context,"网络未连接");
			return;
		}
		if(handler != null) handler.sendEmptyMessage(EventName.UI.START);
		Map<String,String> map=new HashMap<>();
		map.put("fromUniversity", fromUniversity);
		HttpsUtils.Post(host+"/base/class", map, new Callback.CommonCallback<List<ClassInfo>>(){

			@Override
			public void onCancelled(CancelledException arg0) {
			}
			@Override
			public void onError(Throwable ex, boolean isCheck) {
				ItOneUtils.showToast(context,ex.toString());
			}

			@Override
			public void onFinished() {
				if(handler != null) handler.sendEmptyMessage(EventName.UI.FINISH);
			}

			@Override
			public void onSuccess(List<ClassInfo> result) {
				// TODO Auto-generated method stub
				ArrayList<String> temp = new ArrayList<String>();
				for(ClassInfo info:result){
					temp.add(ItOneUtils.generateMessage(info.name,info.fromFaculty));
				}
				handler.obtainMessage(EventName.UI.SUCCESS,temp).sendToTarget();
			}

		});
	}

	public void SMS(String mob){
		if(!isNetworkConnected()){
			ItOneUtils.showToast(context,"网络未连接");
			return;
		}
		ItOneUtils.showToast(context,"发送短信");
		Map<String,String> map=new HashMap<>();
		map.put("mob",mob);
		HttpsUtils.Post(host+"/base/sms", map, new Callback.CommonCallback<String>(){

			@Override
			public void onCancelled(CancelledException arg0) {
			}
			@Override
			public void onError(Throwable ex, boolean isCheck) {
			}

			@Override
			public void onFinished() {}

			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub
				handler.obtainMessage(EventName.UI.SUCCESS+7,result).sendToTarget();
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
