package com.example.gt_coc_studentevents;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonParser {

	static private String result = null;
	static private InputStream is = null;
	static private StringBuilder sb = null;
	//private static List nameValuePairs;
	static public ArrayList<EventListing> eventList = new ArrayList<EventListing>();
	
	private static void httpPost() throws IOException {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("http://gruesomevisage.net/query.php");
		//httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		HttpResponse response = httpclient.execute(httppost);
		HttpEntity entity = response.getEntity();
		is = entity.getContent();
	}
	
	private static void responseToString() throws IOException  {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
		sb = new StringBuilder();
		sb.append(reader.readLine() + "\n");
		String line ="0";
		
		while ((line = reader.readLine()) != null) {
			sb.append(line + "\n");
		}
		
		is.close();
		result=sb.toString();
		
	}
	
	private static void pairData() throws JSONException {
		
		
		JSONArray jArray;
		
		//System.out.println("The result is " + result);
		
			jArray = new JSONArray(result);
			JSONObject json_data = null;
			for(int i=0;i<jArray.length();i++) {
				json_data = jArray.getJSONObject(i);
				eventList.add(new EventListing(
						json_data.getString("eventName"),
						json_data.getString("location"),
						json_data.getString("time"),
						json_data.getString("description")
						));
			}
		
	}
	
	public static ArrayList<EventListing> getList() throws Exception {
		httpPost();
		responseToString();
		pairData();
		return eventList;
	}
	
	public static void main(String[] args){
		
		try {
			ArrayList<EventListing> elist = getList();
			System.out.println(elist.size());
			 for (int i =0;i<elist.size();i++) {
				 System.out.println(
						 elist.get(i).getEventName() + "\n" + 
						 elist.get(i).getTime() + "\n" +
						 elist.get(i).getLocation() + "\n" +
						 elist.get(i).getDescription());
			 }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("You have died from being hammered in the ass.");
		}
	}
}
