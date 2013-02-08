package com.example.mytwitter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

//import com.example.updatetextview.TextViewUpdate.updateDisplay;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TwitterFeed extends ListActivity {
	
	Integer count = 0;
	String fr = "fr";
	String es = "es";
	String de = "de";
	String ru = "ru";
	String zh = "zh";
	String ja = "ja";
	String pt = "pt";
	String ko = "ko";
	
	String french = "Français: ";
	String spanish = "Spanish: ";
	String german = "German: ";
	String russian = "Russian: ";
	String chinese = "Chinese: ";
	String japanese = "Japanese: ";
	String korean = "Korean: ";
	String portuguese = "Portuguese: ";
	
	String testString;
	String Translatable;
	
	
	private ArrayList<Tweet> tweets = new ArrayList<Tweet>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_twitter_feed);
		
		final Handler mHandler = new Handler(Looper.getMainLooper());
		
		TimerTask task = new TimerTask() {
            public void run() {
            	mHandler.post(new Runnable(){
            		public void run(){
            	//do {
                    
            		tweets.clear();	
                    new MyTask().execute();
                    
                    count += 1;
                //} while (count <= 100); 	
            	}});    
           }
            
        };
        Timer timer = new Timer();
        timer.schedule(task, 500, 50000);
		
		//new MyTask().execute();
		
		
	}
	
	private class MyTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progressDialog;
        protected void onPreExecute() {
                progressDialog = ProgressDialog.show(TwitterFeed.this,
                                  "", "Loading new Tweets. Please wait...", true);
        }
        @Override
        protected Void doInBackground(Void... arg0) {
                try {
                        HttpClient hc = new DefaultHttpClient();
                        HttpGet get = new
                        //HttpGet("http://search.twitter.com/search.json?q=android");
                        //HttpGet("http://search.twitter.com/search.json?q=from:@AdobeWEM");
                        //HttpGet("http://search.twitter.com/search.json?q=from:@Adobe");
                        HttpGet("http://search.twitter.com/search.json?q=#globmini");
                        //HttpGet("https://userstream.twitter.com/1.1/@AdobeWEM.json");
                        
                        //Streaming Example
                        //GET	https://userstream.twitter.com/1.1/user.AdobeWEM
                        
                        HttpResponse rp = hc.execute(get);
                        if(rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
                        {
                                String result = EntityUtils.toString(rp.getEntity());
                                JSONObject root = new JSONObject(result);
                                JSONArray sessions = root.getJSONArray("results");
                                Integer Max = sessions.length();
                                Integer Min = 0;
                                Random rand = new Random();
                                for (int i = 0; i < 2; i++) {
                                		Integer randomNumber = rand.nextInt(Max - Min + 1) + Min;
                                        JSONObject session = sessions.getJSONObject(randomNumber);
                                        Tweet tweet = new Tweet();
                                        Translatable = session.getString("text");
                                        Translatable = Html.fromHtml(Translatable).toString();
                                         tweet.content = "------TWEET------ " + Translatable + " ------ENDTWEET------";
                                         //tweet.content = session.getString("text");
                                         tweet.author = "AUTHOR " + session.getString("from_user");
                                         
                                         tweet.translationFR = postHTTP(Translatable,fr, french);
                                         tweet.translationES = postHTTP(Translatable,es, spanish);
                                         tweet.translationPT = postHTTP(Translatable,pt, portuguese);
                                         tweet.translationDE = postHTTP(Translatable,de, german);
                                         tweet.translationJA = postHTTP(Translatable,ja, japanese);
                                         tweet.translationZH = postHTTP(Translatable,zh, chinese);
                                         tweets.add(tweet);
                                }
                       }
               } catch (Exception e) {
                       Log.e("TwitterFeedActivity", "Error loading JSON", e);
               }
               return null;
  }
  @Override
  protected void onPostExecute(Void result) {
          progressDialog.dismiss();
          setListAdapter(new TweetListAdaptor(
                          TwitterFeed.this, R.layout.list_item, tweets));
  	}

}	
	private class TweetListAdaptor extends ArrayAdapter<Tweet> {
        private ArrayList<Tweet> tweets;
        public TweetListAdaptor(Context context,
                                    int textViewResourceId,
                                    ArrayList<Tweet> items) {
                 super(context, textViewResourceId, items);
                 this.tweets = items;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
        		System.out.println("Inside View getView");
                View v = convertView;
                if (v == null) {
                        LayoutInflater vi = (LayoutInflater) getSystemService                        
(Context.LAYOUT_INFLATER_SERVICE);
                        v = vi.inflate(R.layout.list_item, null);
                }
                Tweet o = tweets.get(position);
                TextView tt = (TextView) v.findViewById(R.id.toptext);
                TextView bt = (TextView) v.findViewById(R.id.bottomtext);
                TextView transFR = (TextView) v.findViewById(R.id.translatedTextFR);
                TextView transES = (TextView) v.findViewById(R.id.translatedTextES);
                TextView transPT = (TextView) v.findViewById(R.id.translatedTextPT);
                TextView transDE = (TextView) v.findViewById(R.id.translatedTextDE);
                TextView transJA = (TextView) v.findViewById(R.id.translatedTextJA);
                TextView transZH = (TextView) v.findViewById(R.id.translatedTextZH);
                tt.setText(o.content);
                
                //trying to get marquee scrolling
                tt.setSelected(true);
                //scrolling working - looks pretty cool.
                
                bt.setText(o.author);
                
                //display translated string...
                transFR.setText(o.translationFR);
                transES.setText(o.translationES);
                transPT.setText(o.translationPT);
                transDE.setText(o.translationDE);
                transJA.setText(o.translationJA);
                transZH.setText(o.translationZH);
                return v;
        }
	
}
	
	public class Tweet {
	    String author;
	    String content;
	    StringBuilder translationFR;
	    StringBuilder translationES;
	    StringBuilder translationPT;
	    StringBuilder translationDE;
	    StringBuilder translationJA;
	    StringBuilder translationZH;
	}	

	
	public StringBuilder postHTTP (String...params){
		
		//translate variables
		//String postURLFR ="http://10.72.141.5:8080/sai?lp=en_fr&service=translate";
		String URLhead = "http://10.72.141.5:8080/sai?lp=en_";
		String URLtail = "&service=translate";
		//String targetLang;
		String TweetTranslate;
		
		StringBuilder responseTrans = null;
		
		//Dummy variables for testing
		Integer markerInt;
		StringBuilder testTweet = new StringBuilder();
		
			String postURL = URLhead + params[1] + URLtail;
			
			
			HttpClient httpclient = new DefaultHttpClient();
		    HttpPost httppost = new HttpPost(postURL);
	    	
		    TweetTranslate = params[0];
		    TweetTranslate = Html.fromHtml(TweetTranslate).toString();
		    System.out.println("Tweet is: "+ TweetTranslate);
		        
		    
		    try {
		        // Trying list (SUCESS WITH STRING ENTITY!)
		        StringEntity singleEntryList = new StringEntity(TweetTranslate);
		        httppost.setEntity(singleEntryList);
		        
		        //DummyTestingStringCode
		    	//Random randomN = new Random();
		    	//markerInt = randomN.nextInt(30 - 0 + 1);			    	
		    	//testTweet.append(String.valueOf(markerInt));
		    	//testTweet.append(TweetTranslate);		    	
		    	//System.out.println(testTweet);
		    	
		        // Execute HTTP Post Request
		        HttpResponse response = httpclient.execute(httppost);
		        //testString=responseTrans.toString();
		        
		        responseTrans = (inputStreamToString(response.getEntity().getContent()));
		     		       
		        responseTrans.delete(0, 5);
		        
		        responseTrans.insert(0, params[2]);
		        
		    } catch (ClientProtocolException e) {
		        // TODO Auto-generated catch block
		    } catch (IOException e) {
		        // TODO Auto-generated catch block
		    }
		    	return responseTrans;
		    
		    //return testTweet;
		    //}
		   // return "Success!";
		
		
	
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



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_twitter_feed, menu);
		return true;
	}

}

