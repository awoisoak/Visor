package com.awoisoak.visor.signals;

/**
 *
 * We decided to register/unregister to the bus on onStart/onStop
 * The reason is that we are posting the common ErrorResponse object for every request so an activity could be stopped
 * when changing to a second one and both would receive the event.
 *
 * In the future we might go for put them back in the onCreate/onDestroy if we create different event objects
 * for each kind of error response coming from the server
 */

public interface SignalManager {

    public void register(Object object);

    public void unregister(Object object);

    public void postEvent(Object event);
}
