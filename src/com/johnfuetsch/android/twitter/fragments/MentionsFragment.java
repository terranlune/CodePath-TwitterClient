package com.johnfuetsch.android.twitter.fragments;

import android.util.Log;

import com.johnfuetsch.android.twitter.TwitterClientApp;

public class MentionsFragment extends TweetListFragment {

	@Override
	protected void loadData(String sinceId, String maxId, int insertPosition) {
		// TODO Auto-generated method stub

		Log.e("MentionsFragment", "insertData at position " + insertPosition
				+ " (sinceid: " + sinceId + " maxId:" + maxId + ")");

		if (!getNetworkLock()) return;

		TwitterClientApp.getRestClient().getMentionsTimeline(sinceId, maxId,
				new TweetListResponseHandler(insertPosition));
	}

}
