package com.johnfuetsch.android.twitter.fragments;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.johnfuetsch.android.twitter.R;
import com.johnfuetsch.android.twitter.TwitterClientApp;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * A placeholder fragment containing a simple view.
 */
public class ComposeFragment extends Fragment {

	private EditText etTweetText;

	public ComposeFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_compose,
				container, false);
		
		etTweetText = (EditText) rootView.findViewById(R.id.etComposeTweetText);
		
		return rootView;
	}
	
	public void onTweet() {
		String status = etTweetText.getText().toString();
		

		TwitterClientApp.getRestClient().postStatusUpdate(status, 
				new JsonHttpResponseHandler() {

					@Override
					public void onSuccess(JSONObject jsonTweet) {
						Log.d("tweetSuccess", jsonTweet.toString());
						
						Intent intent = new Intent();
						intent.putExtra("jsonTweet", jsonTweet.toString());
						
						Activity activity = getActivity();
						activity.setResult(Activity.RESULT_OK, intent);
						activity.finish();						

					}
				});
	}
}