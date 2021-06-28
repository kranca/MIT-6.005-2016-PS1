/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Set;
import java.util.List;

import org.junit.Test;

public class ExtractTest {

    /*
     * TODO: your testing strategies for these methods should go here.
     * See the ic03-testing exercise for examples of what a testing strategy comment looks like.
     * Make sure you have partitions.
     * 
     * Testing strategy
     * 
     * Partitions the inputs as follows:
     * tweets with list size: 0, 1, >1, >2
     * 
     * tweets: No mutation
     * usernames: 0, 1, >1
     * usernames: upper and lower case, special char, not username
     * username: at start of tweet, middle of tweet, end of tweet
     * username: not repeated
     * 
     */
    
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-02-17T12:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "bbitdiddle", "@A1ysa maybe not for you", d3);
    private static final Tweet tweet4 = new Tweet(4, "bbitdiddle", "Maybe not for you @Aly_sa", d3);
    private static final Tweet tweet5 = new Tweet(5, "bbitdiddle", "Hello @Alysa maybe not for you", d3);
    private static final Tweet tweet6 = new Tweet(6, "bbitdiddle", "alysa@A1ysa maybe not for you", d3);
    private static final Tweet tweet7 = new Tweet(6, "bbitdiddle", "Hey @a1ysa maybe not for you", d3);
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testGetTimespanTwoTweets() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2));
        
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d2, timespan.getEnd());
    }
    
  //covers usernames = 0
    @Test
    public void testGetMentionedUsersNoMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1));
        
        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }

    /*
     * Warning: all the tests you write here must be runnable against any
     * Extract class that follows the spec. It will be run against several staff
     * implementations of Extract, which will be done by overwriting
     * (temporarily) your version of Extract with the staff's version.
     * DO NOT strengthen the spec of Extract or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in Extract, because that means you're testing a
     * stronger spec than Extract says. If you need such helper methods, define
     * them in a different class. If you only need them in this test class, then
     * keep them in this test class.
     */
    
    //covers tweets = 0
    @Test
    public void testGetTimespanNoTweets() {
    	Timespan timespan = Extract.getTimespan(Arrays.asList());
    	
    	assertEquals("expected start equeal end", timespan.getEnd(), timespan.getStart());
    	//assertEquals("expected end", d4, timespan.getEnd());
    }
    
    //covers tweets = 1
    @Test
    public void testGetTimespanOneTweet() {
    	Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1));
    	
    	assertEquals("expected start", d1, timespan.getStart());
    	assertEquals("expected end", d1, timespan.getEnd());
    }
    
    //covers tweets > 2
    @Test
    public void testGetTimespanThreeTweets() {
    	Timespan timespan3 = Extract.getTimespan(Arrays.asList(tweet1, tweet2, tweet3));
    	
    	assertEquals("expected start", d1, timespan3.getStart());
    	assertEquals("expected end", d3, timespan3.getEnd());
    }
    
    //covers mutation
    @Test
    public void testMutation() {
    	List<Tweet> tweets = Arrays.asList(tweet4, tweet5);
    	Extract.getTimespan(tweets);
    	
    	assertEquals("expected unmutated list", Arrays.asList(tweet4, tweet5), tweets);
    }
    
    //covers usernames = 1, upper and lower case, username at beginning of message
    @Test
    public void testGetOneUsernames() {
    	Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet3));
    	assertEquals("Expected set size 1", 1, mentionedUsers.size());
    	assertTrue("Expected a1ysa", mentionedUsers.contains("a1ysa"));
    }
    
    //covers usernames > 1, special char, username at the end of message, username at the middle of message
    @Test
    public void testGetThreeUsernames() {
    	Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet3, tweet4, tweet5));
    	assertEquals("Expected set size 3", 3, mentionedUsers.size());
    	assertTrue("Expected a1ysa", mentionedUsers.contains("a1ysa"));
    	assertTrue("Expected aly_sa", mentionedUsers.contains("aly_sa"));
    	assertTrue("Expected alysa", mentionedUsers.contains("alysa"));
    }
    
    //covers false username
    @Test
    public void testGetFalseUsersname() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet6));
        
        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }
    
    //no username repeats
    @Test
    public void testNoUsernameRepeats() {
    	Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet3, tweet7));
    	assertEquals("Expected 1 username", 1, mentionedUsers.size());
    }

}
