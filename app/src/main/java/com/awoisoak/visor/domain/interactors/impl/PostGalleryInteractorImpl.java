package com.awoisoak.visor.domain.interactors.impl;

import com.awoisoak.visor.data.source.WPAPI;
import com.awoisoak.visor.data.source.WPListener;
import com.awoisoak.visor.data.source.responses.ErrorResponse;
import com.awoisoak.visor.data.source.responses.MediaFromPostResponse;
import com.awoisoak.visor.domain.interactors.PostGalleryInteractor;
import com.awoisoak.visor.signals.SignalManagerFactory;

import javax.inject.Inject;

public class PostGalleryInteractorImpl implements PostGalleryInteractor {

    private int mOffset;
    private WPAPI api;

    @Inject
    public PostGalleryInteractorImpl(WPAPI api) {
        this.api = api;
    }

    @Override
    public void getImages(String postId) {
        System.out.println("awoooooo | Triggering getImages | offset =" + mOffset);
        api.retrieveAllMediaFromPost(postId,mOffset, new WPListener<MediaFromPostResponse>() {
            @Override
            public void onResponse(MediaFromPostResponse response) {
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
        mOffset += MAX_NUMBER_IMAGES_RETURNED;
    }
}
