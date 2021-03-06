package com.johnfuetsch.android.twitter.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.johnfuetsch.android.twitter.R;
import com.johnfuetsch.android.twitter.activities.UserProfileActivity;
import com.johnfuetsch.android.twitter.models.TimelineTweet;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TimelineTweetsAdapter extends ArrayAdapter<TimelineTweet> {

	private static class ViewLookupCache {
		TextView tvUserScreenName;
		TextView tvUserName;
		TextView tvTweetText;
		TextView tvTweetTimestamp;
		ImageView ivUserProfileImage;
		View vHoleInData;
	}

	public TimelineTweetsAdapter(Context context, ArrayList<TimelineTweet> tTweets) {
		super(context, R.layout.tweet_item, tTweets);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// Get the data item for this position
		TimelineTweet tTweet = getItem(position);

		// Check if an existing view is being reused, otherwise inflate the view
		ViewLookupCache viewLookupCache; // view lookup cache stored in tag
		if (convertView == null) {
			viewLookupCache = new ViewLookupCache();
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(R.layout.tweet_item, null);
			viewLookupCache.tvTweetText = (TextView) convertView
					.findViewById(R.id.tvTweetText);
			viewLookupCache.tvUserName = (TextView) convertView
					.findViewById(R.id.tvUserName);
			viewLookupCache.tvUserScreenName = (TextView) convertView
					.findViewById(R.id.tvUserScreenName);
			viewLookupCache.tvTweetTimestamp = (TextView) convertView
					.findViewById(R.id.tvTweetTimestamp);
			viewLookupCache.ivUserProfileImage = (ImageView) convertView
					.findViewById(R.id.ivUserProfileImage);
			viewLookupCache.vHoleInData = (View) convertView.findViewById(R.id.lHoleInData);
			convertView.setTag(viewLookupCache);
		} else {
			viewLookupCache = (ViewLookupCache) convertView.getTag();
		}
		// Populate the data into the template view using the data object
		viewLookupCache.tvTweetText.setText(tTweet.getTweet().text);
		viewLookupCache.tvUserName.setText(tTweet.getTweet().getUser().name);
		viewLookupCache.tvTweetTimestamp
				.setText(getRelativeDateTimeString(tTweet.getTweet().created));
		viewLookupCache.tvUserScreenName.setText("@" + tTweet.getTweet().getUser().screen_name);
		ImageLoader.getInstance().displayImage(tTweet.getTweet().getUser().profile_image_url,
				viewLookupCache.ivUserProfileImage);
		viewLookupCache.vHoleInData.setVisibility(tTweet.holeInData ? View.VISIBLE : View.GONE);

		viewLookupCache.ivUserProfileImage.setTag(tTweet);
		viewLookupCache.ivUserProfileImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TimelineTweet tTweet = (TimelineTweet) v.getTag();
				Intent intent = new Intent(v.getContext(), UserProfileActivity.class);
				intent.putExtra("user", tTweet.getTweet().getUser());
				v.getContext().startActivity(intent);
			}});
		
		// Return the completed view to render on screen
		return convertView;
	}

	static int[] steps = { 1, 60, 3600, 3600 * 24, 3600 * 24 * 7,
			3600 * 24 * 365 };
	static String[] names = { "s", "m", "h", "d", "w", "y" };
	static public CharSequence getRelativeDateTimeString(long time) {

		Long dif = (System.currentTimeMillis() - time) / 1000;

		if (dif < 0)
			return "";

		int i = 1;
		for (; i < steps.length - 1; i++) {
			if (dif < steps[i]) {
				String output = Long.toString(dif / steps[i - 1])
						+ names[i - 1];
				return output;
			}
		}

		return Long.toString(dif / steps[i]) + names[i];
	}

}
