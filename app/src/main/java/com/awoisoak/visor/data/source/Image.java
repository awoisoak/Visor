package com.awoisoak.visor.data.source;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Image object
 */
@DatabaseTable(tableName = "images")

public class Image {
    public final static String ID = "id";
    public final static String POST_ID = "postId";
    public final static String THUMBNAIL = "thumbnail";
    public final static String SQUARE = "square";
    public final static String SMALL_SIZE = "smallSize";
    public final static String POST_BIG = "postBig";
    public final static String LARGE = "large";
    public final static String FULL = "full";


    /**
     * Image id
     */
    @DatabaseField(id = true, columnName = ID)
    String id;


    /**
     * POST id which the image belongs to
     */
    @DatabaseField(columnName = POST_ID, dataType = DataType.STRING)
    String postId;

    /**
     * ~150x150
     */
    @DatabaseField(columnName = THUMBNAIL, dataType = DataType.STRING)
    String thumbnail;

    /**
     * ~400x400
     */
    @DatabaseField(columnName = SQUARE, dataType = DataType.STRING)
    String square;

    /**
     * ~400x267
     */
    @DatabaseField(columnName = SMALL_SIZE, dataType = DataType.STRING)
    String smallSize;

    /**
     * ~840x561
     */
    @DatabaseField(columnName = POST_BIG, dataType = DataType.STRING)
    String postBig;

    /**
     * ~1024x684
     */
    @DatabaseField(columnName = LARGE, dataType = DataType.STRING)
    String large;

    /**
     * ~2048x1368
     */
    @DatabaseField(columnName = FULL, dataType = DataType.STRING)
    String full;

    public Image() {
    }
    public Image(String id, String postId, String thumbnail, String square, String smallSize, String postBig, String large,
                 String full) {
        this.id = id;
        this.postId = postId;
        this.thumbnail = thumbnail;
        this.square = square;
        this.smallSize = smallSize;
        this.postBig = postBig;
        this.large = large;
        this.full = full;
    }

    /**
     * ~2048x1368
     *
     * @return
     */
    public String getFull() {
        return full;
    }

    /**
     * ~1024x684
     *
     * @return
     */
    public String getLarge() {
        return large;
    }

    /**
     * ~400x267
     *
     * @return
     */
    public String getSmallSize() {
        return smallSize;
    }

    /**
     * ~840x561
     *
     * @return
     */
    public String getPostBig() {
        return postBig;
    }

    /**
     * ~400x400
     *
     * @return
     */
    public String getSquare() {
        return square;
    }

    /**
     * ~150x150
     *
     * @return
     */
    public String getThumbnail() {
        return thumbnail;
    }

    /**
     * Post Id which this image belongs to
     * @return
     */
    public String getPostId() {
        return postId;
    }
}
