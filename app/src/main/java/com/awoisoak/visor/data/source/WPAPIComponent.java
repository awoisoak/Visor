package com.awoisoak.visor.data.source;

import com.awoisoak.visor.presentation.ApplicationModule;

import dagger.Component;

@WPAPIScope
@Component(modules = {WPAPIModule.class, ApplicationModule.class})
public interface WPAPIComponent {
    WPAPI getWPAPI();
}
