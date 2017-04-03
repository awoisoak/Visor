package com.awoisoak.visor.threading;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A helper to run {@code Runnable} in different threads (Main thread and UILess ones).
 *
 * <p>
 * The domain layer should not use this class but the classes in the Presentation layer.
 * The rational is that Threadpool is platform dependant (it uses Handlers and Loopers) and the domain layer should be
 * platform independent
 */
public final class ThreadPool {
    private static Handler sUiThreadHandler;
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

    private ThreadPool() {
    }

    /**
     * Run the {@code Runnable} on the UI main thread.
     *
     * @param runnable the runnable
     */
    public static void runOnUiThread(Runnable runnable) {
        if (sUiThreadHandler == null) {
            sUiThreadHandler = new Handler(Looper.getMainLooper());
        }
        sUiThreadHandler.post(runnable);
    }

    /**
     * Run the {@code Runnable} on a non-UI thread.
     *
     * @param runnable the runnable
     */
    public static void run(Runnable runnable) {
        EXECUTOR_SERVICE.execute(runnable);
    }
}
