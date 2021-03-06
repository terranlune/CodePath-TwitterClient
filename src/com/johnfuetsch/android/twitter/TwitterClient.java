package com.johnfuetsch.android.twitter;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * �
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
	public static final String REST_URL = "https://api.twitter.com/1.1";
	public static final String REST_CONSUMER_KEY = "8PHPKICP3x9Xi4jB1ruNg";
	public static final String REST_CONSUMER_SECRET = "KEvj8tanspGrLXVZJEj6n6WnacI7xhTg2a2izjeoz9Y";
	public static final String REST_CALLBACK_URL = "oauth://twitterclient";

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY,
				REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	public void getHomeTimeline(String sinceId, String maxId,
			AsyncHttpResponseHandler handler) {
		getTimeline("statuses/home_timeline.json", sinceId, maxId, handler);
	}

	public void getMentionsTimeline(String sinceId, String maxId,
			AsyncHttpResponseHandler handler) {
		getTimeline("statuses/mentions_timeline.json", sinceId, maxId, handler);
	}
	
	public void getUserTimeline(String screenName, String sinceId, String maxId,
			AsyncHttpResponseHandler handler) {
		
		RequestParams args = getTimelineParams(sinceId, maxId);
		args.put("screen_name", screenName);
		String apiUrl = getApiUrl("statuses/user_timeline.json");

		Log.e("TwitterClient",
				AsyncHttpClient.getUrlWithQueryString(apiUrl, args));

		client.get(apiUrl, args, handler);
	}
	
	private void getTimeline(String endpoint, String sinceId, String maxId,
			AsyncHttpResponseHandler handler) {
		
		RequestParams args = getTimelineParams(sinceId, maxId);
		String apiUrl = getApiUrl(endpoint);

		Log.e("TwitterClient",
				AsyncHttpClient.getUrlWithQueryString(apiUrl, args));

		client.get(apiUrl, args, handler);
	}

	private RequestParams getTimelineParams(String sinceId, String maxId) {
		// max_id is inclusive. Subtract one to make it exclusive
		if (maxId != null) {
			maxId = Long.toString(Long.parseLong(maxId) - 1);
		}

		RequestParams args = new RequestParams();
		args.put("count", "50");
		args.put("since_id", sinceId);
		args.put("max_id", maxId);
		return args;
	}

	public void postStatusUpdate(String status, AsyncHttpResponseHandler handler) {

		RequestParams args = new RequestParams();
		args.put("status", status);

		String apiUrl = getApiUrl("statuses/update.json");

		Log.d("TwitterClient",
				AsyncHttpClient.getUrlWithQueryString(apiUrl, args));

		client.post(apiUrl, args, handler);
	}

	public void verifyCredentials(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("account/verify_credentials.json");
		client.get(apiUrl, handler);
	}
}