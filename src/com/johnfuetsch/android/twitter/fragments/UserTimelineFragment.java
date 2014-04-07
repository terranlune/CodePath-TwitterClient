package com.johnfuetsch.android.twitter.fragments;

import android.os.Bundle;
import android.util.Log;

import com.johnfuetsch.android.twitter.TwitterClientApp;
import com.johnfuetsch.android.twitter.models.User;

public class UserTimelineFragment extends TweetListFragment {
	
	private static final String USER = "user";

	public void loadData(String sinceId, String maxId, int insertPosition) {

		Log.e("UserTimelineFragment", "insertData at position " + insertPosition
				+ " (sinceid: " + sinceId + " maxId:" + maxId + ")");
		
		if (!getNetworkLock()) return;

		User user = (User) getArguments().getSerializable(USER);
		
		TwitterClientApp.getRestClient().getUserTimeline(user.screen_name, sinceId, maxId,
				new TweetListResponseHandler(insertPosition));
	}
	
	public static UserTimelineFragment newInstance(User user) {
		UserTimelineFragment f = new UserTimelineFragment();
		
		Bundle args = new Bundle();
		args.putSerializable(USER, user);
		
		f.setArguments(args);
		return f;
	}
}