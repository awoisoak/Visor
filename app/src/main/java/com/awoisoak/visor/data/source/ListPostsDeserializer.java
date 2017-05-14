package com.awoisoak.visor.data.source;

import com.awoisoak.visor.data.source.responses.ListsPostsResponse;
import com.awoisoak.visor.data.source.responses.WPResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Specific Deserializer for ListPostsResponse
 * <p>
 * http://demo.wp-api.org/wp-json/wp/v2/posts
 */


class ListPostsDeserializer<T extends WPResponse> implements JsonDeserializer<ListsPostsResponse> {


    public ListPostsDeserializer() {
    }


    /**
     * @param je
     * @param type
     * @param jdc
     * @return
     * @throws JsonParseException
     */
    @Override
    public ListsPostsResponse deserialize(JsonElement je, Type type, JsonDeserializationContext jdc)
            throws JsonParseException {

        JsonArray arrayOfPosts = je.getAsJsonArray();
        ArrayList<Post> postsList = new ArrayList<Post>();

        JsonElement id;
        JsonElement creationDate;
        JsonElement modificationDate;
        JsonElement title;
        JsonElement featuredMedia;
        JsonElement featuredMediaSmall;
        JsonElement images;
        JsonElement links;
        JsonElement contentRenedered;



        for (JsonElement post : arrayOfPosts) {
            id = post.getAsJsonObject().get(WPService.ID);

            creationDate = post.getAsJsonObject().get(WPService.DATE);
            modificationDate = post.getAsJsonObject().get(WPService.MODIFIED);
            title = post.getAsJsonObject().get(WPService.TITLE).getAsJsonObject().get(WPService.RENDERED);
            links = post.getAsJsonObject().get(WPService.LINKS);
            images = links.getAsJsonObject().get(WPService.WP_ATTACHMENT).getAsJsonArray().get(0).getAsJsonObject()
                    .get(WPService.HREF);
            featuredMedia = post.getAsJsonObject().get(WPService.EMBEDDED).getAsJsonObject()
                    .get(WPService.WP_FEATURED_MEDIA)
                    .getAsJsonArray()
                    .get(0).getAsJsonObject().get(WPService.MEDIA_DETAILS).getAsJsonObject()
                    .get(WPService.SIZES).getAsJsonObject().get(WPService.POST_BIG_SIZE);
            /**
             * post-big is one of the new sizes supported by WP. Some pictures might not included so
             * ,if that's the case, we fallback to large
             * Large will always be included for featuredImages
             * However, in {@link MediaFromPostDeserializer } we must ensure we fallback to 'full' as some
             * post images might not include large
             *
             * https://make.wordpress.org/core/2015/11/10/responsive-images-in-wordpress-4-4/
             */
            if (featuredMedia != null) {
                featuredMedia = featuredMedia.getAsJsonObject().get(WPService.SOURCE_URL);
            } else {
                featuredMedia = post.getAsJsonObject().get(WPService.EMBEDDED).getAsJsonObject()
                        .get(WPService.WP_FEATURED_MEDIA)
                        .getAsJsonArray()
                        .get(0).getAsJsonObject().get(WPService.MEDIA_DETAILS).getAsJsonObject()
                        .get(WPService.SIZES).getAsJsonObject().get(WPService.LARGE_SIZE).getAsJsonObject()
                        .get(WPService.SOURCE_URL);
            }


            featuredMediaSmall = post.getAsJsonObject().get(WPService.EMBEDDED).getAsJsonObject()
                    .get(WPService.WP_FEATURED_MEDIA)
                    .getAsJsonArray()
                    .get(0).getAsJsonObject().get(WPService.MEDIA_DETAILS).getAsJsonObject()
                    .get(WPService.SIZES).getAsJsonObject().get(WPService.SMALL_SIZE).getAsJsonObject()
                    .get(WPService.SOURCE_URL);

            contentRenedered = post.getAsJsonObject().get(WPService.CONTENT).getAsJsonObject().get(WPService.RENDERED);

            postsList.add(new Post(id.toString(), creationDate.getAsString(), modificationDate.getAsString(),
                                   title.getAsString(),
                                   featuredMedia.getAsString(),featuredMediaSmall.getAsString(),
                                   images.getAsString(), contentRenedered.getAsString()));
        }
        ListsPostsResponse r = new ListsPostsResponse(postsList);
        return r;
    }
}