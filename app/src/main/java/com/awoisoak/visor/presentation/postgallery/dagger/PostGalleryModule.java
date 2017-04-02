package com.awoisoak.visor.presentation.postgallery.dagger;

import com.awoisoak.visor.data.source.WPAPI;
import com.awoisoak.visor.domain.interactors.PostGalleryInteractor;
import com.awoisoak.visor.domain.interactors.impl.PostGalleryInteractorImpl;
import com.awoisoak.visor.presentation.ActivityScope;
import com.awoisoak.visor.presentation.postgallery.PostGalleryPresenter;
import com.awoisoak.visor.presentation.postgallery.PostGalleryPresenterImpl;
import com.awoisoak.visor.presentation.postgallery.PostGalleryView;


import dagger.Module;
import dagger.Provides;

@Module
public class PostGalleryModule {
    private final PostGalleryView mView;

    public PostGalleryModule(PostGalleryView view) {
        mView = view;
    }

    @Provides
    @ActivityScope
    PostGalleryInteractor providePostGalleryInteractor(WPAPI api) {
        return new PostGalleryInteractorImpl(api);
    }

    @Provides
    @ActivityScope
    PostGalleryPresenter providePostGalleryPresenter(PostGalleryInteractor interactor) {
        return new PostGalleryPresenterImpl(mView, interactor);
    }
}
