CodePath-TwitterClient
======================

A simple twitter client implementation for http://thecodepath.com/androidbootcamp

Week 2
------

Completed user stories:
* Includes all required user stories from Week 3 Twitter Client
* User can switch between Timeline and Mention views using tabs.
 * User can view their home timeline tweets.
 * User can view the recent mentions of their username.
 * User can scroll to bottom of either of these lists and new tweets will load ("infinite scroll")
* User can navigate to view their own profile
 * User can see picture, tagline, # of followers, # of following, and tweets on their profile.
* User can click on the profile image in any tweet to see another user's profile.
 * User can see picture, tagline, # of followers, # of following, and tweets of clicked user.
 * Profile view should include that user's timeline
* Optional: When a network request goes out, user sees an indeterminate progress indicator
* Optional: User can open the twitter app offline and see last loaded tweets
 * Tweets are persisted into sqlite and can be displayed from the local DB

Screenshots:

![Look ma, tabs](http://i.imgur.com/mBlHmFHl.png)
![mentions](http://i.imgur.com/An4A876l.png)
![profile view](http://i.imgur.com/faBiAHEl.png)
![user profile view plus loading](http://i.imgur.com/7W9HT6il.png)

Week 1
------

Completed user stories:
* User can sign in using OAuth login flow
* User can view last 25 tweets from their home timeline
 * User should be able to see the user, body and timestamp for tweet
 * User should be displayed the relative timestamp for a tweet "8m", "7h"
* User can load more tweets once they reach the bottom of the list using "infinite scroll" pagination
* User can compose a new tweet
 * User can click a “Compose” icon in the Action Bar on the top right
 * User will have a Compose view opened
 * User can enter a message and hit a button to post to twitter
 * User should be taken back to home timeline with new tweet visible
 * Optional: User can see a counter with total number of characters left for tweet
* Optional: User can refresh tweets timeline by pulling down to refresh (i.e pull-to-refresh)
* Optional: Improve the user interface and theme the app to feel twitter branded

Screenshots:

![Initial timeline view](http://i.imgur.com/wKVqTM1l.png)
![Initial compose view](http://i.imgur.com/Itshsgil.png)
![In progress compose](http://i.imgur.com/Ekx3PVJl.png)
![Timeline post compose](http://i.imgur.com/xQCJvpIl.png)
![Pull to refresh](http://i.imgur.com/sN3iC25l.png)
![Timeline post refresh](http://i.imgur.com/vh5SjoTl.png)
