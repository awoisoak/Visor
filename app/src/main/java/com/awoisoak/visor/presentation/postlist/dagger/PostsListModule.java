package com.awoisoak.visor.presentation.postlist.dagger;

import com.awoisoak.visor.data.source.WPAPI;
import com.awoisoak.visor.domain.interactors.PostsRequestInteractor;
import com.awoisoak.visor.domain.interactors.impl.PostsRequestInteractorImpl;
import com.awoisoak.visor.presentation.ActivityScope;
import com.awoisoak.visor.presentation.postlist.PostsListPresenter;
import com.awoisoak.visor.presentation.postlist.PostsListPresenterImpl;
import com.awoisoak.visor.presentation.postlist.PostsListView;

import dagger.Module;
import dagger.Provides;

@Module
public class PostsListModule {
    private final PostsListView mView;

    public PostsListModule(PostsListView view) {
        mView = view;
    }

    @Provides
    @ActivityScope
    PostsRequestInteractor providePostsRequestInteractor(WPAPI api) {
        return new PostsRequestInteractorImpl(api);
    }

    @Provides
    @ActivityScope
    PostsListPresenter providePostsListPresenter(PostsRequestInteractor interactor) {
        return new PostsListPresenterImpl(mView, interactor);
    }
}
