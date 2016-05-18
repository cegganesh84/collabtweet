package com.collabtweet.service;

import com.collabtweet.exception.StorageException;

import java.util.TreeMap;

/**
 * Created by kganesh on 5/17/16.
 */
public interface TweetService {

    boolean storeTweet(String user, String tweet) throws StorageException;

    TreeMap<Long, String> getFeed(String user) throws StorageException;

    boolean addFollower(String user, String follower) throws StorageException;
}
