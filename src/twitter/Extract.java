/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Pattern;

/**
 * Extract consists of methods that extract information from a list of tweets.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class Extract {

    /**
     * Get the time period spanned by tweets.
     * 
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @return a minimum-length time interval that contains the timestamp of
     *         every tweet in the list.
     */
    public static Timespan getTimespan(List<Tweet> tweets) {
        if (tweets.isEmpty()) {
        	return new Timespan(Instant.now(), Instant.now());
        }
        else {
        	//sort tweets by time. No mutability since sortedTweets is a new list
        	List<Tweet> sortedTweets = new ArrayList<>(tweets);
        	sortedTweets.sort(Comparator.comparing(Tweet::getTimestamp));
        	//get Timestamp of first and last tweets
        	Instant start = sortedTweets.get(0).getTimestamp();
        	Instant end = sortedTweets.get(sortedTweets.size()-1).getTimestamp();
        	
        	return new Timespan(start, end);
        }
    }

    /**
     * Get usernames mentioned in a list of tweets.
     * 
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @return the set of usernames who are mentioned in the text of the tweets.
     *         A username-mention is "@" followed by a Twitter username (as
     *         defined by Tweet.getAuthor()'s spec).
     *         The username-mention cannot be immediately preceded or followed by any
     *         character valid in a Twitter username.
     *         For this reason, an email address like bitdiddle@mit.edu does NOT 
     *         contain a mention of the username mit.
     *         Twitter usernames are case-insensitive, and the returned set may
     *         include a username at most once.
     */
    public static Set<String> getMentionedUsers(List<Tweet> tweets) {
        Set<String> usernamesInText = new HashSet<>();
    	for (Tweet tweet:tweets) {
        	String text = tweet.getText();
        	String [] words = text.split("\\s");
        	for (String word:words) {
        		if (Pattern.matches("@([A-Za-z0-9_-]+)", word)) {
        			String newUsername = word.substring(1).toLowerCase();
        			if (!usernamesInText.contains(newUsername)) {
        				usernamesInText.add(newUsername);
        			}
        		}
        		
        	}
        }
    	return usernamesInText;
    }

}
