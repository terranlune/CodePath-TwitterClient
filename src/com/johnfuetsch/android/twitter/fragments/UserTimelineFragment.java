package com.johnfuetsch.android.twitter.fragments;

import android.os.Bundle;

import com.johnfuetsch.android.twitter.controllers.UserTimelineController;
import com.johnfuetsch.android.twitter.models.User;

public class UserTimelineFragment extends BaseTimelineFragment {
	
	private static final String USER = "user";

	public static UserTimelineFragment newInstance(User user) {
		UserTimelineFragment f = new UserTimelineFragment();
		
		Bundle args = new Bundle();
		args.putSerializable(USER, user);
		
		f.setArguments(args);
		return f;
	}

	@Override
	public void setupController() {
		User user = (User) getArguments().getSerializable(USER);
		controller = new UserTimelineController(getActivity(), this, user);
	}
}