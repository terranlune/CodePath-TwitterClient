package com.johnfuetsch.android.twitter.activities;

import java.util.ArrayList;

import org.json.JSONArray;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.johnfuetsch.android.twitter.EndlessScrollListener;
import com.johnfuetsch.android.twitter.R;
import com.johnfuetsch.android.twitter.TwitterClientApp;
import com.johnfuetsch.android.twitter.adapters.TweetsAdapter;
import com.johnfuetsch.android.twitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timeline, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		private ListView lvTweets;
		private TweetsAdapter tweetsAdapter;

		public PlaceholderFragment() {
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

}
