package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import org.junit.Test;

public class MySocialNetworkTest {
	
	/*
     * Testing strategy
     * 
     * getHashtags:
     * Partitions the inputs as follows:
     * List of tweets with size: 0, 1, 2, >2
     * tweets: no mutation
     * hashtag: not case sensitive
     * 
     * influencers:
     * test against expected value, allow arbitrary order for usernames with same followers count
     * 
     * guessFollowsGraph:
     * Partitions the inputs as follows:
     * hashtags: not shared, shared by two users
     * 
     * combined case of user mention and shared hashtags without repetition in social network
     */
	
	private static final Instant d1 = Instant.parse("2016-02-17T09:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    
    //tweets for getHashtags
    private static final Tweet tweet1 = new Tweet(1, "user1", "This is a #HASHTAG", d1);
    private static final Tweet tweet2 = new Tweet(2, "user2", "I like your #hashtag", d2);
    private static final Tweet tweet3 = new Tweet(3, "user3", "My #hashtag", d2);
    private static final Tweet tweet4 = new Tweet(4, "user4", "#differenthashtag", d2);
    private static final Tweet tweet5 = new Tweet(5, "user5", "I like your #differenthashtag", d2);
    private static final Tweet tweet6 = new Tweet(6, "user6", "I like your differenthashtag", d2);
    private static final Tweet tweet7 = new Tweet(7, "user7", "@user1 #hashtag", d2);
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    //covers hashtags = 0
    @Test
    public void testNoHashtags() {
    	Set<String> hashtags = SocialNetwork.getHashtags(Arrays.asList(tweet6));
        
        assertTrue("expected empty set", hashtags.isEmpty());
    }
    
    //covers hashtags = 1, case insensitive
    @Test
    public void testOneHashtag() {
    	Set<String> hashtags = SocialNetwork.getHashtags(Arrays.asList(tweet1));
    	
    	assertTrue("Expected hashtag #hashtag in set", hashtags.contains("#hashtag"));
    }
    
    //covers hashtags = 2
    @Test
    public void testTwoHashtags() {
    	Set<String> hashtags = SocialNetwork.getHashtags(Arrays.asList(tweet1, tweet2));
    	
    	assertTrue("Expected hashtag #hashtag in set", hashtags.contains("#hashtag"));
    	assertEquals("Expected no repetition", 1, hashtags.size());
    }
    //covers hashtags > 2
    @Test
    public void testMultipleHashtags() {
    	Set<String> hashtags = SocialNetwork.getHashtags(Arrays.asList(tweet1, tweet2, tweet3, tweet4));
    	
    	assertTrue("Expected hashtag #hashtag in set", hashtags.contains("#hashtag"));
    	assertTrue("Expected hashtag #differenthashtag in set", hashtags.contains("#differenthashtag"));
    	assertEquals("Expected no repetition", 2, hashtags.size());
    }
    //covers mutation
    @Test
    public void mutation() {
    	List<Tweet> tweets = Arrays.asList(tweet5, tweet6);
    	SocialNetwork.getHashtags(tweets);
    	
    	assertEquals("Expected no mutation of list tweets", Arrays.asList(tweet5, tweet6), tweets);
    }
    //covers guessFollowsGraph
    //covers no shared hashtags
    @Test
    public void noSharedHashtags() {
    	Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1, tweet4));
    	
    	assertTrue("Expected empty set in network of user1", followsGraph.get("user1").isEmpty());
    	assertTrue("Expected empty set in network of user4", followsGraph.get("user4").isEmpty());
    }
    //covers hashtag shared by 2 users
    @Test
    public void sharedHashtag() {
    	Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1, tweet2, tweet4));
    	
    	assertTrue("Expected user2 in social network of user1", followsGraph.get("user1").contains("user2"));
    	assertTrue("Expected user1 in social network of user2", followsGraph.get("user2").contains("user1"));
    	assertTrue("Expected empty set in network of user4", followsGraph.get("user4").isEmpty());
    }
    //covers combined case of user mention and shared hashtags without repetition in social network
    @Test
    public void combinedCaseMentionHashtag() {
    	Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1, tweet7));
    	Set<String> noRepetition = new HashSet<>();
    	noRepetition.add("user1");
    	
    	assertEquals("Expected no username repetition in social network of user7", noRepetition, followsGraph.get("user7"));
    }

}
