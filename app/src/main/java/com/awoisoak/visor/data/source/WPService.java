package com.awoisoak.visor.data.source;

import com.awoisoak.visor.data.source.responses.ListsPostsResponse;
import com.awoisoak.visor.data.source.responses.MediaFromPostResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * WordPress services
 */

public interface WPService {


    //GENERIC
    String EMPTY_RESPONSE = "[]";

    //HEADERS
    String HEADER_TOTAL_PAGES = "X-WP-TotalPages";
    String HEADER_TOTAL_RECORDS = "X-WP-Total";

    //MEDIA
    String POST_ID = "post";
    String WIDTH = "width";
    String HEIGHT = "height";
    String MEDIA_DETAILS = "media_details";
    String SOURCE_URL = "source_url";
    String SIZES = "sizes";
    String THUMBNAIL_SIZE = "thumbnail";
    String SQUARE_SIZE = "square";
    String SMALL_SIZE = "small-size";
    String POST_BIG_SIZE = "post-big";
    String LARGE_SIZE = "large";
    String FULL_SIZE = "full";

    //POSTS
    String ID = "id";
    String DATE = "date";
    String MODIFIED = "modified";
    String RENDERED = "rendered";
    String TITLE = "title";
    String LINKS = "_links";
    String WP_ATTACHMENT = "wp:attachment";
    String HREF = "href";
    String EMBEDDED = "_embedded";
    String WP_FEATURED_MEDIA = "wp:featuredmedia";
    String CONTENT = "content";

    /**
     * This header is now required, otherwise the parsing will fail
     *
     * @param offset
     * @return
     */
    @Headers("Content-Type: application/json")

    /**
     * Retrieve the {@link WPAPI#MAX_NUMBER_POSTS_RETURNED}
     * posts given the passed offset.
     * <p>
     * ?_embed is added to the request in order to include the embedded media in the response (ex. featured media)
     * avoiding us to trigger a second request
     *
     * @param offset, current page of the collection
     * @return
     */
    @GET("/wp-json/wp/v2/posts?_embed")
    Call<ListsPostsResponse> getPosts(@Query("offset") int offset);

    /**
     * Retrieve the posts newer than the passed date
     * <p>
     * ?_embed is added to the request in order to include the embedded media in the response (ex. featured media)
     * avoiding us to trigger a second request
     *
     * @param date
     * @return
     */
    //TODO Max number of posts returned is 10 by default, we leave it like this?
    @GET("/wp-json/wp/v2/posts?_embed")
    Call<ListsPostsResponse> getLastPostsFrom(@Query("after") String date);


    /**
     * Retrieve the {@link WPAPI#MAX_NUMBER_IMAGES_RETURNED}
     * images from the specific post given the passed offset
     *
     * @param parent, parent post Id
     * @return
     */
    @GET("/wp-json/wp/v2/media")
    Call<MediaFromPostResponse> getImagesFromPost(@Query("parent") String parent, @Query("offset") int offset);

}
