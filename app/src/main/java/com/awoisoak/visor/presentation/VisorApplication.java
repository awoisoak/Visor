package com.awoisoak.visor.presentation;

import android.app.Application;

import com.awoisoak.visor.data.source.DaggerWPAPIComponent;
import com.awoisoak.visor.data.source.WPAPI;
import com.awoisoak.visor.data.source.WPAPIComponent;

import dagger.Module;

@Module
public class VisorApplication extends Application {
    private WPAPIComponent mWPAPIComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mWPAPIComponent =
                DaggerWPAPIComponent.builder()
                        .applicationModule(new ApplicationModule(getApplicationContext())).build();
    }

    public WPAPIComponent getWPAPIComponent() {
        return mWPAPIComponent;
    }

}
