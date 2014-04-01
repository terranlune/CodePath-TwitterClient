package com.johnfuetsch.android.twitter.models;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Tweet {
	
	public String id;
	public String text;
	public User user;
	public long created;
	
	@SuppressWarnings("deprecation")
	public static Tweet fromJson(JSONObject jsonObject) {
		Tweet tweet = new Tweet();
		// Deserialize json into object fields
		try {
			tweet.id = jsonObject.getString("id_str");
			tweet.text = jsonObject.getString("text");
			tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
			tweet.created = Date.parse(jsonObject.getString("created_at"));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		// Return new object
		return tweet;
	}
	
	  public static ArrayList<Tweet> fromJson(JSONArray jsonArray) {
		  
	      ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());
	      
	      // Process each result in json array, decode and convert to tweet object
	      for (int i=0; i < jsonArray.length(); i++) {
	          JSONObject tweetJson = null;
	          try {
	            tweetJson = jsonArray.getJSONObject(i);
	          } catch (Exception e) {
	              e.printStackTrace();
	              continue;
	          }

	          Tweet tweet = Tweet.fromJson(tweetJson);
	          if (tweet != null) {
	            tweets.add(tweet);
	          }
	      }

	      return tweets;
	  }

}
