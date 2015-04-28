/*
 * Created on 23.09.2008
 *
 */
package twitterbackup;


/**
 * Encapsulates user information. Twitter has been progressively putting more
 * and more information from the user's profile in here.
 * 
 * @author burkardj
 * @version $Id: user.java 4002 2011-06-26 09:45:15Z johann $
 */
public class user {

    long id;

    String name;

    String screen_name;

    String location;

    String description;

    String profile_image_url;
    
    String profile_image_url_https;

    String url;

    boolean _protected;

    int followers_count;

    String profile_background_color;

    String profile_text_color;

    String profile_link_color;

    String profile_sidebar_fill_color;

    String profile_sidebar_border_color;

    int friends_count;

    String created_at;

    int favourites_count;

    int utc_offset;

    String time_zone;

    String profile_background_image_url;
    
    String profile_background_image_url_https;

    boolean profile_background_tile;
    
    boolean profile_use_background_image;
    
    boolean notifications;
    
    boolean geo_enabled;
    
    boolean verified;
    
    boolean following;

    int statuses_count;
    
    String lang;
    
    boolean contributors_enabled;
    
    boolean follow_request_sent;
    
    String listed_count;
    
    boolean show_all_inline_media;
    
    String default_profile;
    
    String default_profile_image;
    
    String is_translator;

}
