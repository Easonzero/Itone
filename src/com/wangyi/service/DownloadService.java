package com.wangyi.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import com.wangyi.define.BookData;
import com.wangyi.define.UserInfo;
import com.wangyi.imp.database.DBBook;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class DownloadService extends IntentService {
	
	private Handler handler = new Handler();
	private DefaultHttpClient client;
	private HttpPost post;
	private BookData book;
	private static String IP = "http://192.168.0.106:8080";
	private long count = 0;
	private long fileLength;
	private static final int INIT = 1;
    private static final int DOWNLOADING = 2;
    private static final int PAUSE = 3;
    private int state = INIT;
    
	private class mBroadcastReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			int flag = intent.getIntExtra("flag", INIT);
			if(flag == PAUSE){
				pause();
			}
		}
    
    };
   
    private mBroadcastReceiver mbr;

	public DownloadService(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	public DownloadService() {
		super("download");
		this.setIntentRedelivery(true);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(final Intent intent) {
		// TODO Auto-generated method stub
		String url = intent.getStringExtra("book_url");
		String name = intent.getStringExtra("book_name");
		long count = intent.getLongExtra("count",0);
		state = intent.getIntExtra("flag",INIT);
		book = new BookData();
		book.bookName = name;
		book.url = url;
		book.count = count;
		downloadBook(book);
	}
	
	public void onDestroy() {
	    handler.removeCallbacks(run);
	    super.onDestroy();
	}
	
	public void downloadBook(BookData book){
		client = new DefaultHttpClient();
		HttpParams params = null;  
        params = client.getParams();
        HttpConnectionParams.setConnectionTimeout(params, 5000);  
        HttpConnectionParams.setSoTimeout(params, 35000);
		String url = IP + "/ItOne/DownloadBook";
		post = new HttpPost(url);
		try {
			List<NameValuePair> postdate = new ArrayList();
			postdate.add(new BasicNameValuePair("book_url",book.url));
			postdate.add(new BasicNameValuePair("book_count",String.valueOf(book.count)));
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(postdate,HTTP.UTF_8);
			post.setEntity(entity); 
			HttpResponse response = client.execute(post);
			if(response.getStatusLine().getStatusCode() == 200){
				InputStream inputStream = null;
				inputStream = response.getEntity().getContent();
				fileLength = Integer.valueOf(response.getFirstHeader("Content-Length").getValue().toString());
				
				File bookf = new File("/sdcard/textBook/"+book.bookName+".pdf");
				RandomAccessFile accessFile = new RandomAccessFile(bookf, "rwd");
				
				if(state == INIT){
					bookf.createNewFile();
					accessFile.setLength(fileLength);
				}
				
				accessFile.seek(book.count);
				
				handler.post(run);
				byte[] buffer = new byte[1024];
		        int n = 0;
		        while (0 != (n = inputStream.read(buffer))) {
		        	accessFile.write(buffer, 0, n);
		            count += n;
		            if (state == PAUSE) {
		            	DBBook db = new DBBook(this).open();
						db.setBookCount(book.url, count);
						db.close();
		                return;
		            }
		        }
		        Intent intent = new Intent();
				intent.setAction("download");
				intent.putExtra("name", book.bookName);
				intent.putExtra("count",count);
				intent.putExtra("fileLength",fileLength);
				sendBroadcast(intent);
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}
	
	public void pause() {
        state = PAUSE;
    }

    public void download() {
        state = DOWNLOADING;
    }
	
	 private Runnable run = new Runnable() {
		 public void run() {
			Intent intent = new Intent();
			intent.setAction("download");
			intent.putExtra("name", book.bookName);
			intent.putExtra("count",count);
			intent.putExtra("fileLength",fileLength);
			sendBroadcast(intent);
		 	handler.postDelayed(run, 1000);
		 }
	};
	

}
