package com.awoisoak.visor.domain.interactors.impl;

import com.awoisoak.visor.data.source.WPAPI;
import com.awoisoak.visor.data.source.WPListener;
import com.awoisoak.visor.data.source.responses.ErrorResponse;
import com.awoisoak.visor.data.source.responses.MediaFromPostResponse;
import com.awoisoak.visor.domain.interactors.PostGalleryInteractor;
import com.awoisoak.visor.signals.SignalManagerFactory;

import javax.inject.Inject;

public class PostGalleryInteractorImpl implements PostGalleryInteractor {

    private WPAPI api;

    @Inject
    public PostGalleryInteractorImpl(WPAPI api) {
        this.api = api;
    }

    @Override
    public void getImages(String postId, int offset) {
        api.retrieveAllMediaFromPost(postId,offset, new WPListener<MediaFromPostResponse>() {
            @Override
            public void onResponse(MediaFromPostResponse response) {
                SignalManagerFactory.getSignalManager().postEvent(response);
            }

            @Override
            public void onError(ErrorResponse error) {
                SignalManagerFactory.getSignalManager().postEvent(error);
            }
        });
    }

}
