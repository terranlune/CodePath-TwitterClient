package com.johnfuetsch.android.twitter.activities;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.johnfuetsch.android.twitter.R;
import com.johnfuetsch.android.twitter.fragments.TimelineFragment;

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
				// TODO: Inject posted tweet
				String jsonTweet = data.getStringExtra("jsonTweet");
				// TODO: Fill in any tweets between the latest one we knew about, and the one we just inserted
			}
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
}
