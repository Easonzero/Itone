package com.wangyi.view.fragment;

import java.util.ArrayList;
import java.util.Random;

import org.xutils.view.annotation.*;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wangyi.adapter.DLBookListAdapter;
import com.wangyi.define.BookData;
import com.wangyi.define.UserInfo;
import com.wangyi.imp.database.DBBook;
import com.wangyi.service.DownloadService;
import com.wangyi.utils.MyHttps;
import com.wangyi.utils.PreferencesReader;
import com.wangyi.view.BaseFragment;
import com.wangyi.widget.KeywordsFlow;
import com.wangyi.widget.LessonListLayout;
import com.wangyi.reader.R;

@ContentView(R.layout.fragment_allsubject)
public class AllSubjectFragment extends BaseFragment {
	
	private Context context;
	private Activity act;
	private SensorManager sensorManager; 
    private Vibrator vibrator; 
	ArrayList<BookData> books;
	private String URL = "/ItOne/GetBookList";
	private String URLbook = "/ItOne/GetBook";
	public String[] keywords = {"工科数学","大学物理","大学英语","俄语","离散数学",
			"西方建筑史","茶文化欣赏","音乐鉴赏","思修","马哲","形式与政治",
			"交流技巧","数字逻辑","matlab","博弈论","西方文学","欧洲史",
			"西方美术史","人机交互"};
	
	@ViewInject(R.id.keywordsflow)
	private KeywordsFlow keywordsFlow;
	@ViewInject(R.id.search_edit)
	private EditText editSearch;
	@ViewInject(R.id.lessons)
	private LessonListLayout lessons;
	@ViewInject(R.id.booklist)
	private ListView bookList;

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState){
		act = this.getActivity();
		context = this.getActivity().getApplicationContext();
		
		sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE); 
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		
		initEditSearch();
		initLessons();
	}
	
	@Event(value=R.id.booklist,type=ListView.OnItemClickListener.class)
	private void onListItemClick(AdapterView<?> adapter, View view, int pos,
			long arg3){
		BookData book = (BookData)adapter.getItemAtPosition(pos);
		if(isNetworkConnected(context)){
			if(UserInfo.sessionId != null){
				Toast.makeText(context, book.bookName + "开始下载...", 3000).show();
				PreferencesReader.saveDownloadList(act, book);
				DBBook db = new DBBook(context).open();
				db.addBook(book.bookName, book.subject, book.occupation, book.fromUniversity, book.downloadNumber, book.count, book.fileLength, book.url);
				db.close();
				Intent intent = new Intent(context,DownloadService.class);
				intent.putExtra("book_url", book.url);
				intent.putExtra("book_name",book.bookName);
				context.startService(intent);
			}
			else{
				Toast.makeText(context, "请先登录", 3000).show();
			}
		}
		else{
			Toast.makeText(context, "网络未连接", 3000).show();
		}
	}
	
	@Event(R.id.keywordsflow)
	private void onKeywordsClick(View view){
		editSearch.clearFocus();
	}
	
	@Event(value=R.id.search_edit,type=View.OnFocusChangeListener.class)
	private void onEditSearchFocusChange(View view, boolean is){
		if(is){
			keywordsFlow.setVisibility(View.VISIBLE);
			keywordsFlow.setDuration(800l);  
			keywordsFlow.rubKeywords();  
			feedKeywordsFlow(keywordsFlow, keywords);  
			keywordsFlow.go2Show(KeywordsFlow.ANIMATION_OUT);
			if (sensorManager != null) {
	            sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
	        }
		}
		else{
			keywordsFlow.setVisibility(View.GONE);
			if (sensorManager != null) {
	            sensorManager.unregisterListener(sensorEventListener); 
	        } 
			editSearch.setText("");
		}
	}
	
	private void initLessons(){
		lessons.addLessons(keywords,new OnClickListener(){

			@Override
			public void onClick(final View view) {
				// TODO Auto-generated method stub
				if(lessons.obj != null){
					((TextView)lessons.obj).setBackground(null);
					((TextView)lessons.obj).setPadding(0, 0, 0, 0);
				}
				view.setBackgroundResource(R.drawable.bg_class);
				((TextView)view).setPadding(0, 0, 0, 0);
				lessons.obj = view;
				if(isNetworkConnected(context)){
					new Thread(){
						public void run(){
							MyHttps mHttps = new MyHttps(URL);
							Message msg = new Message();
							
							ArrayList<BookData> books = new ArrayList<BookData>();
							mHttps.getBooksDate(((TextView)view).getText().toString(),books);

							msg.obj = books;
							msg.what = 2;
							handler.sendMessage(msg);
						}
					}.start();
				}
				else{
					handler.sendEmptyMessage(1);
				}
			}
			
		});
	}
	
	private void initEditSearch(){
		editSearch.addTextChangedListener( new TextWatcher() {  
            
	          @Override  
	          public void onTextChanged(CharSequence s, int start, int before, int count) {
	        	  if(s.toString().equals("")&&start - before != 0){
	        		  keywordsFlow.setVisibility(View.VISIBLE);
	  				  keywordsFlow.setDuration(800l);  
	  				  keywordsFlow.rubKeywords();  
	  				  feedKeywordsFlow(keywordsFlow, keywords);  
	  				  keywordsFlow.go2Show(KeywordsFlow.ANIMATION_OUT);
	  				  sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
	  				  
	        	  }
	        	  else{
	        		  if (sensorManager != null) {
	        	            sensorManager.unregisterListener(sensorEventListener); 
	        	      } 
	        		  keywordsFlow.setVisibility(View.GONE);
	        	  }
	          }  
	            
	          @Override  
	          public void beforeTextChanged(CharSequence s, int start, int count, int after) {  
	        	  
	          }  
	            
	          @Override  
	          public void afterTextChanged(final Editable s) { 
	        	  new Thread(){
	        		  public void run(){
	        			  MyHttps mh = new MyHttps(URLbook);
	        			  ArrayList<BookData> books = mh.getBookDate(s.toString());
	        			  Message msg = new Message();
	        			  msg.obj = books;
	        			  msg.what = 2;
	        			  handler.sendMessage(msg);
	        		  }
	        	  }.start();
	          }  
	        });
	}

	
	private static void feedKeywordsFlow(KeywordsFlow keywordsFlow, String[] arr) {  
        Random random = new Random();  
        for (int i = 0; i < KeywordsFlow.MAX; i++) {  
            int ran = random.nextInt(arr.length);  
            String tmp = arr[ran];  
            keywordsFlow.feedKeyword(tmp);  
        }  
    }
	
	private Handler handler = new Handler() { 
		 
        @Override 
        public void handleMessage(Message msg) { 
            super.handleMessage(msg); 
            switch (msg.what) { 
            case 0:
            	keywordsFlow.setDuration(800l);  
				keywordsFlow.rubKeywords();  
				feedKeywordsFlow(keywordsFlow, keywords);  
				keywordsFlow.go2Show(KeywordsFlow.ANIMATION_OUT);
                break; 
            case 1:
            	Toast.makeText(context,"服务器抽风中...",5000).show();
            	break;
            case 2:
            	ArrayList<BookData> books = (ArrayList<BookData>)msg.obj;
				if(books != null){
					DLBookListAdapter adapter = new DLBookListAdapter(context,books);
					bookList.setAdapter(adapter);
				}
				else{
					handler.sendEmptyMessage(1);
				}
				break;
            } 
        } 
 
    };
    
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
	
	private SensorEventListener sensorEventListener = new SensorEventListener() { 
		 
        @Override 
        public void onSensorChanged(SensorEvent event) {
            float[] values = event.values; 
            float x = values[0];
            float y = values[1];
            float z = values[2];
            int medumValue = 19;
            if (Math.abs(x) > medumValue || Math.abs(y) > medumValue || Math.abs(z) > medumValue) { 
                vibrator.vibrate(200); 
                Message msg = new Message(); 
                msg.what = 0; 
                handler.sendMessage(msg); 
            }
        } 

		@Override
		public void onAccuracyChanged(Sensor arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}
    };
}
