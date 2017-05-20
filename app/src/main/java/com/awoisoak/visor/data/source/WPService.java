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
    public static String POST_ID = "post";
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
    public static String CONTENT="content";


    /**
     * Retrieve the {@link com.awoisoak.visor.domain.interactors.PostsRequestInteractor#MAX_NUMBER_POSTS_RETURNED}
     * posts given the passed offset.
     *
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
     *
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
     * Retrieve the {@link com.awoisoak.visor.domain.interactors.PostGalleryInteractor#MAX_NUMBER_IMAGES_RETURNED}
     * images from the specific post given the passed offset
     *
     * @param parent, parent post Id
     * @return
     */
    @GET("/wp-json/wp/v2/media")
    Call<MediaFromPostResponse> getImagesFromPost(@Query("parent") String parent, @Query("offset") int offset);

}
