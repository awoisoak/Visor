package com.awoisoak.visor.data.source;

/**
 * Image object
 */

public class Image {

    String thumbnail;
    String square;
    String smallSize;
    String postBig;
    String large;
    String full;


    public Image(String thumbnail, String square, String smallSize, String postBig, String large,
                 String full) {
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
}
