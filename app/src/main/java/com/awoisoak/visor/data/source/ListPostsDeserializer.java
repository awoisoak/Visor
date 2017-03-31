package com.awoisoak.visor.data.source;

import com.awoisoak.visor.data.source.responses.ListPostsResponse;
import com.awoisoak.visor.data.source.responses.WPResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Specific Deserializer for ListPostsResponse
 * <p>
 * http://demo.wp-api.org/wp-json/wp/v2/posts
 */


class ListPostsDeserializer<T extends WPResponse> implements JsonDeserializer<ListPostsResponse> {


    public ListPostsDeserializer() {
    }

    @Override
    public ListPostsResponse deserialize(JsonElement je, Type type, JsonDeserializationContext jdc)
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
            featuredMedia = links.getAsJsonObject().get("wp:featuredmedia").getAsJsonArray().get(0).getAsJsonObject().get("href");
            images = links.getAsJsonObject().get("wp:attachment").getAsJsonArray().get(0).getAsJsonObject().get("href");

            postsList.add(new Post(id.toString(), creationDate.toString(), modificationDate.toString(), title.toString(), featuredMedia.toString(),
                                     images.toString()));
        }
        ListPostsResponse r = new ListPostsResponse(postsList);
        return r;
    }
}