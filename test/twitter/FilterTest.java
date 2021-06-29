/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class FilterTest {

    /*
     * TODO: your testing strategies for these methods should go here.
     * See the ic03-testing exercise for examples of what a testing strategy comment looks like.
     * Make sure you have partitions.
     * 
     * Testing strategy
     * 
     * Filter by username:
     * Partitions the inputs as follows:
     * list of tweets with size 0, 1, 2, >2
     * 
     * tweets: no mutation
     * tweets: return in same order as tweets input list
     * username: only valid chars
     * username: not case sensitive
     * 
     * Filter by Timespan:
     * tweets: in Timespan, at border of Timespan, out of Timespan
     * tweets: no mutation
     * tweets: return in same order as tweets input list
     * 
     * Filter by words:
     * tweets: no mutation
     * tweets: return in same order as tweets input list
     * tweets: returns tweets containing AT LEAST ONE of the words
     * words: assumes single words, not case sensitive
     */
    
    private static final Instant d1 = Instant.parse("2016-02-17T09:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "bbitdiddle", "@A1ysa maybe not for you", d2);
    private static final Tweet tweet4 = new Tweet(4, "bbitdiddle", "Maybe not for you @Aly_sa", d2);
    private static final Tweet tweet5 = new Tweet(5, "bbitdiddle", "Hello @Alysa maybe not for you", d2);
    private static final Tweet tweet6 = new Tweet(6, "bbitdiddle", "alysa@A1ysa maybe not for you", d2);
    private static final Tweet tweet7 = new Tweet(7, "bbitdiddle", "Hey @a1ysa maybe not for you", d2);
    private static final Tweet tweet8 = new Tweet(8, "bbitdiddle", "Hey @a1ysa talk to me", d2);
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testWrittenByMultipleTweetsSingleResult() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2), "alyssa");
        
        assertEquals("expected singleton list", 1, writtenBy.size());
        assertTrue("expected list to contain tweet", writtenBy.contains(tweet1));
    }
    
    @Test
    public void testInTimespanMultipleTweetsMultipleResults() {
        Instant testStart = Instant.parse("2016-02-17T09:00:00Z");
        Instant testEnd = Instant.parse("2016-02-17T12:00:00Z");
        
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2), new Timespan(testStart, testEnd));
        
        assertFalse("expected non-empty list", inTimespan.isEmpty());
        assertTrue("expected list to contain tweets", inTimespan.containsAll(Arrays.asList(tweet1, tweet2)));
        assertEquals("expected same order", 0, inTimespan.indexOf(tweet1));
    }
    
    @Test
    public void testContaining() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("talk"));
        
        assertFalse("expected non-empty list", containing.isEmpty());
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet1, tweet2)));
        assertEquals("expected same order", 0, containing.indexOf(tweet1));
    }

    /*
     * Warning: all the tests you write here must be runnable against any Filter
     * class that follows the spec. It will be run against several staff
     * implementations of Filter, which will be done by overwriting
     * (temporarily) your version of Filter with the staff's version.
     * DO NOT strengthen the spec of Filter or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in Filter, because that means you're testing a stronger
     * spec than Filter says. If you need such helper methods, define them in a
     * different class. If you only need them in this test class, then keep them
     * in this test class.
     */
    
    //covers filter by username
    //covers no results for valid username, tweets > 2
    @Test
    public void testWrittenByNoResult() {
    	List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet2, tweet3, tweet4, tweet5), "alyssa");
    	
    	assertTrue("Expected empty list", writtenBy.isEmpty());
    }
    
    //covers tweets = 0
    @Test
    public void testWrittenByEmptyList() {
    	List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(), "alyssa");
    	
    	assertTrue("Expected empty list", writtenBy.isEmpty());
    }
    
    //covers tweets = 1
    @Test
    public void testWrittenByOneTweet() {
    	List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet2), "bbitdiddle");
    	
    	assertEquals("Expected list size 1", 1, writtenBy.size());
    }
    
    //covers tweets = 2
    @Test
    public void testWrittenByTwoTweets() {
    	List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2), "bbitdiddle");
    	
    	assertEquals("Expected list size 1", 1, writtenBy.size());
    }
    
    //covers username not case sensitive
    @Test
    public void testWrittenByNotCaseSensitive() {
    	List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2, tweet3), "BbitDiddle");
    	
    	assertEquals("Expected list size 2", 2, writtenBy.size());
    }
    
    //covers mutation
    @Test
    public void testWrittenByMutation() {
    	List<Tweet> tweets = Arrays.asList(tweet6, tweet7);
    	Filter.writtenBy(tweets, "bbitdiddle");
    	
    	assertEquals("Expected unmutated list", Arrays.asList(tweet6, tweet7), tweets);
    }
    
    //covers filter by Timespan
    //covers at the border of timespan, same order as tweets input
    @Test
    public void testInTimespanAtBorder() {
    	Instant testStart = Instant.parse("2016-02-17T09:00:00Z");
        Instant testEnd = Instant.parse("2016-02-17T11:00:00Z");
        
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2), new Timespan(testStart, testEnd));
        
        assertFalse("expected non-empty list", inTimespan.isEmpty());
        assertTrue("expected list to contain tweets", inTimespan.containsAll(Arrays.asList(tweet1, tweet2)));
        assertEquals("expected same order", 0, inTimespan.indexOf(tweet1));
    }
    
    //covers out of timespan
    @Test
    public void testInTimespanOut() {
    	Instant testStart = Instant.parse("2016-02-17T10:00:01Z");
    	Instant testEnd = Instant.parse("2016-02-17T10:59:00Z");
    	
    	//private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    	//private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    	
    	List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2), new Timespan(testStart, testEnd));
    	
    	assertTrue("Expected empty list", inTimespan.isEmpty());
    }
    
    //covers no mutation
    @Test
    public void testInTimespanMutation() {
    	Instant testStart = Instant.parse("2016-02-17T10:00:00Z");
        Instant testEnd = Instant.parse("2016-02-17T11:00:00Z");
        
        List<Tweet> tweets = Arrays.asList(tweet4, tweet5);
        Filter.inTimespan(tweets, new Timespan(testStart, testEnd));
        
        assertEquals("Expected unmutated list", Arrays.asList(tweet4, tweet5), tweets);
    }
    
    //covers filter by words
    //covers no mutation
    @Test
    public void testContainingMutation() {
    	List<Tweet> tweets = Arrays.asList(tweet1, tweet2);
        Filter.containing(tweets, Arrays.asList("talk"));
        
        assertEquals("Expected unmutated list", Arrays.asList(tweet1, tweet2), tweets);
    }
    
    //covers same order as tweets input
    
    //covers one word filter, word not case sensitive
    @Test
    public void testContainingCaseSensitive() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("Talk"));
        
        assertFalse("expected non-empty list", containing.isEmpty());
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet1, tweet2)));
        assertEquals("expected same order", 0, containing.indexOf(tweet1));
    }
    
    //covers two word filter, shows results for at least one word in each result
    @Test
    public void testContainingTwoWords() {
    	List<Tweet> containing = Filter.containing(Arrays.asList(tweet2, tweet3, tweet4, tweet5), Arrays.asList("talk", "hello"));
    	
    	assertEquals("Expected list size = 2", 2, containing.size());
    }
    
    //covers no tweet repetition
    @Test
    public void testContainingNoRepetition() {
    	List<Tweet> containing = Filter.containing(Arrays.asList(tweet2, tweet3, tweet4, tweet5), Arrays.asList("talk", "30"));
    	
    	assertEquals("Expected list size = 1", 1, containing.size());
    }
    
    //covers for correct order
    @Test
    public void testContainingCorrectOrder() {
    	List<Tweet> containing = Filter.containing(Arrays.asList(tweet2, tweet3, tweet4, tweet8), Arrays.asList("talk", "maybe"));
    	
    	assertEquals("Expected tweet8 at position 3", 3, containing.indexOf(tweet8));
    }

}
