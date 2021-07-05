/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * SocialNetwork provides methods that operate on a social network.
 * 
 * A social network is represented by a Map<String, Set<String>> where map[A] is
 * the set of people that person A follows on Twitter, and all people are
 * represented by their Twitter usernames. Users can't follow themselves. If A
 * doesn't follow anybody, then map[A] may be the empty set, or A may not even exist
 * as a key in the map; this is true even if A is followed by other people in the network.
 * Twitter usernames are not case sensitive, so "ernie" is the same as "ERNie".
 * A username should appear at most once as a key in the map or in any given
 * map[A] set.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class SocialNetwork {

    /**
     * Guess who might follow whom, from evidence found in tweets.
     * 
     * @param tweets
     *            a list of tweets providing the evidence, not modified by this
     *            method.
     * @return a social network (as defined above) in which Ernie follows Bert
     *         if and only if there is evidence for it in the given list of
     *         tweets.
     *         One kind of evidence that Ernie follows Bert is if Ernie
     *         @-mentions Bert in a tweet. This must be implemented. Other kinds
     *         of evidence may be used at the implementor's discretion.
     *         All the Twitter usernames in the returned social network must be
     *         either authors or @-mentions in the list of tweets.
     */
    public static Map<String, Set<String>> guessFollowsGraph(List<Tweet> tweets) {
        Set<String> socialNetwork = new HashSet<>();
        List<String> authors = new ArrayList<>();
        List<Tweet> filteredByAuthor = new ArrayList<>();
        Map<String, Set<String>> socialNetworkMap = new HashMap<>();
        
        //for loop trough tweet list, save authors to list
        for (Tweet tweet:tweets) {
        	if(!authors.contains(tweet.getAuthor())) {
        		authors.add(tweet.getAuthor());
        	}
        }

        //for every author in tweets list
        for (String author:authors) {
        	//filteredByAuthor.clear();
        	//socialNetwork.clear();
        	//filter tweets by author
        	filteredByAuthor = Filter.writtenBy(tweets, author);
        	//get mentions
        	socialNetwork = Extract.getMentionedUsers(filteredByAuthor);
        	//remove author from mentions
        	if (socialNetwork.contains(author)) {
        		socialNetwork.remove(author);
        	}
        	//save mentions to map
        	socialNetworkMap.put(author, socialNetwork);
        }
        return socialNetworkMap;
    }

    /**
     * Find the people in a social network who have the greatest influence, in
     * the sense that they have the most followers.
     * 
     * @param followsGraph
     *            a social network (as defined above)
     * @return a list of all distinct Twitter usernames in followsGraph, in
     *         descending order of follower count.
     */
    public static List<String> influencers(Map<String, Set<String>> followsGraph) {
    	List<String> listOfInfluencers = new ArrayList<>();
    	Map<String, Integer> mapOfInfluencers = new HashMap<>();
    	//for all authors in graph (keys set)
    	for (String author:followsGraph.keySet()) {
    		Integer followersCount = 0;
    		//for set of values (external set)
    		for (Set<String> externalSet:followsGraph.values()) {
    			//for set of mentions (internal set, since values() contains a set of String mentions)
    			for (String mention:externalSet) {
    				//count times author is mentioned
    				if (mention.equals(author)) {
    					followersCount++;
    				}
    			}
    		}
    		//asign times mentioned to author in map
    		mapOfInfluencers.put(author, followersCount);
    	}
    	//sort by removing key with max value from mapOfInfluencers
    	while (!mapOfInfluencers.isEmpty()) {
    		//find max value from mapOfInfluencers
        	Integer maxValueFollowers = 0;
        	for (Integer value:mapOfInfluencers.values()) {
        		if (value>maxValueFollowers) {
        			maxValueFollowers = value;
        		}
        	}
        	//find corresponding key in mapOfInfluencers to max value
        	for (String authorUnsorted:mapOfInfluencers.keySet()) {
        		if (mapOfInfluencers.get(authorUnsorted).equals(maxValueFollowers)) {
        			//add username to listOfInfluencers and remove username's key from mapOfInfluencers
        			listOfInfluencers.add(authorUnsorted);
        			mapOfInfluencers.remove(authorUnsorted);
        			break;
        		}
        	}
    	}
    	
    	return listOfInfluencers;
    }

}
