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
     * medium_large (768px) is not always available:
     * https://make.wordpress.org/core/2015/11/10/responsive-images-in-wordpress-4-4/
     * So we better go for small-size by now (400px)
     *
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
        JsonElement images;
        JsonElement links;

        for (JsonElement post : arrayOfPosts) {
            id = post.getAsJsonObject().get("id");
            creationDate = post.getAsJsonObject().get("date");
            modificationDate = post.getAsJsonObject().get("modified");
            title = post.getAsJsonObject().get("title").getAsJsonObject().get("rendered");
            links = post.getAsJsonObject().get("_links");
            images = links.getAsJsonObject().get("wp:attachment").getAsJsonArray().get(0).getAsJsonObject().get("href");
            featuredMedia =
                    post.getAsJsonObject().get("_embedded").getAsJsonObject().get("wp:featuredmedia").getAsJsonArray()
                            .get(0).getAsJsonObject().get("media_details").getAsJsonObject()
                            .get("sizes").getAsJsonObject().get("small-size").getAsJsonObject().get("source_url");

            postsList
                    .add(new Post(id.toString(), creationDate.getAsString(), modificationDate.getAsString(), title.getAsString(),
                                  featuredMedia.getAsString(),
                                  images.getAsString()));
        }
        ListsPostsResponse r = new ListsPostsResponse(postsList);
        return r;
    }
}