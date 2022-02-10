//TODO FirebaseJobDispatcher deprecated

//package com.awoisoak.visor.service;
//
//import android.content.Context;
//
//import com.firebase.jobdispatcher.Constraint;
//import com.firebase.jobdispatcher.Driver;
//import com.firebase.jobdispatcher.FirebaseJobDispatcher;
//import com.firebase.jobdispatcher.GooglePlayDriver;
//import com.firebase.jobdispatcher.Job;
//import com.firebase.jobdispatcher.Lifetime;
//import com.firebase.jobdispatcher.Trigger;
//
//import java.util.concurrent.TimeUnit;
//
///**
// * It creates an scheduled job to check if new posts are available in the blog {@see MyJobService}
// * It will be executed every 24h only when the Wifi is enabled.
// */
//
//
//public class Scheduler {
//
//    private static final String CHECK_NEW_ENTRIES_JOB_TAG = "check_new_entries_tag";
//
//    private static final int JOB_WINDOW_START_MINUTES = 24 * 60;
//    private static final int JOB_WINDOW_START_SECONDS = (int) (TimeUnit.MINUTES.toSeconds(JOB_WINDOW_START_MINUTES));
//    private static final int JOB_WINDOW_END_SECONDS = JOB_WINDOW_START_SECONDS;
//
//    private static boolean isInitialized;
//
//    public static void scheduleCheckingNewEntries(Context context) {
//
//        if (isInitialized) {
//            return;
//        }
//
//        Driver driver = new GooglePlayDriver(context);
//        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
//        Job checkNewEntries = dispatcher.newJobBuilder()
//                .setService(MyJobService.class)
//                .setTag(CHECK_NEW_ENTRIES_JOB_TAG)
//                .setConstraints(Constraint.ON_UNMETERED_NETWORK)
//                .setLifetime(Lifetime.FOREVER)
//                .setRecurring(true)
//                .setTrigger(Trigger.executionWindow(JOB_WINDOW_START_SECONDS,
//                                                    JOB_WINDOW_START_SECONDS + JOB_WINDOW_END_SECONDS))
//                .setReplaceCurrent(true)
//                .build();
//        dispatcher.schedule(checkNewEntries);
//
//        isInitialized = true;
//
//    }
//}
