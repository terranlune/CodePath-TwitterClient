package com.johnfuetsch.android.twitter.fragments;

import java.util.ArrayList;

import org.json.JSONArray;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.johnfuetsch.android.twitter.EndlessScrollListener;
import com.johnfuetsch.android.twitter.R;
import com.johnfuetsch.android.twitter.TwitterClientApp;
import com.johnfuetsch.android.twitter.adapters.TweetsAdapter;
import com.johnfuetsch.android.twitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * A placeholder fragment containing a simple view.
 */
public class TimelineFragment extends Fragment {

	private ListView lvTweets;
	private TweetsAdapter tweetsAdapter;

	public TimelineFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_timeline,
				container, false);

		lvTweets = (ListView) rootView.findViewById(R.id.lvTweets);

		ArrayList<Tweet> tweets = new ArrayList<Tweet>();
		tweetsAdapter = new TweetsAdapter(getActivity(), tweets);
		lvTweets.setAdapter(tweetsAdapter);

		lvTweets.setOnScrollListener(new EndlessScrollListener(10) {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {

				String maxId = null;
				if (tweetsAdapter.getCount() > 0) {
					Tweet oldestTweet = tweetsAdapter.getItem(tweetsAdapter
							.getCount() - 1);
					maxId = oldestTweet.id;
				}

				loadData(null, maxId);

			}
		});

		loadData(null, null);

		return rootView;
	}

	public void loadData(String sinceId, String maxId) {

		TwitterClientApp.getRestClient().getHomeTimeline(sinceId, maxId,
				new JsonHttpResponseHandler() {

					@Override
					public void onSuccess(JSONArray jsonTweets) {

						ArrayList<Tweet> tweets = Tweet
								.fromJson(jsonTweets);
						tweetsAdapter.addAll(tweets);
					}
				});
	}
}