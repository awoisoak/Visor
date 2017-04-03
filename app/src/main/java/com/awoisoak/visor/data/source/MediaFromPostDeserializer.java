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
import java.util.List;

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
        List<Image> imagesList = new ArrayList<Image>();
        JsonArray arrayImages = je.getAsJsonArray();
        JsonElement thumbnail;
        JsonElement square;
        JsonElement smallSize;
        JsonElement postBig;
        JsonElement large;
        JsonElement full;
        JsonElement sizes;


        if (je.toString().equals("[]")) {
            imagesList = checkBusan();
        }

        for (JsonElement post : arrayImages) {
            int width = post.getAsJsonObject().get(WPService.MEDIA_DETAILS).getAsJsonObject().get("width").getAsInt();
            int height = post.getAsJsonObject().get(WPService.MEDIA_DETAILS).getAsJsonObject().get("height").getAsInt();
            /**
             * Filter out the few portrait images available for aesthetic purposes
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

    /**
     * Busan post is affected by an known WordPress issue
     * https://github.com/WP-API/WP-API/issues/2600
     * This method is an ugly workaround as the solution should be done in the website re-uploading all Busan pics
     */
    private List<Image> checkBusan() {
        List<Image> busanImageList = new ArrayList<Image>();
        Image busan1 =
                new Image("http://awoisoak.com/wp-content/uploads/2015/11/dsc06532_lzn_wm.resized-150x150.jpg",
                          "http://awoisoak.com/wp-content/uploads/2015/11/dsc06532_lzn_wm.resized-400x400.jpg",
                          "http://awoisoak.com/wp-content/uploads/2015/11/dsc06532_lzn_wm.resized-400x225.jpg",
                          "http://awoisoak.com/wp-content/uploads/2015/11/dsc06532_lzn_wm.resized-840x473.jpg",
                          "http://awoisoak.com/wp-content/uploads/2015/11/dsc06532_lzn_wm.resized-1024x577.jpg",
                          "http://awoisoak.com/wp-content/uploads/2015/11/dsc06532_lzn_wm.resized.jpg");

        Image busan2 =
                new Image("http://awoisoak.com/wp-content/uploads/2015/11/dsc06558_lzn_wm.resized-150x150.jpg",
                          "http://awoisoak.com/wp-content/uploads/2015/11/dsc06558_lzn_wm.resized-400x400.jpg",
                          "http://awoisoak.com/wp-content/uploads/2015/11/dsc06558_lzn_wm.resized-400x225.jpg",
                          "http://awoisoak.com/wp-content/uploads/2015/11/dsc06558_lzn_wm.resized-840x473.jpg",
                          "http://awoisoak.com/wp-content/uploads/2015/11/dsc06558_lzn_wm.resized-1024x577.jpg",
                          "http://awoisoak.com/wp-content/uploads/2015/11/dsc06558_lzn_wm.resized.jpg");


        Image busan3 =
                new Image("http://awoisoak.com/wp-content/uploads/2015/11/dsc06652_lzn_wm.resized-150x150.jpg",
                          "http://awoisoak.com/wp-content/uploads/2015/11/dsc06652_lzn_wm.resized-400x400.jpg",
                          "http://awoisoak.com/wp-content/uploads/2015/11/dsc06652_lzn_wm.resized-400x225.jpg",
                          "http://awoisoak.com/wp-content/uploads/2015/11/dsc06652_lzn_wm.resized-840x473.jpg",
                          "http://awoisoak.com/wp-content/uploads/2015/11/dsc06652_lzn_wm.resized-1024x577.jpg",
                          "http://awoisoak.com/wp-content/uploads/2015/11/dsc06652_lzn_wm.resized.jpg");


        Image busan4 =
                new Image("http://awoisoak.com/wp-content/uploads/2015/11/dsc06669_lzn_wm.resized-150x150.jpg",
                          "http://awoisoak.com/wp-content/uploads/2015/11/dsc06669_lzn_wm.resized-400x400.jpg",
                          "http://awoisoak.com/wp-content/uploads/2015/11/dsc06669_lzn_wm.resized-400x225.jpg",
                          "http://awoisoak.com/wp-content/uploads/2015/11/dsc06669_lzn_wm.resized-840x473.jpg",
                          "http://awoisoak.com/wp-content/uploads/2015/11/dsc06669_lzn_wm.resized-1024x577.jpg",
                          "http://awoisoak.com/wp-content/uploads/2015/11/dsc06669_lzn_wm.resized.jpg");


        Image busan5 =
                new Image("http://awoisoak.com/wp-content/uploads/2015/11/dsc06692_lzn_wm.resized-150x150.jpg",
                          "http://awoisoak.com/wp-content/uploads/2015/11/dsc06692_lzn_wm.resized-400x400.jpg",
                          "http://awoisoak.com/wp-content/uploads/2015/11/dsc06692_lzn_wm.resized-400x225.jpg",
                          "http://awoisoak.com/wp-content/uploads/2015/11/dsc06692_lzn_wm.resized-840x473.jpg",
                          "http://awoisoak.com/wp-content/uploads/2015/11/dsc06692_lzn_wm.resized-1024x577.jpg",
                          "http://awoisoak.com/wp-content/uploads/2015/11/dsc06692_lzn_wm.resized.jpg");


        Image busan6 =
                new Image("http://awoisoak.com/wp-content/uploads/2015/11/dsc06704_lzn_wm.resized-150x150.jpg",
                          "http://awoisoak.com/wp-content/uploads/2015/11/dsc06704_lzn_wm.resized-400x400.jpg",
                          "http://awoisoak.com/wp-content/uploads/2015/11/dsc06704_lzn_wm.resized-400x225.jpg",
                          "http://awoisoak.com/wp-content/uploads/2015/11/dsc06704_lzn_wm.resized-840x473.jpg",
                          "http://awoisoak.com/wp-content/uploads/2015/11/dsc06704_lzn_wm.resized-1024x577.jpg",
                          "http://awoisoak.com/wp-content/uploads/2015/11/dsc06704_lzn_wm.resized.jpg");


        Image busan7 =
                new Image("http://awoisoak.com/wp-content/uploads/2015/11/dsc06827_lzn_wm.resized-150x150.jpg",
                          "http://awoisoak.com/wp-content/uploads/2015/11/dsc06827_lzn_wm.resized-400x400.jpg",
                          "http://awoisoak.com/wp-content/uploads/2015/11/dsc06827_lzn_wm.resized-400x234.jpg",
                          "http://awoisoak.com/wp-content/uploads/2015/11/dsc06827_lzn_wm.resized-840x491.jpg",
                          "http://awoisoak.com/wp-content/uploads/2015/11/dsc06827_lzn_wm.resized-1024x599.jpg",
                          "http://awoisoak.com/wp-content/uploads/2015/11/dsc06827_lzn_wm.resized.jpg");


        Image busan8 =
                new Image("http://awoisoak.com/wp-content/uploads/2015/11/dsc06832_lzn_wm.resized-150x150.jpg",
                          "http://awoisoak.com/wp-content/uploads/2015/11/dsc06832_lzn_wm.resized-400x400.jpg",
                          "http://awoisoak.com/wp-content/uploads/2015/11/dsc06832_lzn_wm.resized-400x212.jpg",
                          "http://awoisoak.com/wp-content/uploads/2015/11/dsc06832_lzn_wm.resized-840x445.jpg",
                          "http://awoisoak.com/wp-content/uploads/2015/11/dsc06832_lzn_wm.resized-1024x543.jpg",
                          "http://awoisoak.com/wp-content/uploads/2015/11/dsc06832_lzn_wm.resized.jpg");


        Image busan9 =
                new Image("http://awoisoak.com/wp-content/uploads/2015/11/dsc06834_lzn_wm.resized-150x150.jpg",
                          "http://awoisoak.com/wp-content/uploads/2015/11/dsc06834_lzn_wm.resized-400x400.jpg",
                          "http://awoisoak.com/wp-content/uploads/2015/11/dsc06834_lzn_wm.resized-400x225.jpg",
                          "http://awoisoak.com/wp-content/uploads/2015/11/dsc06834_lzn_wm.resized-840x473.jpg",
                          "http://awoisoak.com/wp-content/uploads/2015/11/dsc06834_lzn_wm.resized-1024x577.jpg",
                          "http://awoisoak.com/wp-content/uploads/2015/11/dsc06834_lzn_wm.resized.jpg");


        busanImageList.add(busan1);
        busanImageList.add(busan2);
        busanImageList.add(busan3);
        busanImageList.add(busan4);
        busanImageList.add(busan5);
        busanImageList.add(busan6);
        busanImageList.add(busan7);
        busanImageList.add(busan8);
        busanImageList.add(busan9);
        return busanImageList;
    }
}


