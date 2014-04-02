package com.johnfuetsch.android.twitter.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.johnfuetsch.android.twitter.R;
import com.johnfuetsch.android.twitter.fragments.ComposeFragment;

public class ComposeActivity extends Activity implements
		ComposeFragment.OnComposeTextEdited {

	private TextView tvCharCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new ComposeFragment()).commit();
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compose, menu);

		MenuItem menuItem = menu.findItem(R.id.action_char_count);
		LinearLayout actionView = (LinearLayout) menuItem.getActionView();
		tvCharCount = (TextView) actionView.findViewById(R.id.tvCharCount);

		onCharCountUpdated(140);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_tweet) {
			ComposeFragment composeFragment = (ComposeFragment) getFragmentManager()
					.findFragmentById(R.id.container);
			composeFragment.onTweet();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCharCountUpdated(int count) {
		if (tvCharCount != null) {
			tvCharCount.setText(Integer.toString(count));
		}
	}

}
