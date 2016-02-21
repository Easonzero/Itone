package com.wangyi.utils;

public class MyHttps {
	/*private DefaultHttpClient client;
	private HttpPost post;
	private HttpGet get;
	private String url;
	private static String IP = "http://192.168.0.102:8080";
	public MyHttps(String url){
		client = new DefaultHttpClient();
		HttpParams params = null;  
        params = client.getParams();
        HttpConnectionParams.setConnectionTimeout(params, 5000);  
        HttpConnectionParams.setSoTimeout(params, 35000);
		this.url = IP + url;
		post = new HttpPost(this.url);
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
	
	public ArrayList<BookData> getBookDate(String bookName){
		try {
			ArrayList<BookData> books = new ArrayList();
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
					BookData book = new BookData();
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
	
	public void getBooksDate(String subject,ArrayList<BookData> books){
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
					BookData book = new BookData();
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
	}*/
 }

