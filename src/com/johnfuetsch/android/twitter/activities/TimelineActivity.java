package com.johnfuetsch.android.twitter.activities;

import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.johnfuetsch.android.twitter.R;
import com.johnfuetsch.android.twitter.TwitterClientApp;
import com.johnfuetsch.android.twitter.fragments.MentionsFragment;
import com.johnfuetsch.android.twitter.fragments.TimelineFragment;
import com.johnfuetsch.android.twitter.models.Tweet;
import com.johnfuetsch.android.twitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends Activity implements TabListener {

	private static final int ACTION_COMPOSE = 0;
	private Tweet onPostTweet = null;
	private TimelineFragment timelineFragment;
	private MentionsFragment mentionsFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);

		setupTitle();
		setupTabs();
	}

	@Override
	protected void onResume() {
		super.onResume();
		handleOnPostTweet();
	}

	private void handleOnPostTweet() {
		Log.e("TimelineActivity", "handleOnPostTweet " + onPostTweet);
		if (onPostTweet != null) {
			ActionBar actionBar = getActionBar();
			Tab tab0 = actionBar.getTabAt(0);
			actionBar.selectTab(tab0);
			timelineFragment.onPostTweet(onPostTweet);
		}
		onPostTweet = null;
	}

	private void setupTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);

		Tab tab1 = actionBar.newTab().setText("Home")
		// .setIcon(R.drawable.ic_home)
				.setTag("TimelineFragment").setTabListener(this);

		actionBar.addTab(tab1);
		actionBar.selectTab(tab1);

		Tab tab2 = actionBar.newTab().setText("Mentions")
		// .setIcon(R.drawable.ic_mentions)
				.setTag("MentionsFragment").setTabListener(this);

		actionBar.addTab(tab2);
	}

	private void setupTitle() {
		TwitterClientApp.getRestClient().verifyCredentials(
				new JsonHttpResponseHandler() {

					@Override
					public void onSuccess(JSONObject jsonUser) {
						User user = User.fromJson(jsonUser);
						TimelineActivity.this.setTitle("@" + user.screen_name);
					}

					@Override
					public void onFailure(Throwable e, JSONObject error) {
						super.onFailure(e, error);
						e.printStackTrace();
						Log.e("TimelineActivity", error.toString());
					}
				});
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
		if (id == R.id.action_compose) {
			Intent intent = new Intent(this, ComposeActivity.class);
			startActivityForResult(intent, ACTION_COMPOSE);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == ACTION_COMPOSE) {
			if (resultCode == RESULT_OK) {
				onPostTweet = (Tweet) data.getSerializableExtra("tweet");
				Log.e("TimelineActivity", "Setting onPostTweet");
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		String tag = (String) tab.getTag();
		if (tag.equals("TimelineFragment")) {
			if (timelineFragment == null) {
				timelineFragment = new TimelineFragment();
				ft.add(R.id.container, timelineFragment);
			} else {
				ft.attach(timelineFragment);
			}
			// ft.commit();
		} else if (tag.equals("MentionsFragment")) {
			if (mentionsFragment == null) {
				mentionsFragment = new MentionsFragment();
				ft.add(R.id.container, mentionsFragment);
			} else {
				ft.attach(mentionsFragment);
			}
			// ft.commit();
		}
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {

		String tag = (String) tab.getTag();
		if (tag.equals("TimelineFragment")) {
			ft.detach(timelineFragment);
		} else if (tag.equals("MentionsFragment")) {
			ft.detach(mentionsFragment);
		}
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}
}
