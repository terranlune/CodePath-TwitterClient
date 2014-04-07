package com.johnfuetsch.android.twitter.fragments;

import android.util.Log;

import com.johnfuetsch.android.twitter.TwitterClientApp;


/**
 * A placeholder fragment containing a simple view.
 */
public class TimelineFragment extends TweetListFragment {

	public TimelineFragment() {
	}

	public void insertData(String sinceId, String maxId, int position) {

		Log.e("TimelineFragment", "insertData at position " + position
				+ " (sinceid: " + sinceId + " maxId:" + maxId + ")");
		if (busy) {
			Log.e("TimelineFragment", "Busy - aborting");
			return;
		} else {
			busy = true;
		}

		TwitterClientApp.getRestClient().getHomeTimeline(sinceId, maxId,
				new TweetListResponseHandler(position));
	}
}