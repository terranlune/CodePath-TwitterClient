package com.johnfuetsch.android.twitter.helpers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.widget.Toast;

public class ToastErrors {

	static public void toastErrors(Context context, JSONObject errorResponse,
			String backupMessage) {
		try {
			toastErrors(context, errorResponse);
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(context, backupMessage, Toast.LENGTH_SHORT).show();
		}
	}

	static public void toastErrors(Context context, JSONObject errorResponse)
			throws JSONException {
		JSONArray errors = errorResponse.getJSONArray("errors");
		for (int i = 0; i < errors.length(); ++i) {
			JSONObject error = errors.getJSONObject(i);
			Toast.makeText(context, error.getString("message"),
					Toast.LENGTH_SHORT).show();
		}
	}
}
