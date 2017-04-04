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


    //GENERIC
    public static String EMPTY_RESPONSE="[]";

    //HEADERS
    public static String HEADER_TOTAL_PAGES="X-WP-TotalPages";
    public static String HEADER_TOTAL_RECORDS="X-WP-Total";

    //MEDIA
    public static String WIDTH="width";
    public static String HEIGHT="height";
    public static String MEDIA_DETAILS="media_details";
    public static String SOURCE_URL="source_url";
    public static String SIZES="sizes";
    public static String THUMBNAIL_SIZE="thumbnail";
    public static String SQUARE_SIZE="square";
    public static String SMALL_SIZE="small-size";
    public static String POST_BIG_SIZE="post-big";
    public static String LARGE_SIZE="large";
    public static String FULL_SIZE="full";

    //POSTS
    public static String ID="id";
    public static String DATE="date";
    public static String MODIFIED="modified";
    public static String RENDERED ="rendered";
    public static String TITLE="title";
    public static String LINKS ="_links";
    public static String WP_ATTACHMENT="wp:attachment";
    public static String HREF="href";
    public static String EMBEDDED="_embedded";
    public static String WP_FEATURED_MEDIA="wp:featuredmedia";


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
    Call<MediaFromPostResponse> retrieveAllMediaFromPost(@Query("parent") String parent, @Query("offset") int offset);

}
