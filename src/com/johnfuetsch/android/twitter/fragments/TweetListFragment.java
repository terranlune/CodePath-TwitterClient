package com.johnfuetsch.android.twitter.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.johnfuetsch.android.twitter.EndlessScrollListener;
import com.johnfuetsch.android.twitter.R;
import com.johnfuetsch.android.twitter.adapters.TweetsAdapter;
import com.johnfuetsch.android.twitter.helpers.ToastErrors;
import com.johnfuetsch.android.twitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class TweetListFragment extends Fragment {

	protected PullToRefreshListView lvTweets;
	private TweetsAdapter tweetsAdapter;
	protected EndlessScrollListener scrollListener;
	protected boolean busy = false;

	public TweetListFragment() {
		super();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Prevent searching again on rotate
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_tweet_list, container,
				false);

		lvTweets = (PullToRefreshListView) rootView.findViewById(R.id.lvTweets);

		View emptyView = rootView.findViewById(R.id.vEmptyList);
		lvTweets.setEmptyView(emptyView);

		ArrayList<Tweet> tweets = new ArrayList<Tweet>();
		tweetsAdapter = new TweetsAdapter(getActivity(), tweets);
		lvTweets.setAdapter(tweetsAdapter);

		scrollListener = new EndlessScrollListener(10) {
			@Override
			public boolean onLoadMore(int page, int totalItemsCount) {
				// Only load more if we're already showing stuff
				if (tweetsAdapter.getCount() > 0) {
					return loadOlderData();
				} else {
					return false;
				}
			}

			@Override
			public boolean onLoadItems(int minPosition, int maxPosition) {
				return fillInHoles(minPosition, maxPosition);
			}
		};
		lvTweets.setOnScrollListener(scrollListener);

		// Set a listener to be invoked when the list should be refreshed.
		lvTweets.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				loadNewerData();
			}
		});

		// Seed the list with some initial data
		loadNewerData();
		// insertData(null, "452149613114687488", 0);

		return rootView;
	}

	public void onPostTweet(Tweet tweet) {
		tweet.holeInData = true;
		tweetsAdapter.insert(tweet, 0);
	}

	abstract protected void insertData(String sinceId, String maxId, int position);
	
	public boolean loadOlderData() {
		Tweet oldestTweet = tweetsAdapter.getItem(tweetsAdapter.getCount() - 1);
		String maxId = oldestTweet.id;
		insertData(null, maxId, tweetsAdapter.getCount());
		return true;
	}

	public void loadNewerData() {
		String latestId = null;
		if (tweetsAdapter.getCount() > 0) {
			Tweet latestTweet = tweetsAdapter.getItem(0);
			latestId = latestTweet.id;
		}
		insertData(latestId, null, 0);
	}

	private boolean fillInHoles(int minPosition, int maxPosition) {
		for (int i = minPosition; i < maxPosition; ++i) {
			Tweet tweet = (Tweet) tweetsAdapter.getItem(i);
			if (tweet.holeInData) {
				String olderTweetId = null;
				if (i + 1 < tweetsAdapter.getCount()) {
					Tweet olderTweet = (Tweet) tweetsAdapter.getItem(i + 1);
					olderTweetId = olderTweet.id;
				}
				insertData(olderTweetId, tweet.id, i + 1);
				return true;
			}
		}
		return false;
	}

	protected void insertTweets(ArrayList<Tweet> tweets, int position) {
		int i = 0;
		Tweet tweet = null;
		for (; i < tweets.size(); ++i) {
			tweet = tweets.get(i);
			tweetsAdapter.insert(tweet, position + i);
		}

		// If we get any results, then it's possible there are more to come.
		// Indicate this by setting the "holeInData" attribute on the last
		// tweet.
		if (tweet != null) {
			tweet.holeInData = true;
		}

		// Since we've successfully added data after maxId, that
		// Tweet no longer has a hole in the data
		if (position - 1 >= 0) {
			tweetsAdapter.getItem(position - 1).holeInData = false;
			tweetsAdapter.notifyDataSetChanged();
		}
	}

	class TweetListResponseHandler extends JsonHttpResponseHandler {
		protected int position;

		TweetListResponseHandler(int position) {
			this.position = position;
		}

		@Override
		public void onSuccess(JSONArray jsonTweets) {

			ArrayList<Tweet> tweets = Tweet.fromJson(jsonTweets);

			insertTweets(tweets, position);

			// Reset the progress indicators
			lvTweets.onRefreshComplete();
			scrollListener.loading = false;
			busy = false;
		}

		@Override
		public void onFailure(Throwable e, JSONObject error) {
			super.onFailure(e, error);
			ToastErrors.toastErrors(getActivity(), error,
					"Error loading timeline");
			e.printStackTrace();
			Log.e("TimelineFragment", error.toString());
			lvTweets.onRefreshComplete();
			scrollListener.loading = false;
			busy = false;
		}

	}
}