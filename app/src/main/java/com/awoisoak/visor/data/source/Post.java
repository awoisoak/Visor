package com.awoisoak.visor.data.source;

/**
 * Post Object
 */

public class Post {
    String id;
    String title;
    String creationDate;
    String modificationDate;
    String wpFeaturedMedia;
    String wpAttachment;


    public Post(String id, String date, String modificationDate, String title, String wpFeaturedMedia,
                String wpAttachment) {
        this.id = id;
        this.creationDate = date;
        this.title = title;
        this.wpFeaturedMedia = wpFeaturedMedia;
        this.wpAttachment = wpAttachment;
    }
}
