package com.johnfuetsch.android.twitter.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.johnfuetsch.android.twitter.EndlessScrollListener;
import com.johnfuetsch.android.twitter.R;
import com.johnfuetsch.android.twitter.TwitterClientApp;
import com.johnfuetsch.android.twitter.adapters.TweetsAdapter;
import com.johnfuetsch.android.twitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

/**
 * A placeholder fragment containing a simple view.
 */
public class TimelineFragment extends Fragment {

	private PullToRefreshListView lvTweets;
	private TweetsAdapter tweetsAdapter;

	public TimelineFragment() {
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

		View rootView = inflater.inflate(R.layout.fragment_timeline,
				container, false);

		lvTweets = (PullToRefreshListView) rootView.findViewById(R.id.lvTweets);

		ArrayList<Tweet> tweets = new ArrayList<Tweet>();
		tweetsAdapter = new TweetsAdapter(getActivity(), tweets);
		lvTweets.setAdapter(tweetsAdapter);

		lvTweets.setOnScrollListener(new EndlessScrollListener(10) {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {

				Log.e("TimelineFragment","onLoadMore");
				
				// Only load more if we're already showing stuff 
				if (tweetsAdapter.getCount() > 0) {
					Tweet oldestTweet = tweetsAdapter.getItem(tweetsAdapter
							.getCount() - 1);
					String maxId = oldestTweet.id;
					appendData(maxId);
				}


			}
		});
		
        // Set a listener to be invoked when the list should be refreshed.
        lvTweets.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
            	
            	Log.e("TimelineFragment", "onRefresh");
            	
                // Your code to refresh the list contents
                // Make sure you call listView.onRefreshComplete()
                // once the loading is done. This can be done from here or any
                // place such as when the network request has completed successfully.

				String latestId = null;
				if (tweetsAdapter.getCount() > 0) {
					Tweet latestTweet = tweetsAdapter.getItem(0);
					latestId = latestTweet.id;
				}
				insertData(latestId, null, 0);
				

            }
        });

        // Seed the list with some initial data
		appendData(null);

		return rootView;
	}

	class PullToRefreshResponseHandler extends JsonHttpResponseHandler {
		protected String sinceId;
		protected String maxId;
		protected int position;
		PullToRefreshResponseHandler(String sinceId, String maxId, int position) {
			this.sinceId = sinceId;
			this.maxId = maxId;
			this.position = position;
		}
	}
	
	public void insertData(String sinceId, String maxId, int position) {

		
		TwitterClientApp.getRestClient().getHomeTimeline(sinceId, maxId,
				new PullToRefreshResponseHandler(sinceId, maxId, position) {

					@Override
					public void onSuccess(JSONArray jsonTweets) {
						
						Log.e("TimelineFragment","inserting " + jsonTweets.length() + " tweets");
						ArrayList<Tweet> tweets = Tweet.fromJson(jsonTweets);
						int i=0;
						for (; i<tweets.size(); ++i) {
							Tweet tweet = tweets.get(i);
							tweetsAdapter.insert(tweet, position+i);
						}
						// If we get any results, then it's possible there 
						// are more to come. Fetch them, and insert between 
						// what we started with and what we just inserted..
						if (tweets.size() > 0) {
							Tweet oldestRefreshedTweet = tweets.get(tweets.size()-1);
							insertData(sinceId, oldestRefreshedTweet.id, position+i);
						}else{
							lvTweets.onRefreshComplete();
						}
					}

					@Override
					public void onFailure(Throwable e, JSONObject error) {
						super.onFailure(e, error);
						toastErrors(getActivity(), error, "Error refreshing timeline");
						e.printStackTrace();
						Log.e("TimelineFragment", error.toString());
						lvTweets.onRefreshComplete();
					}
				});
	}
	
	public void appendData(String maxId) {

		TwitterClientApp.getRestClient().getHomeTimeline(null, maxId,
				new JsonHttpResponseHandler() {

					@Override
					public void onSuccess(JSONArray jsonTweets) {

						Log.e("TimelineFragment", "Appending " + jsonTweets.length() + " tweets");
						ArrayList<Tweet> tweets = Tweet
								.fromJson(jsonTweets);
						tweetsAdapter.addAll(tweets);
					}

					@Override
					public void onFailure(Throwable e, JSONObject error) {
						super.onFailure(e, error);
						toastErrors(getActivity(), error, "Error getting timeline");
						e.printStackTrace();
						Log.e("TimelineFragment", error.toString());
					}
				});
	}
	
	static public void toastErrors(Context context, JSONObject errorResponse, String backupMessage) {
		try {
			toastErrors(context, errorResponse);
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(context, backupMessage, Toast.LENGTH_SHORT).show();
		}
	}
	static public void toastErrors(Context context, JSONObject errorResponse) throws JSONException {
		JSONArray errors = errorResponse.getJSONArray("errors");
		for (int i=0; i<errors.length(); ++i) {
			JSONObject error = errors.getJSONObject(i);
			Toast.makeText(context, error.getString("message"), Toast.LENGTH_SHORT).show();
		}
	}
}