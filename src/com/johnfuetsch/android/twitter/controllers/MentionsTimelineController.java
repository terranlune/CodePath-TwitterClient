package com.johnfuetsch.android.twitter.controllers;

import android.content.Context;

import com.johnfuetsch.android.twitter.TwitterClientApp;

public class MentionsTimelineController extends BaseTimelineController {

	public MentionsTimelineController(Context context,
			TimelineControllerCallback callback) {
		super(context, callback);
	}
	
	@Override
	protected void loadData(String sinceId, String maxId, int insertPosition) {
		
		if (!getNetworkLock()) return;

		TwitterClientApp.getRestClient().getMentionsTimeline(sinceId, maxId,
				new TweetListResponseHandler(insertPosition));
	}

}
