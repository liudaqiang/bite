package com.lq.bite.utils;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;

public class HttpUtil {
//	public static JSONObject doGet(String url) {
//		CloseableHttpClient httpclient = HttpClientBuilder.create().build();
//		HttpGet httpget = new HttpGet(url);
//		JSONObject jsonObj = null;
//		try {
//			HttpResponse response = httpclient.execute(httpget);
//			HttpEntity entity = response.getEntity();
//			if(entity!=null) {
//				String result = EntityUtils.toString(entity,"UTF-8");
//				jsonObj = JSONObject.fromObject(result);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		return jsonObj;
//	}
	
	public static String doPost(String url,String json) {
		CloseableHttpClient httpclient = HttpClientBuilder.create().build();
		HttpPost httpPost = new HttpPost(url);
		try {
			StringEntity entityStr = new StringEntity(json, ContentType.APPLICATION_JSON);
			httpPost.setEntity(entityStr);
			HttpResponse response = httpclient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			if(entity!=null) {
				String result = EntityUtils.toString(entity, "UTF-8");
				return result;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
