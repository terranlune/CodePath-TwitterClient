package com.johnfuetsch.android.twitter.models;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "user")
public class User extends Model implements Serializable {

	private static final long serialVersionUID = -6682876873706217360L;

	@Column(name = "remote_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	public String id;
	
	@Column(name = "name")
	public String name;
	
	@Column(name = "profile_image_url")
	public String profile_image_url;
	
	@Column(name = "screen_name")
	public String screen_name;
	
	@Column(name = "description")
	public String description;
	
	@Column(name = "statuses_count")
	public int statuses_count; // Aka. tweets
	
	@Column(name = "friends_count")
	public int friends_count; // Aka. following
	
	@Column(name = "followers_count")
	public int followers_count;

	public User() {
	}
	
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

	public static User fromId(String id) {
		return new Select().from(User.class).where("remote_id = ?", id)
				.limit(1).executeSingle();
	}
}
