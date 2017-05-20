package com.awoisoak.visor.data.source;

import com.awoisoak.visor.data.source.responses.ListsPostsResponse;
import com.awoisoak.visor.data.source.responses.MediaFromPostResponse;

/**
 * Interface for the Restful API Manager
 */

public interface WPAPI {

    /**
     * Default values in WP
     * https://developer.wordpress.org/rest-api/reference/posts/#arguments
     */
    int MAX_NUMBER_POSTS_RETURNED = 10;
    int MAX_NUMBER_IMAGES_RETURNED = 10;

    /**
     * Retrieve the {@link com.awoisoak.visor.data.source.WPAPI#MAX_NUMBER_POSTS_RETURNED}
     * posts given the passed offset.
     *
     * @param offset, Offset the result set by a specific number of items.
     * @return
     */
    void getPosts(int offset, WPListener<ListsPostsResponse> l);


    /**
     * Retrieve the posts newer than the passed date
     *
     * @param date, date
     * @return
     */

    void getLastPostsFrom(String date, WPListener<ListsPostsResponse>l);

    /**
     * Retrieve the {@link com.awoisoak.visor.data.source.WPAPI#MAX_NUMBER_IMAGES_RETURNED}
     * images from the specific post given the passed offset
     *
     * @param parent, post Id
     * @return
     */

    void getImagesFromPost(String parent, int offset, WPListener<MediaFromPostResponse>l);




}
