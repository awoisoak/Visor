package com.awoisoak.visor.data.source;

import com.awoisoak.visor.data.source.responses.MediaFromPostResponse;
import com.awoisoak.visor.data.source.responses.WPResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Specific Deserializer for MediaFromPostResponse
 * <p>
 * https://developer.wordpress.org/rest-api/reference/media/#list-media
 */


class MediaFromPostDeserializer<T extends WPResponse>
        implements JsonDeserializer<MediaFromPostResponse> {


    public MediaFromPostDeserializer() {
    }

    @Override
    public MediaFromPostResponse deserialize(JsonElement je, Type type, JsonDeserializationContext jdc)
            throws JsonParseException {

        JsonArray arrayImages = je.getAsJsonArray();
        ArrayList<Image> imagesList = new ArrayList<Image>();
        JsonElement thumbnail;
        JsonElement square;
        JsonElement medium;
        JsonElement medium_large;
        JsonElement large;
        JsonElement full;
        JsonElement sizes;

        for (JsonElement post : arrayImages) {
            sizes = post.getAsJsonObject().get("media_details").getAsJsonObject().get("sizes");
            thumbnail = sizes.getAsJsonObject().get("thumbnail").getAsJsonObject().get("source_url");
            square = sizes.getAsJsonObject().get("square").getAsJsonObject().get("source_url");
            medium = sizes.getAsJsonObject().get("medium").getAsJsonObject().get("source_url");
            medium_large = sizes.getAsJsonObject().get("medium_large").getAsJsonObject().get("source_url");
            large = sizes.getAsJsonObject().get("large").getAsJsonObject().get("source_url");
            full = sizes.getAsJsonObject().get("full").getAsJsonObject().get("source_url");
            imagesList
                    .add(new Image(thumbnail.toString(), square.toString(), medium.toString(), medium_large.toString(),
                                   large.toString(), full.toString()));
        }
        MediaFromPostResponse r = new MediaFromPostResponse(imagesList);
        return r;
    }
}