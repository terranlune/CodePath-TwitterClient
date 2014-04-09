package com.johnfuetsch.android.twitter.fragments;

import com.johnfuetsch.android.twitter.controllers.BaseTimelineController;
import com.johnfuetsch.android.twitter.controllers.HomeTimelineController;
import com.johnfuetsch.android.twitter.models.Tweet;


/**
 * A placeholder fragment containing a simple view.
 */
public class HomeTimelineFragment extends BaseTimelineFragment {

	HomeTimelineController homeController;
	
	public void onPostTweet(Tweet tweet) {
		homeController.onPostTweet(tweet);
	}

	@Override
	public BaseTimelineController initController() {
		homeController = new HomeTimelineController(getActivity(), this);
		return homeController;
	}
	
}