package com.johnfuetsch.android.twitter.controllers;

import android.content.Context;

import com.johnfuetsch.android.twitter.TwitterClientApp;
import com.johnfuetsch.android.twitter.models.User;

public class UserTimelineController extends BaseTimelineController {

	protected User user;
	
	public UserTimelineController(Context context,
			TimelineControllerCallback callback, User user) {
		super(context, callback);
		
		this.user = user;
	}
	
	public void loadData(String sinceId, String maxId, int insertPosition) {

		if (!getNetworkLock()) return;
		
		TwitterClientApp.getRestClient().getUserTimeline(user.screen_name, sinceId, maxId,
				new TweetListResponseHandler(insertPosition));
	}
	

}
