package com.awoisoak.visor.data.source;

import com.awoisoak.visor.data.source.responses.ListPostsResponse;
import com.awoisoak.visor.data.source.responses.MediaFromPostResponse;

/**
 * Interface for the Restful API Manager
 */

public interface WPAPI {

    /**
     * List all posts in the website
     *
     * @param page, current page of the collection
     * @return
     */
    void listPosts(int page, WPListener<ListPostsResponse> l);


    /**
     * Retrieve all media from a post
     *
     * @param parent, post Id
     * @return
     */

    void retrieveAllMediaFromPost(String parent, WPListener<MediaFromPostResponse>l);

}
