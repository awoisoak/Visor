package com.awoisoak.visor.presentation.postgallery;

import com.awoisoak.visor.data.source.Image;
import com.awoisoak.visor.data.source.Post;
import com.awoisoak.visor.presentation.IPresenter;


public interface PostGalleryPresenter extends IPresenter {

    /**
     * Called when the user choose to retry a request who had failed
     */
    void onRetryPostRequest();

    /**
     * Called when the user reaches the bottom of the RecyclerView
     */
    void onBottomReached();

    /**
     * Called when the user wants to get inside an image
     */
    void showImage(Image image);
}
