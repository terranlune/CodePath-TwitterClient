package com.johnfuetsch.android.twitter.fragments;

import com.johnfuetsch.android.twitter.controllers.BaseTimelineController;
import com.johnfuetsch.android.twitter.controllers.MentionsTimelineController;

public class MentionsFragment extends BaseTimelineFragment {

	@Override
	public BaseTimelineController initController() {
		return new MentionsTimelineController(getActivity(), this);
	}

}
