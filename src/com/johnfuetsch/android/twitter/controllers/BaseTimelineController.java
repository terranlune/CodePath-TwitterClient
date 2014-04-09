package com.johnfuetsch.android.twitter.controllers;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.johnfuetsch.android.twitter.TwitterClientApp;
import com.johnfuetsch.android.twitter.adapters.TimelineTweetsAdapter;
import com.johnfuetsch.android.twitter.models.TimelineTweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public abstract class BaseTimelineController {

	protected TimelineTweetsAdapter tTweetsAdapter;
	protected boolean busy = false;
	protected Context mContext;
	protected TimelineControllerCallback mCallback;

	public BaseTimelineController(Context context,
			TimelineControllerCallback callback) {
		mContext = context;
		mCallback = callback;
		ArrayList<TimelineTweet> tTweets = new ArrayList<TimelineTweet>();
		tTweetsAdapter = new TimelineTweetsAdapter(mContext, tTweets);
	}

	public interface TimelineControllerCallback {
		public void onBusyStart();

		public void onBusyEnd();

		public void onError(JSONObject error);
	}

	public TimelineTweetsAdapter getAdapter() {
		return tTweetsAdapter;
	}

	protected void loadData(String sinceId, String maxId, int insertPosition) {

		if (!getNetworkLock())
			return;

		TwitterClientApp.getRestClient().getHomeTimeline(sinceId, maxId,
				new TweetListResponseHandler(insertPosition));
	}

	public boolean loadOlderData() {
		TimelineTweet oldestTimelineTweet = tTweetsAdapter
				.getItem(tTweetsAdapter.getCount() - 1);
		String maxId = oldestTimelineTweet.getTweet().id;
		loadData(null, maxId, tTweetsAdapter.getCount());
		return true;
	}

	public void loadNewerData() {
		String latestId = null;
		if (tTweetsAdapter.getCount() > 0) {
			TimelineTweet latestTimelineTweet = tTweetsAdapter.getItem(0);
			latestId = latestTimelineTweet.getTweet().id;
		}
		loadData(latestId, null, 0);
	}

	public void loadInitialData() {
		retrieveData();
		loadNewerData();
	}

	public void retrieveData() {

		List<TimelineTweet> tTweets = TimelineTweet.getRecent(50,  getTimelineId());
		for (TimelineTweet tTweet : tTweets) {
			// Pre-fetch while we're still on the bg thread
			tTweet.getTweet().getUser();
		}
		tTweetsAdapter.addAll(tTweets);
	}

	public boolean fillInHoles(int minPosition, int maxPosition) {
		for (int i = minPosition; i < maxPosition; ++i) {
			TimelineTweet tTweet = (TimelineTweet) tTweetsAdapter.getItem(i);
			if (tTweet.holeInData) {
				String olderTweetId = null;
				if (i + 1 < tTweetsAdapter.getCount()) {
					TimelineTweet olderTimelineTweet = (TimelineTweet) tTweetsAdapter
							.getItem(i + 1);
					olderTweetId = olderTimelineTweet.getTweet().id;
				}
				loadData(olderTweetId, tTweet.getTweet().id, i + 1);
				return true;
			}
		}
		return false;
	}

	protected void insertTweets(ArrayList<TimelineTweet> tTweets, int position) {
		int i = 0;
		TimelineTweet tTweet = null;
		for (; i < tTweets.size(); ++i) {
			tTweet = tTweets.get(i);
			tTweetsAdapter.insert(tTweet, position + i);
		}

		// If we get any results, then it's possible there are more to come.
		// Indicate this by setting the "holeInData" attribute on the last
		// tweet.
		if (tTweet != null) {
			tTweet.holeInData = true;
			tTweet.save();
		}

		// Since we've successfully added data after maxId, that
		// Tweet no longer has a hole in the data
		if (position - 1 >= 0) {
			tTweet = tTweetsAdapter.getItem(position - 1);
			tTweet.holeInData = false;
			tTweet.save();
			tTweetsAdapter.notifyDataSetChanged();
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

			ArrayList<TimelineTweet> tTweets = TimelineTweet.fromJson(
					jsonTweets, getTimelineId());

			saveTweets(tTweets);

			insertTweets(tTweets, position);

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

	public abstract String getTimelineId();

	public void saveTweets(ArrayList<TimelineTweet> tTweets) {

		ActiveAndroid.beginTransaction();
		try {
			for (TimelineTweet tTweet : tTweets) {
				tTweet.getTweet().getUser().save();
				tTweet.getTweet().save();
				tTweet.save();
			}
			ActiveAndroid.setTransactionSuccessful();
		} finally {
			ActiveAndroid.endTransaction();
		}

		//TwitterClientApp.backupDb();
	}
}