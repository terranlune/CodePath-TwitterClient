package com.johnfuetsch.android.twitter.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.johnfuetsch.android.twitter.R;
import com.johnfuetsch.android.twitter.fragments.UserTimelineFragment;
import com.johnfuetsch.android.twitter.models.User;
import com.nostra13.universalimageloader.core.ImageLoader;

public class UserProfileActivity extends Activity {

	private TextView tvUserName;
	private TextView tvUserDescription;
	private ImageView ivUserProfileImage;
	private TextView tvTweetCount;
	private TextView tvFollowingCount;
	private TextView tvFollowersCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_profile);

		tvUserName = (TextView) findViewById(R.id.tvUserName);
		tvUserDescription = (TextView) findViewById(R.id.tvUserDescription);
		ivUserProfileImage = (ImageView) findViewById(R.id.ivUserProfileImage);
		tvTweetCount = (TextView) findViewById(R.id.tvTweetCount);
		tvFollowingCount = (TextView) findViewById(R.id.tvFollowingCount);
		tvFollowersCount = (TextView) findViewById(R.id.tvFollowersCount);

		User user = (User) getIntent().getSerializableExtra("user");

		setHeaderInfo(user);

		// Load the timeline
		getFragmentManager()
				.beginTransaction()
				.add(R.id.container,
						UserTimelineFragment.newInstance(user))
				.commit();
		
	}

	private void setHeaderInfo(User user) {
		ImageLoader.getInstance().displayImage(user.profile_image_url.replace("normal", "bigger"),
				ivUserProfileImage);
		setTitle("@" + user.screen_name);
		tvUserName.setText(user.name);
		tvUserDescription.setText(user.description);
		tvTweetCount.setText(getResources().getString(R.string.tweet_count, user.statuses_count));
		tvFollowersCount.setText(getResources().getString(R.string.followers_count, user.followers_count));
		tvFollowingCount.setText(getResources().getString(R.string.following_count, user.friends_count));
	};

}
