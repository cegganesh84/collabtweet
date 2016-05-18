package com.collabtweet;

import com.collabtweet.exception.StorageException;
import com.collabtweet.service.InMemoryTweetService;
import com.collabtweet.service.TweetService;
import com.google.common.base.Strings;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Root resource (exposed at "collabtweet" path)
 */
@Path("collabtweet")
public class CollabTweet {

    TweetService tweetService = new InMemoryTweetService();

    /**
     * Method handling HTTP POST requests. The returned object will be sent
     * to the client as "application/json" media type.
     *
     * @return String that will be returned as a application/json response.
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("tweet")
    public Response tweetIt(@QueryParam("user") String user, @QueryParam("payload") String payload) {

        // Need to authenticate user is the current user

        if (Strings.isNullOrEmpty(user) || Strings.isNullOrEmpty(payload)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid Query Parameter: user and payload should not be empty").build();
        }

        boolean status;

        try {
            status = tweetService.storeTweet(user, payload);
        } catch (StorageException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Request could not be completed because of backend error").build();
        }

        if (status) {
            return Response.status(Response.Status.OK).entity("Tweet successfully added").build();
        }

        return Response.status(Response.Status.BAD_REQUEST).entity("Tweet already added").build();
    }

    /**
     * Method handling HTTP POST requests. The returned object will be sent
     * to the client as "application/json" media type.
     *
     * @return String that will be returned as a application/json response.
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("follow")
    public Response follow(@QueryParam("follower") String follower, @QueryParam("followee") String followee) {

        // Need to authenticate follower is the current user
        
        if (Strings.isNullOrEmpty(follower) || Strings.isNullOrEmpty(followee)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid Query Parameter: follower and followee should not be empty").build();
        }
        
        boolean status;

        try {
            status = tweetService.addFollower(followee, follower);
        } catch (StorageException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Request could not be completed because of backend error").build();
        }
        
        if (status) {
            return Response.status(Response.Status.OK).entity("Follower successfully added").build();
        }
        
        return Response.status(Response.Status.BAD_REQUEST).entity("Follower-Followee relation already added").build();
    }

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "application/json" media type.
     *
     * @return String that will be returned as a application/json response.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("feed")
    public Response feed(@QueryParam("user") String user) {

        // Need to handle authentication and pagination

        if (Strings.isNullOrEmpty(user)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid Query Parameter: user should not be empty").build();
        }

        try {
            return Response.status(Response.Status.OK).entity(tweetService.getFeed(user)).build();
        } catch (StorageException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Request could not be completed because of backend error").build();
        }
    }
}
