package com.collabtweet.service;

import com.collabtweet.exception.StorageException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * Created by kganesh on 5/17/16.
 */
public class InMemoryTweetService implements TweetService {

    // not safe in concurrent scenarios
    private static HashMap<String, ArrayList<String>> following = new HashMap<>();
    private static HashMap<String, TreeMap<Long, String>> tweets = new HashMap<>();

    @Override
    public boolean storeTweet(String user, String tweet) throws StorageException {
        TreeMap<Long, String> tweetsSoFar = tweets.get(user);

        if (tweetsSoFar == null) {

            tweetsSoFar = new TreeMap<>(Collections.reverseOrder());
        } else if (tweetsSoFar.firstEntry().getValue().equals(tweet)) {

            // accidental re-submission
            return false;
        }

        tweetsSoFar.put(System.nanoTime(), tweet);

        tweets.put(user, tweetsSoFar);

        return true;
    }

    @Override
    public TreeMap<Long, String> getFeed(String user) throws StorageException {

        ArrayList<String> followees = following.get(user);

        if (followees == null) {
            return null;
        }

        TreeMap<Long, String> feed = new TreeMap<>(Collections.reverseOrder());

        for (String followee : followees) {

            final TreeMap<Long, String> tweet = tweets.get(followee);

            if (tweet == null) {
                continue;
            }

            feed.putAll(tweet);
        }

        return feed;
    }

    @Override
    public boolean addFollower(String user, String follower) throws StorageException {

        ArrayList<String> currentList = following.get(follower);

        if (currentList == null) {

            currentList = new ArrayList<>();
        } else if (currentList.contains(user)) {

            return false;
        }

        currentList.add(user);

        following.put(follower, currentList);

        return true;
    }
}
