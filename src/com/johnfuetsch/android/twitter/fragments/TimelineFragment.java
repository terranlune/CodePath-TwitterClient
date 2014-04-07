package com.johnfuetsch.android.twitter.fragments;

import android.util.Log;

import com.johnfuetsch.android.twitter.TwitterClientApp;
import com.johnfuetsch.android.twitter.models.Tweet;


/**
 * A placeholder fragment containing a simple view.
 */
public class TimelineFragment extends TweetListFragment {


	public void onPostTweet(Tweet tweet) {
		tweet.holeInData = true;
		tweetsAdapter.insert(tweet, 0);
	}
	
	public void loadData(String sinceId, String maxId, int insertPosition) {

		Log.e("TimelineFragment", "insertData at position " + insertPosition
				+ " (sinceid: " + sinceId + " maxId:" + maxId + ")");
		
		if (!getNetworkLock()) return;

		TwitterClientApp.getRestClient().getHomeTimeline(sinceId, maxId,
				new TweetListResponseHandler(insertPosition));
	}
}