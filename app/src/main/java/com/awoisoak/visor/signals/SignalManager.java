package com.awoisoak.visor.signals;

/**
 * Interface to be used by the corresponding bus event library wrapper
 */

public interface SignalManager {

    public void register(Object object);

    public void unregister(Object object);

    public void postEvent(Object event);
}
