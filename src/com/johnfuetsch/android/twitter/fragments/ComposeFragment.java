package com.johnfuetsch.android.twitter.fragments;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.johnfuetsch.android.twitter.R;
import com.johnfuetsch.android.twitter.TwitterClientApp;
import com.johnfuetsch.android.twitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * A placeholder fragment containing a simple view.
 */
public class ComposeFragment extends Fragment {

	private EditText etTweetText;
	private OnComposeTextEdited mCallback;
	private TextView tvComposeUserName;
	private TextView tvComposeUserScreenName;
	private ImageView ivComposeUserProfileImage;

	public ComposeFragment() {
	}

	public interface OnComposeTextEdited {
		public void onCharCountUpdated(int count);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			mCallback = (OnComposeTextEdited) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnComposeTextEdited");
		}
	
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_compose,
				container, false);
		
		etTweetText = (EditText) rootView.findViewById(R.id.etComposeTweetText);
		
		etTweetText.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				mCallback.onCharCountUpdated(140 - s.length());
			}});
		
		tvComposeUserName = (TextView) rootView.findViewById(R.id.tvComposeUserName);
		tvComposeUserScreenName = (TextView) rootView.findViewById(R.id.tvComposeUserScreenName);
		ivComposeUserProfileImage = (ImageView) rootView.findViewById(R.id.ivComposeUserProfileImage);
		TwitterClientApp.getRestClient().verifyCredentials(
				new JsonHttpResponseHandler() {

					@Override
					public void onSuccess(JSONObject jsonUser) {
						User user = User.fromJson(jsonUser);
						tvComposeUserName.setText(user.name);
						tvComposeUserScreenName.setText("@" + user.screen_name);

						ImageLoader.getInstance().displayImage(user.profile_image_url,
								ivComposeUserProfileImage);
					}

					@Override
					public void onFailure(Throwable e, JSONObject error) {
						super.onFailure(e, error);
						e.printStackTrace();
						Log.e("ComposeFragment", error.toString());
					}
				});
		
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
						Toast.makeText(getActivity(), "Error posting tweet",
								Toast.LENGTH_SHORT).show();
						e.printStackTrace();
						Log.e("ComposeFragment", error.toString());
					}
				});
	}
}