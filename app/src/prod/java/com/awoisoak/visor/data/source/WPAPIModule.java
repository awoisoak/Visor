package com.awoisoak.visor.data.source;

import dagger.Module;
import dagger.Provides;

@Module
public class WPAPIModule {

    @WPAPIScope
    @Provides
    WPAPI provideWPAPI() {
        return WPManager.getInstance();
    }
}
