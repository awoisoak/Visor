package com.awoisoak.visor.data.source;

import com.awoisoak.visor.data.source.responses.ListPostsResponse;
import com.awoisoak.visor.data.source.responses.MediaFromPostResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * WordPress services
 */

public interface WPService {
    /**
     * List all posts in the website
     * @param page, current page of the collection
     * @return
     */
    @GET("/wp-json/wp/v2/posts")
    Call<ListPostsResponse> listPosts(@Query("page") int page);


    /**
     * Retrieve all media from a post
     *
     * @param parent, parent post Id
     * @return
     */
    @GET("/wp-json/wp/v2/media")
    Call<MediaFromPostResponse> retrieveAllMediaFromPost(@Query("parent") String parent);

}
