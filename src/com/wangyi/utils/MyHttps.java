package com.wangyi.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject; 

import com.wangyi.activity.LoginActivity;
import com.wangyi.database.BookDate;
import com.wangyi.database.UserInfo;

public class MyHttps {
	private DefaultHttpClient client;
	private HttpPost post;
	private HttpGet get;
	private String url;
	private static String IP = "http://192.168.0.106:8080";
	public MyHttps(String url){
		client = new DefaultHttpClient();
		HttpParams params = null;  
        params = client.getParams();
        HttpConnectionParams.setConnectionTimeout(params, 5000);  
        HttpConnectionParams.setSoTimeout(params, 35000);
		this.url = IP + url;
		post = new HttpPost(this.url);
	}
	
	public HttpResponse postLogin(List<BasicNameValuePair> postData){
		UrlEncodedFormEntity entity;
		try {
			entity = new UrlEncodedFormEntity(postData,HTTP.UTF_8);
			post.setEntity(entity); 
			HttpResponse response = client.execute(post);
			return response;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("deprecation")
	public HttpResponse postForm(Map<String, String> userInfo, File file)
	{
		MyMultipartEntity entity;
		try {
			entity = new MyMultipartEntity();
			for (Map.Entry<String, String> entry : userInfo.entrySet()) {
	            entity.addStringPart(entry.getKey(),entry.getValue());
	        }
			if (file != null && file.exists()) {
				entity.addFilePart("headPicFile", file);
			}
			entity.writeTo(entity.mOutputStream);
			post.setEntity(entity);
			HttpResponse response;
			response = client.execute(post);
			return response;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean isLogin(String sessionId,UserInfo userInfo){
		if(sessionId == null){
			return false;
		}
		try {
			String sessionID = "JSESSIONID="+sessionId;
			post.setHeader("Cookie", sessionID);
			HttpResponse response = client.execute(post);
			StringBuilder json = new StringBuilder();
			InputStreamReader inputStreamReader = new InputStreamReader(response.getEntity().getContent());
			BufferedReader bufferReader = new BufferedReader(inputStreamReader);
			for(String s = bufferReader.readLine();s != null;s = bufferReader.readLine()){
				json.append(s);
			}
			try {
				JSONObject userJson = new JSONObject(json.toString());
				userInfo.userName = (String) userJson.get("userName");
				userInfo.city = (String) userJson.getString("city");
				userInfo.faculty = (String) userJson.getString("faculty");
				userInfo.imageUrl = (String) userJson.getString("imageUrl");
				userInfo.occupation = (String) userJson.getString("occupation");
				userInfo.province = (String) userJson.getString("province");
				userInfo.university = (String) userJson.getString("university");
				return true;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public Bitmap getImage(String sessionId){
		if(sessionId == null){
			return null;
		}
		try {
			String sessionID = "JSESSIONID="+sessionId;
			post.setHeader("Cookie", sessionID);
			HttpResponse response = client.execute(post);
			InputStream inputStream = null;
			inputStream = response.getEntity().getContent();
			BufferedInputStream fit = new BufferedInputStream(inputStream);
			return BitmapFactory.decodeStream(fit);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<BookDate> getBookDate(String bookName){
		try {
			ArrayList<BookDate> books = new ArrayList();
			List<NameValuePair> postdate = new ArrayList();
			postdate.add(new BasicNameValuePair("bookName",bookName));
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(postdate,HTTP.UTF_8);
			post.setEntity(entity); 
			HttpResponse response = client.execute(post);
			StringBuilder json = new StringBuilder();
			InputStreamReader inputStreamReader = new InputStreamReader(response.getEntity().getContent());
			BufferedReader bufferReader = new BufferedReader(inputStreamReader);
			for(String s = bufferReader.readLine();s != null;s = bufferReader.readLine()){
				json.append(s);
			}
				JSONObject multiJSON = new JSONObject(json.toString());
				JSONArray booksJSON = multiJSON.getJSONArray("books");
				for(int i = 0;i < booksJSON.length();i++){
					JSONObject bookJSON = booksJSON.getJSONObject(i);
					BookDate book = new BookDate();
					book.bookName = bookJSON.getString("bookName");
					book.subject = bookJSON.getString("subject");
					book.occupation = bookJSON.getString("occupation");
					book.fromUniversity = bookJSON.getString("fromUniversity");
					book.downloadNumber = bookJSON.getInt("downloadNumber");
					book.url = bookJSON.getString("url");
					books.add(book);
				}
				return books;
		}catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
		}
		return null;
	}
	
	public void getBooksDate(String subject,ArrayList<BookDate> books){
		try {
			List<NameValuePair> postdate = new ArrayList();
			postdate.add(new BasicNameValuePair("subject",subject));
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(postdate,HTTP.UTF_8);
			post.setEntity(entity); 
			HttpResponse response = client.execute(post);
			StringBuilder json = new StringBuilder();
			InputStreamReader inputStreamReader = new InputStreamReader(response.getEntity().getContent());
			BufferedReader bufferReader = new BufferedReader(inputStreamReader);
			for(String s = bufferReader.readLine();s != null;s = bufferReader.readLine()){
				json.append(s);
			}
				JSONObject multiJSON = new JSONObject(json.toString());
				JSONArray booksJSON = multiJSON.getJSONArray("books");
				for(int i = 0;i < booksJSON.length();i++){
					JSONObject bookJSON = booksJSON.getJSONObject(i);
					BookDate book = new BookDate();
					book.bookName = bookJSON.getString("bookName");
					book.subject = bookJSON.getString("subject");
					book.occupation = bookJSON.getString("occupation");
					book.fromUniversity = bookJSON.getString("fromUniversity");
					book.downloadNumber = bookJSON.getInt("downloadNumber");
					book.url = bookJSON.getString("url");
					books.add(book);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		catch (IOException e) {
			// TODO Auto-generated catch block
		}
	}
 }

