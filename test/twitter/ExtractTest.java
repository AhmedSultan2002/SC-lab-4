/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Set;

import org.junit.Test;

public class ExtractTest {


    /*
 * Testing strategy for getTimespan():
 * 
 * Partition the input list of tweets by:
 * - Number of tweets: 0, 1, >1
 * - Whether all tweets have the same timestamp or different timestamps
 * - Timestamps are in increasing, decreasing, or random order
 * - Tweets are at the boundaries of the timespan (earliest and latest)
 * 
 * We will test the following cases:
 * - No tweets (empty list)
 * - One tweet
 * - Multiple tweets with different timestamps (testing minimum and maximum times)
 * - Multiple tweets with identical timestamps
 * - Tweets with timestamps in increasing order, decreasing order, and random order
 * 
 * 
 * Testing strategy for getMentionedUsers():
 * 
 * Partition the input list of tweets by:
 * - Number of tweets: 0, 1, >1
 * - Number of mentioned users in the tweet text: 0, 1, >1
 * - Usernames case: all lowercase, all uppercase, mixed case
 * - Tweets containing invalid mentions (e.g., part of email addresses or mentions with invalid characters)
 * - Multiple mentions of the same user in the same tweet
 * - Mentions in different tweets (duplicates across tweets should only appear once in the result set)
 * 
 * We will test the following cases:
 * - No tweets (empty list)
 * - One tweet with no mentions
 * - One tweet with a valid mention
 * - Multiple tweets with valid mentions
 * - Tweets with mixed-case mentions (testing case-insensitivity)
 * - Tweets with invalid mentions (e.g., email addresses)
 * - Duplicate mentions of the same user in the same tweet
 * - Duplicate mentions of the same user across multiple tweets
 * 
 */

    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-02-17T09:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "alyssa", "mentioning @user in this tweet", d3);
    private static final Tweet tweet4 = new Tweet(4, "alyssa", "@user1 and @user2 are mentioned here", d1);
    private static final Tweet tweet5 = new Tweet(5, "alyssa", "mentioning email@domain.com", d2);
    
    @Test(expected = AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    // Test cases for getTimespan()
    @Test(expected = IllegalArgumentException.class)
    public void testGetTimespanNoTweets() {
        Extract.getTimespan(Arrays.asList());
    }
    
    
    @Test
    public void testGetTimespanOneTweet() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1));
        assertEquals("expected start and end to be same", d1, timespan.getStart());
        assertEquals("expected start and end to be same", d1, timespan.getEnd());
    }
    
    @Test
    public void testGetTimespanSameTimestamp() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet4));
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d1, timespan.getEnd());
    }

    @Test
    public void testGetTimespanDifferentTimestamps() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2, tweet3));
        assertEquals("expected start", d3, timespan.getStart());
        assertEquals("expected end", d2, timespan.getEnd());
    }
    
    // Test cases for getMentionedUsers()
    @Test
    public void testGetMentionedUsersNoMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1));
        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }
    
    @Test
    public void testGetMentionedUsersOneMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet3));
        assertEquals("expected single user", Set.of("user"), mentionedUsers);
    }
    
    @Test
    public void testGetMentionedUsersMultipleMentionsInOneTweet() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet4));
        assertEquals("expected two users", Set.of("user1", "user2"), mentionedUsers);
    }
    
    @Test
    public void testGetMentionedUsersMultipleMentionsAcrossTweets() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet3, tweet4));
        assertEquals("expected three users", Set.of("user", "user1", "user2"), mentionedUsers);
    }

    @Test
    public void testGetMentionedUsersCaseInsensitive() {
        Tweet tweetWithDifferentCase = new Tweet(6, "bbitdiddle", "mentioning @User and @user", d2);
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweetWithDifferentCase));
        assertEquals("expected one user", Set.of("user"), mentionedUsers);
    }
    
    @Test
    public void testGetMentionedUsersInvalidMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet5));
        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }
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


