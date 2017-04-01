package com.awoisoak.visor.signals;

import android.util.Log;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;


public class OttoWrapper implements SignalManager {
    private Bus mBus;

    public  OttoWrapper(){
        mBus = new Bus(ThreadEnforcer.ANY);
    }

    @Override
    public void register(Object object) {
        mBus.register(object);
        System.out.println("awooooo | OttoWrapper | bus registered +"+object.getClass().getSimpleName());
    }

    @Override
    public void unregister(Object object) {
        System.out.println("awooooo | OttoWrapper | bus unregister +"+object.getClass().getSimpleName());
        mBus.unregister(object);
    }

    @Override
    public void postEvent(Object event) {
        Log.d("awooo @BUS", "Post event = " + event.getClass().getName());
        mBus.post(event);
    }
}
