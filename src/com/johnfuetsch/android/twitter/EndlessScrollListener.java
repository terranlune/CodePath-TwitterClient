package com.johnfuetsch.android.twitter;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

public abstract class EndlessScrollListener implements OnScrollListener {

	// The minimum amount of items to have below your current scroll position
	// before loading more.
	private int visibleThreshold = 5;
	// The current offset index of data you have loaded
	private int currentPage = 0;
	// The total number of items in the dataset after the last load
	private int previousTotalItemCount = 0;
	// True if we are still waiting for the last set of data to load.
	public boolean loading = true;
	// Sets the starting page index
	private int startingPageIndex = 0;

	public EndlessScrollListener() {
	}

	public EndlessScrollListener(int visibleThreshold) {
		this.visibleThreshold = visibleThreshold;
	}

	public EndlessScrollListener(int visibleThreshold, int startPage) {
		this.visibleThreshold = visibleThreshold;
		this.startingPageIndex = startPage;
		this.currentPage = startPage;
	}

	// This happens many times a second during a scroll, so be wary of the code
	// you place here.
	// We are given a few useful parameters to help us work out if we need to
	// load some more data,
	// but first we check if we are waiting for the previous load to finish.
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// If the total item count is zero and the previous isn't, assume the
		// list is invalidated and should be reset back to initial state
		if (totalItemCount < previousTotalItemCount) {
			this.currentPage = this.startingPageIndex;
			this.previousTotalItemCount = totalItemCount;
			if (totalItemCount == 0) {
				this.loading = true;
			}
		}

		// If itÕs still loading, we check to see if the dataset count has
		// changed, if so we conclude it has finished loading and update the
		// current page number and total item count.
		if (loading && (totalItemCount > previousTotalItemCount)) {
			loading = false;
			previousTotalItemCount = totalItemCount;
			currentPage++;
		}

		// If it isnÕt currently loading, we check to see if we have breached
		// the visibleThreshold and need to reload more data.
		// If we do need to reload some more data, we execute onLoadMore to
		// fetch the data.
		if (!loading
				&& firstVisibleItem + visibleItemCount + visibleThreshold > totalItemCount) {
			loading = true;
			boolean loadingMore = onLoadMore(currentPage, totalItemCount);
			if (!loadingMore) {
				loading = false;
			}
		}

		// If any of the items in visible threshold represent holes in the data,
		// then fill them in
		if (!loading) {
			loading = true;
			int maxItem = Math.min(firstVisibleItem
					+ visibleItemCount + visibleThreshold, totalItemCount-1);
			loading = onLoadItems(firstVisibleItem, maxItem);
		}
	}

	public abstract boolean onLoadItems(int minPosition, int maxPosition);
	
	// Defines the process for actually loading more data based on page
	public abstract boolean onLoadMore(int page, int totalItemsCount);

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// Don't take any action on changed
	}
}
