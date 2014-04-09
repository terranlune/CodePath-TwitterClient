package com.johnfuetsch.android.twitter.fragments;

import com.johnfuetsch.android.twitter.controllers.HomeTimelineController;
import com.johnfuetsch.android.twitter.models.Tweet;


/**
 * A placeholder fragment containing a simple view.
 */
public class HomeTimelineFragment extends BaseTimelineFragment {

	public void onPostTweet(Tweet tweet) {
		controller.onPostTweet(tweet);
	}

	@Override
	public void setupController() {
		controller = new HomeTimelineController(getActivity(), this);
	}
	
}