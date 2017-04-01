package com.awoisoak.visor.signals;

public class SignalManagerFactory {
    private static SignalManager instance;

    public static SignalManager getSignalManager() {
        if (instance == null) {
            instance = new OttoWrapper();
        }
        return instance;
    }

    static void setInstance(SignalManager instance) {
        SignalManagerFactory.instance = instance;
    }
}
