package com.johnfuetsch.android.twitter.controllers;

import android.content.Context;

import com.johnfuetsch.android.twitter.TwitterClientApp;
import com.johnfuetsch.android.twitter.models.Tweet;

public class HomeTimelineController extends BaseTimelineController {

	public HomeTimelineController(Context context,
			TimelineControllerCallback callback) {
		super(context, callback);
	}

	@Override
	protected void loadData(String sinceId, String maxId, int insertPosition) {

		if (!getNetworkLock())
			return;

		TwitterClientApp.getRestClient().getHomeTimeline(sinceId, maxId,
				new TweetListResponseHandler(insertPosition));
	}

	public void onPostTweet(Tweet tweet) {
		tweet.holeInData = true;
		tweetsAdapter.insert(tweet, 0);
	}
}
