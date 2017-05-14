package com.awoisoak.visor.data.source;

/**
 * Post Object
 */

public class Post {
    String id;
    String title;
    String creationDate;
    String modificationDate;
    String featuredImage;
    String featuredImageSmall;
    String wpAttachment;
    String content;


    public Post(String id, String date, String modificationDate, String title, String wpFeaturedMedia,String wpFeaturedMediaSmall,
                String wpAttachment, String content) {
        this.id = id;
        this.creationDate = date;
        this.modificationDate = modificationDate;
        this.title = title;
        this.featuredImage = wpFeaturedMedia;
        this.featuredImageSmall = wpFeaturedMediaSmall;
        this.wpAttachment = wpAttachment;
        this.content = content;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public String getId() {
        return id;
    }

    public String getModificationDate() {
        return modificationDate;
    }

    public String getTitle() {
        return title;
    }

    public String getWpAttachment() {
        return wpAttachment;
    }

    /**
     * Features image with size {@link Image#postBig} or {@link Image#large}' size
     * @return
     */
    public String getFeaturedImage() {
        return featuredImage;
    }
    /**
     * Features image with {@link Image#smallSize} size
     * @return
     */
    public String getFeaturedImageSmall() {
        return featuredImageSmall;
    }

    /**
     * Get the content of the post itself (texts and URL to images)
     * @return
     */
    public String getContent() {
        return content;
    }
}
