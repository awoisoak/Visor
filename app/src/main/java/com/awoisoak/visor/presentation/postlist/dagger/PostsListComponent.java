package com.awoisoak.visor.presentation.postlist.dagger;

import com.awoisoak.visor.data.source.WPAPIComponent;
import com.awoisoak.visor.presentation.ActivityScope;
import com.awoisoak.visor.presentation.postlist.PostsListActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = WPAPIComponent.class, modules = PostsListModule.class)
public interface PostsListComponent {
    /**
     * It will inject the PostsListPresenter returned in PostsListModule
     * in the variable with the @Inject annotation in PostsListsActivity
     * (This method could have any other name, it will just says that will inject the dependencies given in PostsListModule
     * into the variables with the @Inject annotation in PostsListActivity)
     */
    void inject(PostsListActivity o);
}
