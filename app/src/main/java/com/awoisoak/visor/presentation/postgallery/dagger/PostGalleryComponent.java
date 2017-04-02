package com.awoisoak.visor.presentation.postgallery.dagger;

import com.awoisoak.visor.data.source.WPAPIComponent;
import com.awoisoak.visor.presentation.ActivityScope;
import com.awoisoak.visor.presentation.postgallery.PostGalleryActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = WPAPIComponent.class, modules = PostGalleryModule.class)
public interface PostGalleryComponent {
    /**
     * It will inject the PostDetailsPresenter returned in PostDetailsModule
     * in the variable with the @Inject annotation in PostDetailssActivity
     * (This method could have any other name, it will just says that will inject the dependencies given in PostDetailsModule
     * into the variables with the @Inject annotation in PostDetailsActivity)
     */
    void inject(PostGalleryActivity o);
}
