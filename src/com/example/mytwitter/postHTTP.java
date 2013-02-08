package com.example.mytwitter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.os.AsyncTask;

	
	


public class postHTTP extends AsyncTask<String,Integer,String>{
	
	//translate variables
	String postURLFR ="http://10.72.141.5:8080/sai?lp=en_fr&service=translate";
	List FRheadlines;
	String translateThis;
	String responseFR;
	StringBuilder responseTestFR;
	String FRString;
	StringBuilder FRTranslate;
	String TweetTranslate;
	
	
	
	protected void onPreExecute(){
		super.onPreExecute();
		
	}
	
	protected String doInBackground(String... params) {
		
		HttpClient httpclientFR = new DefaultHttpClient();
	    HttpPost httppostFR = new HttpPost(postURLFR);
    	
	    translateThis = TweetTranslate;
	    System.out.println(translateThis);
	        
	    
	    try {
	        // Trying list (SUCESS WITH STRING ENTITY!)
	        StringEntity singleEntryListFR = new StringEntity(translateThis);
	        httppostFR.setEntity(singleEntryListFR);
	        
	        
	        // Execute HTTP Post Request
	        HttpResponse responseFR = httpclientFR.execute(httppostFR);
	        responseTestFR = inputStreamToString(responseFR.getEntity().getContent());
	        
	        
	        // Set textView items for display
	        FRTranslate= responseTestFR;
	        System.out.println(responseTestFR);
	       
	       
	        
	    } catch (ClientProtocolException e) {
	        // TODO Auto-generated catch block
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	    }
	
	    
	    return "Success!";
	}
	
	@Override
	protected void onProgressUpdate(Integer...values) {
		//do something
	}
	
	@Override
	protected void onPostExecute(String result) {
		//do something
	}
	
	// Fast Implementation
		private StringBuilder inputStreamToString(InputStream is) throws IOException {
		    String line = "";
		    StringBuilder total = new StringBuilder();
			    
		    // Wrap a BufferedReader around the InputStream
		    BufferedReader rd = new BufferedReader(new InputStreamReader(is));

		    // Read response until the end
		    while ((line = rd.readLine()) != null) { 
		        total.append(line); 
		    }
			    
		    // Return full string
		    return total;
		}
}	
