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
        JsonElement smallSize;
        JsonElement postBig;
        JsonElement large;
        JsonElement full;
        JsonElement sizes;

        for (JsonElement post : arrayImages) {
            int width = post.getAsJsonObject().get(WPService.MEDIA_DETAILS).getAsJsonObject().get("width").getAsInt();
            int height = post.getAsJsonObject().get(WPService.MEDIA_DETAILS).getAsJsonObject().get("height").getAsInt();
            /**
             * Filter out the few portrait images available ofr aesthetic purposes
             */
            if (width > height) {
                sizes = post.getAsJsonObject().get(WPService.MEDIA_DETAILS).getAsJsonObject().get(WPService.SIZES);
                thumbnail = sizes.getAsJsonObject().get(WPService.THUMBNAIL_SIZE).getAsJsonObject()
                        .get(WPService.SOURCE_URL);
                square = sizes.getAsJsonObject().get(WPService.SQUARE_SIZE).getAsJsonObject().get(WPService.SOURCE_URL);
                smallSize =
                        sizes.getAsJsonObject().get(WPService.SMALL_SIZE).getAsJsonObject().get(WPService.SOURCE_URL);
                full = sizes.getAsJsonObject().get(WPService.FULL_SIZE).getAsJsonObject().get(WPService.SOURCE_URL);

                /**
                 * large won't be included if it's not available if the image is too small.
                 * In that case we fallback to full size
                 */
                large = sizes.getAsJsonObject().get(WPService.LARGE_SIZE);
                large = large != null ? large.getAsJsonObject().get(WPService.SOURCE_URL) : full;

                /**
                 * post-big is one of the new sizes supported by WP. Some pictures might not included it so
                 * if that's the case, we fallback to large
                 * https://make.wordpress.org/core/2015/11/10/responsive-images-in-wordpress-4-4/
                 */
                postBig = sizes.getAsJsonObject().get(WPService.POST_BIG_SIZE);
                postBig = postBig != null ? postBig.getAsJsonObject().get(WPService.SOURCE_URL) : large;

                imagesList
                        .add(new Image(thumbnail.getAsString(), square.getAsString(), smallSize.getAsString(),
                                       postBig.getAsString(),
                                       large.getAsString(), full.getAsString()));
            }
        }
        MediaFromPostResponse r = new MediaFromPostResponse(imagesList);
        return r;
    }
}