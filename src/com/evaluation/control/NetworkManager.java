package com.evaluation.control;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.entity.mime.*;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class NetworkManager {
	//private String url = "http://192.168.0.17:8080/EvaluationService/testLogin/login!json.action";
	private HttpClient client;
	private HttpGet getRequest;
	private HttpPost request;
	private HttpResponse response;
	private String TAG = "effort";
	
	public NetworkManager() {
		getRequest = new HttpGet();
		request = new HttpPost();
	}
	public void open() {
		client = new DefaultHttpClient();
	}
	public String executeGet(String url) throws Exception {  
        //BufferedReader in = null;  
        String out = null;
            // 定义HttpClient  
            //HttpClient client = new DefaultHttpClient();  
            // 实例化HTTP方法  
        if(isConnect(url)) {
//            getRequest.setURI(new URI(url));  
//            response = client.execute(getRequest);  
            HttpEntity entity = response.getEntity();    
            if (entity != null) {    
                out = EntityUtils.toString(entity, "UTF-8");    
                Log.i(TAG, out);
            }
        }
        return out;
    }
	
	public InputStream executeGetStream(String url) throws Exception {  
        //BufferedReader in = null;  
		open();
        InputStream is = null;
        // 定义HttpClient    
        // 实例化HTTP方法   
        getRequest.setURI(new URI(url));  
        response = client.execute(getRequest);  
        is = response.getEntity().getContent();
        return is;
    }
	
	public String getValue(String url) {
		if(isConnect(url)) {
			try{
				HttpEntity entity = response.getEntity();    
	            if (entity != null) {    
	                String out = EntityUtils.toString(entity, "UTF-8");    
	                Log.i(TAG, out);
	                return out;
	            }
			}catch(Exception e) {
				Log.e(TAG, e.getMessage());
			}
		}
		return null;
	}
	public boolean isConnect(String url) {
		try {
			open();
			getRequest.setURI(new URI(url));
			response = client.execute(getRequest);
			if (response.getStatusLine().getStatusCode() == 200) { //200表示请求成功    
				return true;
			}
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
		return false;
	}
	public void addFile(String key, String filePath, String type) {
		File file = new File(filePath);
		ContentBody cbFileData = new FileBody(file, type);
		//postEntity.addPart(key, cbFileData);
	}
	public boolean postData(Map<String, String> data, String url) {
		ContentBody cb = null;
		MultipartEntity postEntity = new MultipartEntity();
		try {
//			Set<String> set = data.keySet();
//			for(String key : set){
//				cb = new StringBody(data.get(key));
//				postEntity.addPart(key, cb);
//			}
			Log.e(TAG, url);
			open();
			request.setURI(new URI(url));
			//request.setEntity(postEntity);
			response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == 201) { //200表示请求成功    
				return true;
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return false;
	}
	public boolean getContent() {
		try {
			InputStream reader = response.getEntity().getContent();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	public void close() {
		if(client != null)
			client.getConnectionManager().shutdown();
	}
}
