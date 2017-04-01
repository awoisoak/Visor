package com.awoisoak.visor.domain.interactors.impl;

import com.awoisoak.visor.data.source.Post;
import com.awoisoak.visor.data.source.WPAPI;
import com.awoisoak.visor.data.source.WPListener;
import com.awoisoak.visor.data.source.responses.ErrorResponse;
import com.awoisoak.visor.data.source.responses.ListsPostsResponse;
import com.awoisoak.visor.domain.interactors.PostsRequestInteractor;
import com.awoisoak.visor.signals.SignalManager;
import com.awoisoak.visor.signals.SignalManagerFactory;
import com.awoisoak.visor.threading.ThreadPool;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

public class PostsRequestInteractorImpl implements PostsRequestInteractor {
    /**
     * Default value in WP
     * https://developer.wordpress.org/rest-api/reference/posts/#arguments
     */
    private static int NUMBER_OF_POSTS_RETURNED = 10;
    private int mOffset;
    private WPAPI api;

    @Inject
    public PostsRequestInteractorImpl(WPAPI api) {
        this.api = api;
    }

    @Override
    public void getPosts() {
        System.out.println("awoooooo | Triggering getPosts | offset ="+mOffset);
        api.listPosts(mOffset, new WPListener<ListsPostsResponse>() {
            @Override
            public void onResponse(ListsPostsResponse response) {
                SignalManagerFactory.getSignalManager().postEvent(response);
                increaseOffset();
            }

            @Override
            public void onError(ErrorResponse error) {
                SignalManagerFactory.getSignalManager().postEvent(error);
            }
        });
    }

    public void increaseOffset() {
        mOffset += NUMBER_OF_POSTS_RETURNED;
    }
}
