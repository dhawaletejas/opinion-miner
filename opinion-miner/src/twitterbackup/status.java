/*
 * Created on 23.09.2008
 *
 */
package twitterbackup;


/**
 * Encapsulates a single status update aka "Tweet."
 * 
 * @author burkardj
 * @version $Id: status.java 4119 2011-08-11 17:14:11Z johann $
 */
public class status implements Comparable<status> {

    String created_at;

    long id;

    String text;

    String source;

    boolean truncated;

    boolean favorited;
    
    String in_reply_to_status_id;
    
    String in_reply_to_user_id;
    
    String in_reply_to_screen_name;
    
    String retweet_count;

    boolean retweeted;

    user user;
    
    String geo;
    
    String coordinates;
    
    String place;
    
    boolean possibly_sensitive;
    
    String contributors;

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof status)) {
            return false;
        }
        return id == ((status) obj).id;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return (int) (id ^ (id >> 32));
    }

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(status o) {
        return id > o.id ? -1 : (id == o.id ? 0 : 1);
    }

}
