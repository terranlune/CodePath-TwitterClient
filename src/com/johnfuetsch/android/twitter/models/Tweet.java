package com.johnfuetsch.android.twitter.models;

import java.io.Serializable;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class Tweet implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6263668194545871825L;

	public String id;
	public String text;
	public User user;
	public long created;

	@SuppressWarnings("deprecation")
	public static Tweet fromJson(JSONObject jsonObject) {
		Tweet tweet = new Tweet();
		// Deserialize json into object fields
		try {
			tweet.id = jsonObject.getString("id_str");
			tweet.text = jsonObject.getString("text");
			tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
			tweet.created = Date.parse(jsonObject.getString("created_at"));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		// Return new object
		return tweet;
	}

}
