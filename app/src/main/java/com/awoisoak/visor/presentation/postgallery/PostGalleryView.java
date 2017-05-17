package com.awoisoak.visor.presentation.postgallery;

import com.awoisoak.visor.data.source.Image;
import com.awoisoak.visor.presentation.IView;

import java.util.List;


public interface PostGalleryView extends IView {

    /**
     * Hide the initial ProgressBar displayed before any post is retrieved
     */
    void hideProgressBar();

    /**
     * Bind the Images retrieved. The implementation should create the adapter and set to the RecyclerView
     * @param images
     */
    void bindImagesList(List<Image> images);

    /**
     * Update the adapter with the new images received
     * @param images
     */
    void updatePostGallery(List<Image> images);

    /**
     * Display Snackbar to inform the user of new images being retrieved
     */
    void showLoadingSnackbar();


    /**
     * Display Error Snackbar to inform the user there was an error and ask if he/she want to retry.
     */
    void showErrorSnackbar();


    /**
     * Hide any of the previous Snackbar
     */
    void hideSnackbar();

    /**
     * returns the postId of this gallery
     */
    String getPostId();

    /**
     * Display welcome snackbar
     */
    void showWelcomeSnackbar();
}
