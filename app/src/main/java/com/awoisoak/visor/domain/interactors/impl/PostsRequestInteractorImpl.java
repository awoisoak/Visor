package com.awoisoak.visor.domain.interactors.impl;

import com.awoisoak.visor.data.source.WPAPI;
import com.awoisoak.visor.data.source.WPListener;
import com.awoisoak.visor.data.source.responses.ErrorResponse;
import com.awoisoak.visor.data.source.responses.ListsPostsResponse;
import com.awoisoak.visor.domain.interactors.PostsRequestInteractor;
import com.awoisoak.visor.signals.SignalManagerFactory;

import javax.inject.Inject;

public class PostsRequestInteractorImpl implements PostsRequestInteractor {

    private WPAPI api;

    @Inject
    public PostsRequestInteractorImpl(WPAPI api) {
        this.api = api;
    }

    @Override
    public void getPosts(int offset) {
        api.getPosts(offset, new WPListener<ListsPostsResponse>() {
            @Override
            public void onResponse(ListsPostsResponse response) {
                SignalManagerFactory.getSignalManager().postEvent(response);
            }

            @Override
            public void onError(ErrorResponse error) {
                SignalManagerFactory.getSignalManager().postEvent(error);
            }
        });
    }

    @Override
    public void getLastPostsFrom(String date) {
        api.getLastPostsFrom(date, new WPListener<ListsPostsResponse>() {
            @Override
            public void onResponse(ListsPostsResponse response) {
                SignalManagerFactory.getSignalManager().postEvent(response);
            }

            @Override
            public void onError(ErrorResponse error) {
                SignalManagerFactory.getSignalManager().postEvent(error);
            }
        });
    }

}
