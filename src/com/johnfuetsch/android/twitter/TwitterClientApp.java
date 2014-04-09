package com.johnfuetsch.android.twitter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/*
 * This is the Android application itself and is used to configure various settings
 * including the image cache in memory and on disk. This also adds a singleton
 * for accessing the relevant rest client.
 * 
 *     RestClient client = RestClientApp.getRestClient();
 *     // use client to send requests to API
 *     
 */
public class TwitterClientApp extends com.activeandroid.app.Application {
	private static Context context;
	
    @Override
    public void onCreate() {
        super.onCreate();
        TwitterClientApp.context = this;
        

		// Create global configuration and initialize ImageLoader with this
		// configuration
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheInMemory(true)
				.cacheOnDisc(true)
				.resetViewBeforeLoading(true)
				.build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext()).
				defaultDisplayImageOptions(defaultOptions).
				build();
		ImageLoader.getInstance().init(config);
    }
    
    public static TwitterClient getRestClient() {
    	return (TwitterClient) TwitterClient.getInstance(TwitterClient.class, TwitterClientApp.context);
    }
    
    public static void backupDb() {
    	
    	
    	
    	try {
            File sd = Environment.getExternalStorageDirectory();

            if (sd.canWrite()) {
                File currentDB = context.getDatabasePath("RestClient.db");
                File backupDB = new File(sd, "TwitterClient.db");

                if (currentDB.exists()) {
                	FileInputStream srcStream = new FileInputStream(currentDB);
                    FileChannel src = srcStream.getChannel();
                    FileOutputStream destStream = new FileOutputStream(backupDB);
                    FileChannel dst = destStream.getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                    srcStream.close();
                    destStream.close();
                    Log.e("TwitterClientApp", "Copied " + currentDB.getAbsolutePath() + " to " + backupDB.getAbsolutePath());
                }
            }
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
}