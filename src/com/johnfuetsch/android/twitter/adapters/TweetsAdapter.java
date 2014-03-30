package com.johnfuetsch.android.twitter.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.johnfuetsch.android.twitter.R;
import com.johnfuetsch.android.twitter.models.Tweet;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TweetsAdapter extends ArrayAdapter<Tweet> {

    private static class ViewLookupCache {
        TextView tvUserScreenName;
        TextView tvUserName;
        TextView tvTweetText;
        ImageView ivUserProfileImage;
    }
	
    public TweetsAdapter(Context context, ArrayList<Tweet> tweets) {
       super(context, R.layout.tweet_item, tweets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	
       // Get the data item for this position
       Tweet tweet = getItem(position);   
       
       // Check if an existing view is being reused, otherwise inflate the view
       ViewLookupCache viewLookupCache; // view lookup cache stored in tag
       if (convertView == null) {
          viewLookupCache = new ViewLookupCache();
          LayoutInflater inflater = LayoutInflater.from(getContext());
          convertView = inflater.inflate(R.layout.tweet_item, null);
          viewLookupCache.tvTweetText = (TextView) convertView.findViewById(R.id.tvTweetText);
          viewLookupCache.tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
          viewLookupCache.tvUserScreenName = (TextView) convertView.findViewById(R.id.tvUserScreenName);
          viewLookupCache.ivUserProfileImage = (ImageView) convertView.findViewById(R.id.ivUserProfileImage);
          convertView.setTag(viewLookupCache);
       } else {
           viewLookupCache = (ViewLookupCache) convertView.getTag();
       }
       // Populate the data into the template view using the data object
       viewLookupCache.tvTweetText.setText(tweet.text);
       viewLookupCache.tvUserName.setText(tweet.user.name);
       viewLookupCache.tvUserScreenName.setText("@" + tweet.user.screen_name);
       ImageLoader.getInstance().displayImage(tweet.user.profile_image_url, viewLookupCache.ivUserProfileImage);
       
       // Return the completed view to render on screen
       return convertView;
   }

}
