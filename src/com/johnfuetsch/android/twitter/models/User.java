package com.johnfuetsch.android.twitter.models;

import org.json.JSONException;
import org.json.JSONObject;

public class User {

	public String id;
	public String name;
	public String profile_image_url;
	public String screen_name;
	
	public static User fromJson(JSONObject jsonObject) {
		User user = new User();
		// Deserialize json into object fields
		try {
			user.id = jsonObject.getString("id_str");
			user.name = jsonObject.getString("name");
			user.screen_name = jsonObject.getString("screen_name");
			user.profile_image_url = jsonObject.getString("profile_image_url");
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		// Return new object
		return user;
	}
}
