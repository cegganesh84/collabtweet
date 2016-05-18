# collabtweet
Twitter for developer collaboration

# Api doc

TODO: User swagger framework to create API doc straight out of Jersey application in this project

# Architecture Notes

1. Store user tweets in a column oriented data store like HBase
  1. User id will be the row key
  2. In column family followers, store the followers as column name
    1. Using this quickly we can check if a user is already a follower or not
  3. In column family tweets, store the tweet under the current time as the column name
    1. Using this we can get all the columns of the following users' tweet and merge them by timeline

# Architecture Challenges

1. Precooking the feed vs. Constructing the feed at runtime
  1. Precooking would need lot of space to store and lot of maintainance to keep it updated. (when a new user is followed or the following user posts a tweet)
    1. User may not visit the page also (Hard to justify for all the users)
    2. Horizontal scaling is harder because if more users are added we may need more storage space (exponential growth against linear user growth)
  1. Constructing the feed at runtime is costly, page load time will be slow
    1. Horizontal scaling is somewhat easier because we can manage by adding more compute nodes (linear growth with user growth) 
2. Pagination of user feed is hard to support
  1. Some techniques could be supporting pagination like tweets after a time.
    1. Front end can display 10 latest tweets.
    2. When user clicks load more.. get 10 more tweets after the time of last tweet displayed
