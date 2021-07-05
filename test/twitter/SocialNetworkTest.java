/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
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

public class SocialNetworkTest {

    /*
     * TODO: your testing strategies for these methods should go here.
     * See the ic03-testing exercise for examples of what a testing strategy comment looks like.
     * Make sure you have partitions.
     * 
     * Testing strategy
     * 
     * GuessFollowsGraph:
     * Partitions the inputs as follows:
     * List of tweets with size: 0, 1, 2, >2
     * tweets: no mutation
     * map values: no username repetitions, key not in map values
     * map keys: no username repetition
     * username: not case sensitive
     * 
     * influencers:
     * test against expected value, allow arbitrary order for usernames with same followers count
     */
	
	private static final Instant d1 = Instant.parse("2016-02-17T09:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    
    //tweets for follows graph
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "bbitdiddle", "@A1ysa maybe not for you", d2);
    private static final Tweet tweet4 = new Tweet(4, "bbitdiddle", "Maybe not for you @Aly_sa @bbitdiddle", d2);
    private static final Tweet tweet5 = new Tweet(5, "bbitdiddle", "Hello @Alysa maybe not for you", d2);
    private static final Tweet tweet6 = new Tweet(6, "bbitdiddle", "alysa@A1ysa maybe not for you", d2);
    private static final Tweet tweet7 = new Tweet(7, "bbitdiddle", "Hey @a1ysa maybe not for you", d2);
    private static final Tweet tweet8 = new Tweet(8, "bbitdiddle", "Hey @a1ysa talk to me", d2);
    
    //tweets for influencers test
    private static final Tweet tweet11 = new Tweet(11, "user1", "@user2 @user4", d1); //1 follows 2, 4
    private static final Tweet tweet12 = new Tweet(12, "user2", "@user1 @user3", d1); //2 follows 1, 3
    private static final Tweet tweet13 = new Tweet(13, "user3", "@user1", d1); //		3 follows 1
    private static final Tweet tweet14 = new Tweet(14, "user4", "@user1 @user2", d1); //4 follows 1, 2
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    
    //covers tweets = 0
    @Test
    public void testGuessFollowsGraphEmpty() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(new ArrayList<>());
        
        assertTrue("expected empty graph", followsGraph.isEmpty());
    }
    
    @Test
    public void testInfluencersEmpty() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        assertTrue("expected empty list", influencers.isEmpty());
    }

    /*
     * Warning: all the tests you write here must be runnable against any
     * SocialNetwork class that follows the spec. It will be run against several
     * staff implementations of SocialNetwork, which will be done by overwriting
     * (temporarily) your version of SocialNetwork with the staff's version.
     * DO NOT strengthen the spec of SocialNetwork or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in SocialNetwork, because that means you're testing a
     * stronger spec than SocialNetwork says. If you need such helper methods,
     * define them in a different class. If you only need them in this test
     * class, then keep them in this test class.
     */
    
    //covers guessFollowsGraph
    //covers no mutation, 2 tweets
    @Test
    public void guessFollowsGraphMutation() {
    	List<Tweet> tweets = Arrays.asList(tweet1, tweet2);
    	SocialNetwork.guessFollowsGraph(tweets);
    	
    	assertEquals("Expected no tweets mutation", Arrays.asList(tweet1, tweet2), tweets);
    }
    
    //covers no username repetition in map values, tweets > 2
    @Test
    public void guessFollowsGraphNoUsernameRepetitionMapValues() {
    	Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet3, tweet7, tweet8));
    	
    	Set<String> expectedValue = new HashSet<>();
    	expectedValue.add("a1ysa");
    	
    	assertTrue("Expected no username repetition", followsGraph.values().contains(expectedValue));
    	assertEquals("Expected 1 value for key 'bbitdiddle'", 1, followsGraph.values().size());
    }
    
    //covers no key in map values, 1 tweet
    @Test
    public void guessFollowsGraphNoKeyInMapValues() {
    	Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet4));
    	
    	Set<String> expectedValue1 = new HashSet<>();
    	expectedValue1.add("bbitdiddle");
    	
    	Set<String> expectedValue2 = new HashSet<>();
    	expectedValue2.add("aly_sa");
    	
    	assertFalse("Expected no key 'bbitdiddle' in map values", followsGraph.values().contains(expectedValue1));
    	assertTrue("Expected value 'aly_sa' in map", followsGraph.values().contains(expectedValue2));
    }
    
    //covers not case sensitive
    @Test
    public void guessFollowsGraphUsernameNotCaseSensitive() {
    	Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet3));
    	
    	Set<String> expectedValue = new HashSet<>();
    	expectedValue.add("a1ysa");
    	
    	assertTrue("Expected key in lower case", followsGraph.containsKey("bbitdiddle"));
    	assertTrue("Expected value in lower case", followsGraph.values().contains(expectedValue));
    }
    
    //covers influencers
    @Test
    public void influencersExpectedValues() {
    	Map<String, Set<String>> followsMap = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet11, tweet12, tweet13, tweet14));
    	List<String> influencers = SocialNetwork.influencers(followsMap);
    	
    	assertEquals("Expected user1 at position 0", "user1", influencers.get(0));
    	assertEquals("Expected user2 at position 1", "user2", influencers.get(1));
    }

}
