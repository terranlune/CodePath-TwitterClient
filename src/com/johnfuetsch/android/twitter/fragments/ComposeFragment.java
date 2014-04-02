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
import android.widget.Toast;

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
						// TODO: Send Tweet directly
						intent.putExtra("jsonTweet", jsonTweet.toString());
						
						Activity activity = getActivity();
						activity.setResult(Activity.RESULT_OK, intent);
						activity.finish();						

					}
					
					@Override
					public void onFailure(Throwable e, JSONObject error) {
						super.onFailure(e, error);
						Toast.makeText(getActivity(), "Error posting tweet", Toast.LENGTH_SHORT).show();
						e.printStackTrace();
						Log.e("ComposeFragment", error.toString());
					}
				});
	}
}