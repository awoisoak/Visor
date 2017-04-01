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
     * Run the {@code Runnable} on the UI main thread, to be run after the specified amount
     * of time elapses.
     *
     * @param runnable    the runnable
     * @param delayMillis the delay (in milliseconds) until the Runnable will be executed
     */
    public static void runOnUiThreadDelayed(Runnable runnable, long delayMillis) {
        if (sUiThreadHandler == null) {
            sUiThreadHandler = new Handler(Looper.getMainLooper());
        }
        sUiThreadHandler.postDelayed(runnable, delayMillis);
    }

    /**
     * Cancels a runnable that should be run in the UI main thread after a delay (scheduled
     * by {@link #runOnUiThreadDelayed(Runnable, long)})
     *
     * @param runnable the runnable to be canceled
     */
    public static void cancelRunOnUiThreadDelayed(Runnable runnable) {
        if (sUiThreadHandler != null) {
            sUiThreadHandler.removeCallbacks(runnable);
        }
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
