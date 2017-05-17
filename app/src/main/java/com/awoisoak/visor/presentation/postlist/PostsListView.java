package com.awoisoak.visor.presentation.postlist;

import com.awoisoak.visor.data.source.Post;
import com.awoisoak.visor.presentation.IView;

import java.util.List;


public interface PostsListView extends IView {

    /**
     * Hide the initial ProgressBar displayed before any post is retrieved
     */
    void hideProgressBar();

    /**
     * Bind the Posts retrieved. The implementation should create the adapter and set to the RecyclerView
     * @param posts
     */
    void bindPostsList(List<Post> posts);

    /**
     * Update the adapter with the new posts received
     * @param posts
     */
    void updatePostsList(List<Post> posts);

    /**
     * Display Snackbar to inform the user of new posts being retrieved
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
}
