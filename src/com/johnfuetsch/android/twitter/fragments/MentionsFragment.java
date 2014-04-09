package com.johnfuetsch.android.twitter.fragments;

import com.johnfuetsch.android.twitter.controllers.MentionsTimelineController;

public class MentionsFragment extends BaseTimelineFragment {

	@Override
	public void setupController() {
		controller = new MentionsTimelineController(getActivity(), this);
	}

}
