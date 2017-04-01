package com.awoisoak.visor.data.source;

import com.awoisoak.visor.data.source.responses.ListsPostsResponse;
import com.awoisoak.visor.data.source.responses.MediaFromPostResponse;

/**
 * Interface for the Restful API Manager
 */

public interface WPAPI {

    /**
     * List all posts in the website
     *
     * @param offset, Offset the result set by a specific number of items.
     * @return
     */
    void listPosts(int offset, WPListener<ListsPostsResponse> l);


    /**
     * Retrieve all media from a post
     *
     * @param parent, post Id
     * @return
     */

    void retrieveAllMediaFromPost(String parent, WPListener<MediaFromPostResponse>l);

}
