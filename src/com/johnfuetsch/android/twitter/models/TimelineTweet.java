package com.johnfuetsch.android.twitter.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.annotation.Column.ConflictAction;
import com.activeandroid.query.Select;

@Table(name = "timeline_tweet")
public class TimelineTweet extends Model implements Serializable {

	private static final long serialVersionUID = 7648726738367192168L;

	@Column(name = "tweet_id", uniqueGroups = { "group1" }, onUniqueConflicts = { ConflictAction.REPLACE })
	public String tweet_id;
	private Tweet tweet;

	@Column(name = "timeline_id", uniqueGroups = { "group1" }, onUniqueConflicts = { ConflictAction.REPLACE })
	public String timelineId;
	/*
	 * When true, indicates that there are tweets after this in the timeline
	 */
	@Column(name = "hole_in_data")
	public boolean holeInData;

	public Tweet getTweet() {
		if (tweet == null) {
			tweet = Tweet.fromId(tweet_id);
		}
		return tweet;
	}

	public TimelineTweet() {
	}

	public TimelineTweet(Tweet tweet, String timelineId) {
		this.tweet = tweet;
		this.tweet_id = tweet.id;
		this.timelineId = timelineId;
		this.holeInData = false;
	}

	public static ArrayList<TimelineTweet> fromJson(JSONArray jsonArray,
			String timelineId) {

		ArrayList<TimelineTweet> tTweets = new ArrayList<TimelineTweet>(
				jsonArray.length());

		// Process each result in json array, decode and convert to a
		// TimelineTweet and corresponding Tweet object
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

	public static List<TimelineTweet> getRecent(int count, String timeline_id) {
		return new Select().from(TimelineTweet.class)
				.where("timeline_id = ?", timeline_id).orderBy("id ASC")
				.limit(count).execute();
	}
}
