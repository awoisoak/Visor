package com.awoisoak.visor.presentation;

import android.app.Application;
import android.content.Context;

import com.awoisoak.visor.data.source.DaggerWPAPIComponent;
import com.awoisoak.visor.data.source.WPAPI;
import com.awoisoak.visor.data.source.WPAPIComponent;

import dagger.Module;

@Module
public class VisorApplication extends Application {
    private WPAPIComponent mWPAPIComponent;
    private static VisorApplication sVisorApplication;


    @Override
    public void onCreate() {
        super.onCreate();
        sVisorApplication = this;
        mWPAPIComponent =
                DaggerWPAPIComponent.builder()
                        .applicationModule(new ApplicationModule(getApplicationContext())).build();
    }

    public WPAPIComponent getWPAPIComponent() {
        return mWPAPIComponent;
    }


    public static VisorApplication getVisorApplication() {
        if (sVisorApplication == null) {
            throw new NullPointerException("Application is null");
        } else {
            return sVisorApplication;
        }
    }
}
