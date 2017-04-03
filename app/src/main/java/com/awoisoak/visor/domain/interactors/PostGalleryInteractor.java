package com.awoisoak.visor.domain.interactors;

public interface PostGalleryInteractor {
    /**
     * Default value in WP
     * https://developer.wordpress.org/rest-api/reference/media/#arguments
     */
    public static int MAX_NUMBER_IMAGES_RETURNED = 10;

    void getImages(String postId);
}
