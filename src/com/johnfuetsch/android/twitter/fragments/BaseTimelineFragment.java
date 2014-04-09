package com.johnfuetsch.android.twitter.fragments;

import org.json.JSONObject;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.johnfuetsch.android.twitter.EndlessScrollListener;
import com.johnfuetsch.android.twitter.R;
import com.johnfuetsch.android.twitter.controllers.BaseTimelineController;
import com.johnfuetsch.android.twitter.controllers.BaseTimelineController.TimelineControllerCallback;
import com.johnfuetsch.android.twitter.helpers.ToastErrors;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public abstract class BaseTimelineFragment extends Fragment implements TimelineControllerCallback {

	protected BaseTimelineController controller;
	private PullToRefreshListView lvTweets;
	private EndlessScrollListener scrollListener;

	public BaseTimelineFragment() {
		super();
	}

	abstract public BaseTimelineController initController();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Prevent searching again on rotate
		setRetainInstance(true);
		
		controller = initController();
		controller.loadInitialData();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_tweet_list,
				container, false);

		lvTweets = (PullToRefreshListView) rootView.findViewById(R.id.lvTweets);

		View emptyView = rootView.findViewById(R.id.vEmptyList);
		lvTweets.setEmptyView(emptyView);

		lvTweets.setAdapter(controller.getAdapter());

		scrollListener = new EndlessScrollListener(10) {
			@Override
			public boolean onLoadMore(int page, int totalItemsCount) {
				// onLoadItems handles more cases (such as a really short
				// results list) so just use that
				return false;
			}

			@Override
			public boolean onLoadItems(int minPosition, int maxPosition) {
				return controller.fillInHoles(minPosition, maxPosition);
			}
		};
		lvTweets.setOnScrollListener(scrollListener);

		// Set a listener to be invoked when the list should be refreshed.
		lvTweets.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				controller.loadNewerData();
			}
		});
		
		return rootView;
	}

	@Override
	public void onBusyStart() {
		 getActivity().setProgressBarIndeterminateVisibility(true);
	}

	@Override
	public void onBusyEnd() {
		 if (this.isAdded()) {
		 getActivity().setProgressBarIndeterminateVisibility(false);
		 }
		 lvTweets.onRefreshComplete();
		 scrollListener.loading = false;
	}

	@Override
	public void onError(JSONObject error) {
		if (isAdded()) {
			ToastErrors.toastErrors(getActivity(), error,
					"Error loading tweets");
		}
	}

}