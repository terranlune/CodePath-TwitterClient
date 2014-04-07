package com.johnfuetsch.android.twitter.models;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6682876873706217360L;
	
	public String id;
	public String name;
	public String profile_image_url;
	public String screen_name;
	public String description;
	public int statuses_count; // Aka. tweets
	public int friends_count; // Aka. following
	public int followers_count;
	
	public static User fromJson(JSONObject jsonObject) {
		User user = new User();
		// Deserialize json into object fields
		try {
			user.id = jsonObject.getString("id_str");
			user.name = jsonObject.getString("name");
			user.screen_name = jsonObject.getString("screen_name");
			user.profile_image_url = jsonObject.getString("profile_image_url");
			user.description = jsonObject.getString("description");
			user.statuses_count = jsonObject.getInt("statuses_count");
			user.friends_count = jsonObject.getInt("friends_count");
			user.followers_count = jsonObject.getInt("followers_count");
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		// Return new object
		return user;
	}
}
