package com.johnfuetsch.android.twitter.models;

import java.io.Serializable;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "tweet")
public class Tweet extends Model implements Serializable {

	private static final long serialVersionUID = -6263668194545871825L;

	@Column(name = "remote_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	public String id;
	
	@Column(name = "text")
	public String text;
	
	@Column(name = "user_id")
	public String user_id;
	private User user;
	
	@Column(name = "created")
	public long created;

	public User getUser() {
		if (user == null) {
			user = User.fromId(user_id);
		}
		return user;
	}

	public Tweet() {
	}
	
	@SuppressWarnings("deprecation")
	public static Tweet fromJson(JSONObject jsonObject) {
		Tweet tweet = new Tweet();
		// Deserialize json into object fields
		try {
			tweet.id = jsonObject.getString("id_str");
			tweet.text = jsonObject.getString("text");
			tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
			tweet.user_id = tweet.user.id;
			tweet.created = Date.parse(jsonObject.getString("created_at"));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		// Return new object
		return tweet;
	}
	public static Tweet fromId(String id) {
		return new Select().from(Tweet.class).where("remote_id = ?", id)
				.limit(1).executeSingle();
	}

}
