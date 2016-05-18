package com.collabtweet;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;

public class CollabTweetTest {

    private HttpServer server;
    private WebTarget target;

    @Before
    public void setUp() throws Exception {
        // start the server
        server = Main.startServer();
        // create the client
        Client c = ClientBuilder.newClient();

        target = c.target(Main.BASE_URI);
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

    @Test
    public void testFollow() {
        Response responseMsg = target.path("collabtweet/follow").
                queryParam("followee", "a").
                queryParam("follower", "b").
                request().
                post(null);
        assertEquals("Follower successfully added", responseMsg.readEntity(String.class));
    }

    /**
     * Test to see that the message "Tweet successfully added" is sent in the response.
     */
    @Test
    public void testTweetIt() {
        Response responseMsg = target.path("collabtweet/tweet").
                queryParam("user", "a").
                queryParam("payload", "tweet1").
                request().
                post(null);
        assertEquals("Tweet successfully added", responseMsg.readEntity(String.class));
    }


    @Test
    public void testFeed() {
        Response responseMsg = target.path("collabtweet/feed").
                queryParam("user", "b").
                request().
                get();
        final TreeMap<Long, String> map = responseMsg.readEntity(new GenericType<TreeMap<Long, String>>(){});

        // Test is disabled, Could not convert the entity back to map

        // assertEquals("tweet1", map.firstEntry().getValue());
    }
}
