package com.awoisoak.visor.data.source;

import com.awoisoak.visor.data.source.responses.ListsPostsResponse;
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
     * ?_embed is added to the request in order to include the embedded media in the response (ex. featured media)
     * avoiding us to trigger a second request
     *
     * @param offset, current page of the collection
     * @return
     */
    @GET("/wp-json/wp/v2/posts?_embed")
    Call<ListsPostsResponse> listPosts(@Query("offset") int offset);


    /**
     * Retrieve all media from a post
     *
     * @param parent, parent post Id
     * @return
     */
    //TODO add offset
    @GET("/wp-json/wp/v2/media")
    Call<MediaFromPostResponse> retrieveAllMediaFromPost(@Query("parent") String parent);

}
