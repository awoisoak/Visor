package com.awoisoak.visor.signals;

import android.util.Log;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Wrapper  for the Otto library
 */
public class OttoWrapper implements SignalManager {
    private Bus mBus;

    public  OttoWrapper(){
        mBus = new Bus(ThreadEnforcer.ANY);
    }

    @Override
    public void register(Object object) {
        mBus.register(object);
    }

    @Override
    public void unregister(Object object) {
        mBus.unregister(object);
    }

    @Override
    public void postEvent(Object event) {
        Log.d("@BUS", "Post event = " + event.getClass().getName());
        mBus.post(event);
    }
}
