package com.awoisoak.visor.data.source;

/**
 * Image object
 */

public class Image {

    String thumbnail;
    String square;
    String medium;
    String medium_large;
    String large;
    String full;


    public Image(String thumbnail,String square,String medium, String medium_large,String large,
                  String full) {
        this.thumbnail = thumbnail;
        this.square = square;
        this.medium = medium;
        this.medium_large = medium_large;
        this.large = large;
        this.full = full;
    }

    public String getFull() {
        return full;
    }

    public String getLarge() {
        return large;
    }

    public String getMedium() {
        return medium;
    }

    public String getMedium_large() {
        return medium_large;
    }

    public String getSquare() {
        return square;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}
