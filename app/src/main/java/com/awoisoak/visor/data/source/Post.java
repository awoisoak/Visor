package com.awoisoak.visor.data.source;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Post Object
 */
@DatabaseTable(tableName = "posts")

public class Post {
    public final static String ID = "id";
    public final static String TITLE = "title";
    public final static String CREATION_DATE = "creationDate";
    public final static String MODIFICATION_DATE = "modificationDate";
    public final static String FEATURED_IMAGE = "featuredImage";
    public final static String FEATURED_IMAGE_SMALL = "featuredImageSmall";
    public final static String WP_ATTACHMENT = "wpAttachment";
    public final static String CONTENT = "content";

    @DatabaseField(id = true, columnName = ID)
    String id;

    @DatabaseField(columnName = TITLE, dataType = DataType.STRING, canBeNull = false)
    String title;

    @DatabaseField(columnName = CREATION_DATE, dataType = DataType.STRING, canBeNull = false)
    String creationDate;

    @DatabaseField(columnName = MODIFICATION_DATE, dataType = DataType.STRING, canBeNull = false)
    String modificationDate;

    @DatabaseField(columnName = FEATURED_IMAGE, dataType = DataType.STRING, canBeNull = false)
    String featuredImage;

    @DatabaseField(columnName = FEATURED_IMAGE_SMALL, dataType = DataType.STRING, canBeNull = false)
    String featuredImageSmall;

    @DatabaseField(columnName = WP_ATTACHMENT, dataType = DataType.STRING, canBeNull = false)
    String wpAttachment;

    @DatabaseField(columnName = CONTENT, dataType = DataType.STRING, canBeNull = false)
    String content;

    public Post() {
    }

    public Post(String id, String date, String modificationDate, String title, String wpFeaturedMedia,
                String wpFeaturedMediaSmall,
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
     *
     * @return
     */
    public String getFeaturedImage() {
        return featuredImage;
    }

    /**
     * Features image with {@link Image#smallSize} size
     *
     * @return
     */
    public String getFeaturedImageSmall() {
        return featuredImageSmall;
    }

    /**
     * Get the content of the post itself (texts and URL to images)
     *
     * @return
     */
    public String getContent() {
        return content;
    }
}
