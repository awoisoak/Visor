package com.awoisoak.visor.presentation.postlist;

import com.awoisoak.visor.data.source.Post;
import com.awoisoak.visor.presentation.IPresenter;


public interface PostsListPresenter extends IPresenter {

    /**
     * Called when the user choose to retry a request who had failed
     */
    void onRetryPostRequest();

    /**
     * Called when the user reaches the bottom of the RecyclerView
     */
    void onBottomReached();

    /**
     * Called when the user wants to go inside a post gallery
     */
    void showPostGallery(Post post);

}
