package com.johnfuetsch.android.twitter.controllers;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.johnfuetsch.android.twitter.TwitterClientApp;
import com.johnfuetsch.android.twitter.adapters.TweetsAdapter;
import com.johnfuetsch.android.twitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public abstract class BaseTimelineController {

	protected TweetsAdapter tweetsAdapter;
	protected boolean busy = false;
	protected Context mContext;
	protected TimelineControllerCallback mCallback;

	public BaseTimelineController(Context context, TimelineControllerCallback callback) {
		mContext = context;
		mCallback = callback;
		ArrayList<Tweet> tweets = new ArrayList<Tweet>();
		tweetsAdapter = new TweetsAdapter(mContext, tweets);
	}
	
	public interface TimelineControllerCallback {
		public void onBusyStart();
		public void onBusyEnd();
		public void onError(JSONObject error);
	}

	public TweetsAdapter getAdapter() {
		return tweetsAdapter;
	}
	
	protected void loadData(String sinceId, String maxId, int insertPosition) {

		if (!getNetworkLock())
			return;

		TwitterClientApp.getRestClient().getHomeTimeline(sinceId, maxId,
				new TweetListResponseHandler(insertPosition));
	}

	public boolean loadOlderData() {
		Tweet oldestTweet = tweetsAdapter.getItem(tweetsAdapter.getCount() - 1);
		String maxId = oldestTweet.id;
		loadData(null, maxId, tweetsAdapter.getCount());
		return true;
	}

	public void loadNewerData() {
		String latestId = null;
		if (tweetsAdapter.getCount() > 0) {
			Tweet latestTweet = tweetsAdapter.getItem(0);
			latestId = latestTweet.id;
		}
		loadData(latestId, null, 0);
	}

	public void loadInitialData() {
		loadNewerData();
	}

	public boolean fillInHoles(int minPosition, int maxPosition) {
		for (int i = minPosition; i < maxPosition; ++i) {
			Tweet tweet = (Tweet) tweetsAdapter.getItem(i);
			if (tweet.holeInData) {
				String olderTweetId = null;
				if (i + 1 < tweetsAdapter.getCount()) {
					Tweet olderTweet = (Tweet) tweetsAdapter.getItem(i + 1);
					olderTweetId = olderTweet.id;
				}
				loadData(olderTweetId, tweet.id, i + 1);
				return true;
			}
		}
		return false;
	}

	protected void insertTweets(ArrayList<Tweet> tweets, int position) {
		int i = 0;
		Tweet tweet = null;
		for (; i < tweets.size(); ++i) {
			tweet = tweets.get(i);
			tweetsAdapter.insert(tweet, position + i);
		}

		// If we get any results, then it's possible there are more to come.
		// Indicate this by setting the "holeInData" attribute on the last
		// tweet.
		if (tweet != null) {
			tweet.holeInData = true;
		}

		// Since we've successfully added data after maxId, that
		// Tweet no longer has a hole in the data
		if (position - 1 >= 0) {
			tweetsAdapter.getItem(position - 1).holeInData = false;
			tweetsAdapter.notifyDataSetChanged();
		}
	}

	protected boolean getNetworkLock() {
		if (busy) {
			Log.e("TweetListFragment", "Busy!");
			return false;
		} else {
			busy = true;
		}
		mCallback.onBusyStart();
		return true;
	}

	protected void returnNetworkLock() {
		busy = false;
		mCallback.onBusyEnd();
	}

	class TweetListResponseHandler extends JsonHttpResponseHandler {
		protected int position;

		TweetListResponseHandler(int position) {
			this.position = position;
		}

		@Override
		public void onSuccess(JSONArray jsonTweets) {

			ArrayList<Tweet> tweets = Tweet.fromJson(jsonTweets);

			insertTweets(tweets, position);

			returnNetworkLock();
		}

		@Override
		public void onFailure(Throwable e, JSONObject error) {
			super.onFailure(e, error);
			mCallback.onError(error);
			e.printStackTrace();
			returnNetworkLock();
		}

	}

	public void onPostTweet(Tweet tweet) {
		tweet.holeInData = true;
		tweetsAdapter.insert(tweet, 0);
	}

}