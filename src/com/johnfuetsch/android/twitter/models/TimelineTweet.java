package com.johnfuetsch.android.twitter.models;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class TimelineTweet {

	public Tweet tweet;
	public String timelineId;
	/*
	 * When true, indicates that there are tweets after this in the timeline
	 */
	public boolean holeInData;

	public TimelineTweet(Tweet tweet, String timelineId) {
		this.tweet = tweet;
		this.timelineId = timelineId;
		this.holeInData = false;
	}

	public static ArrayList<TimelineTweet> fromJson(JSONArray jsonArray, String timelineId) {

		ArrayList<TimelineTweet> tTweets = new ArrayList<TimelineTweet>(jsonArray.length());

		// Process each result in json array, decode and convert to a TimelineTweet and corresponding Tweet object
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject tweetJson = null;
			try {
				tweetJson = jsonArray.getJSONObject(i);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}

			Tweet tweet = Tweet.fromJson(tweetJson);
			TimelineTweet tTweet = new TimelineTweet(tweet, timelineId);
			if (tweet != null) {
				tTweets.add(tTweet);
			}
		}

		return tTweets;
	}
	
}
