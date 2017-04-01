package com.awoisoak.visor.data.source;

import com.awoisoak.visor.data.MockWPManager;

import dagger.Module;
import dagger.Provides;

@Module
public class WPAPIModule {

    @WPAPIScope
    @Provides
    WPAPI provideWPAPI() {
        return MockWPManager.getInstance();
    }
}
