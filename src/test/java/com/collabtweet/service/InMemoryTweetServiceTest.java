package com.collabtweet.service;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;

import static org.testng.Assert.*;

/**
 * Created by kganesh on 5/17/16.
 */
public class InMemoryTweetServiceTest {

    InMemoryTweetService inMemoryTweetService;

    @BeforeMethod
    public void setUp() {

        inMemoryTweetService = new InMemoryTweetService();
    }

    @org.testng.annotations.Test
    public void testStoreTweet() throws Exception {

        Assert.assertTrue(inMemoryTweetService.storeTweet("a", "tweet1"));
        Assert.assertFalse(inMemoryTweetService.storeTweet("a", "tweet1"));
        Assert.assertTrue(inMemoryTweetService.storeTweet("a", "tweet2"));

    }

    @org.testng.annotations.Test
    public void testGetFeed() throws Exception {

        inMemoryTweetService.storeTweet("a", "tweet1");
        inMemoryTweetService.storeTweet("b", "tweet2");

        inMemoryTweetService.addFollower("a", "c");
        inMemoryTweetService.addFollower("b", "c");

        Assert.assertEquals(inMemoryTweetService.getFeed("c").size(), 2);
    }

    @org.testng.annotations.Test
    public void testAddFollower() throws Exception {

        Assert.assertTrue(inMemoryTweetService.addFollower("a", "b"));
        Assert.assertFalse(inMemoryTweetService.addFollower("a", "b"));
        Assert.assertTrue(inMemoryTweetService.addFollower("a", "c"));
        Assert.assertTrue(inMemoryTweetService.addFollower("c", "a"));
    }
}
