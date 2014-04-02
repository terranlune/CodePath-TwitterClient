package com.johnfuetsch.android.twitter.activities;



import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.johnfuetsch.android.twitter.R;
import com.johnfuetsch.android.twitter.TwitterClientApp;
import com.johnfuetsch.android.twitter.fragments.TimelineFragment;
import com.johnfuetsch.android.twitter.models.Tweet;
import com.johnfuetsch.android.twitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends Activity {

	private static final int ACTION_COMPOSE = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new TimelineFragment()).commit();
		}
		
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
				Tweet tweet = (Tweet) data.getSerializableExtra("tweet");
				
				TimelineFragment timelineFragment = (TimelineFragment) getFragmentManager().findFragmentById(R.id.container);
				timelineFragment.onPostTweet(tweet);
			}
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
}
