package com.wangyi.view.fragment;

import java.util.ArrayList;
import java.util.Random;

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
import android.support.v4.app.Fragment;
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
import com.wangyi.utils.PreferencesItOne;
import com.wangyi.widget.KeywordsFlow;
import com.wangyi.widget.LessonListLayout;
import com.wangyi.reader.R;

public class AllSubjectFragment extends Fragment {
	
	public String[] keywords = {"������ѧ","��ѧ����","��ѧӢ��","����","��ɢ��ѧ",
			"��������ʷ","���Ļ�����","���ּ���","˼��","����","��ʽ������",
			"��������","�����߼�","matlab","������","������ѧ","ŷ��ʷ",
			"��������ʷ","�˻�����"};
	private KeywordsFlow keywordsFlow;
	private EditText editSearch;
	private Context context;
	private Activity act;
	private SensorManager sensorManager; 
    private Vibrator vibrator; 
	private LessonListLayout lessons;
	private ListView bookList;
	private String URL = "/ItOne/GetBookList";
	private String URLbook = "/ItOne/GetBook";
	ArrayList<BookData> books;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_allsubject, container, false);
		act = this.getActivity();
		context = this.getActivity().getApplicationContext();
		
		keywordsFlow = (KeywordsFlow) view.findViewById(R.id.keywordsflow);
		editSearch = (EditText) view.findViewById(R.id.search_edit);
		lessons = (LessonListLayout) view.findViewById(R.id.lessons);
		bookList = (ListView) view.findViewById(R.id.booklist);
		
		sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE); 
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		
		initEditSearch();
		initKeywordsFlow();
		initLessons();
		
		return view;
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
		
		bookList.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				BookData book = (BookData)adapter.getItemAtPosition(pos);
				if(isNetworkConnected(context)){
					if(UserInfo.sessionId != null){
						Toast.makeText(context, book.bookName + "��ʼ����...", 3000).show();
						PreferencesItOne.saveDownloadList(act, book);
						DBBook db = new DBBook(context).open();
						db.addBook(book.bookName, book.subject, book.occupation, book.fromUniversity, book.downloadNumber, book.count, book.fileLength, book.url);
						db.close();
						Intent intent = new Intent(context,DownloadService.class);
						intent.putExtra("book_url", book.url);
						intent.putExtra("book_name",book.bookName);
						context.startService(intent);
					}
					else{
						Toast.makeText(context, "���ȵ�¼", 3000).show();
					}
				}
				else{
					Toast.makeText(context, "����δ����", 3000).show();
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
	
	private void initKeywordsFlow(){
		
		keywordsFlow.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				editSearch.clearFocus();
			}
			
		});
		
		editSearch.setOnFocusChangeListener(new OnFocusChangeListener(){

			@Override
			public void onFocusChange(View view, boolean is) {
				// TODO Auto-generated method stub
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
            	Toast.makeText(context,"�����������...",5000).show();
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
