//TODO FirebaseJobDispatcher deprecated

//package com.awoisoak.visor.service;
//
//import android.app.Notification;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.content.res.Resources;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.support.v7.app.NotificationCompat;
//import android.util.Log;
//
//import com.awoisoak.visor.R;
//import com.awoisoak.visor.data.source.Post;
//import com.awoisoak.visor.data.source.WPAPI;
//import com.awoisoak.visor.data.source.WPListener;
//import com.awoisoak.visor.data.source.WPManager;
//import com.awoisoak.visor.data.source.local.BlogManager;
//import com.awoisoak.visor.data.source.responses.ErrorResponse;
//import com.awoisoak.visor.data.source.responses.ListsPostsResponse;
//import com.awoisoak.visor.presentation.VisorApplication;
//import com.awoisoak.visor.presentation.postlist.PostsListActivity;
//import com.awoisoak.visor.presentation.postlist.PostsListPresenterImpl;
//import com.awoisoak.visor.threading.ThreadPool;
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.target.Target;
//import com.firebase.jobdispatcher.JobParameters;
//import com.firebase.jobdispatcher.JobService;
//
//import java.util.concurrent.ExecutionException;
//
///**
// *
// * JobService that checks if there is new Entries available in the blog and , in that case,
// * create and display an Android notification.
// *
// * TODO
// * Improvements for the future:
// *      - use dagger
// *      - follow clean architecture
// *      - Directly save the post into the database and modify the SharedPreferences flags so we don't duplicate
// *          the request once the app is open.
// */
//
//
//public class MyJobService extends JobService {
//    public static String MARKER = PostsListPresenterImpl.class.getSimpleName();
//
//    private static final int AWOISOAK_BLOG_PENDING_INTENT_ID = 666;
//
//
//
//    private WPAPI api = WPManager.getInstance();
//    Context c = VisorApplication.getVisorApplication().getApplicationContext();
//
//
//    @Override
//    public boolean onStartJob(final JobParameters job) {
//        ThreadPool.run(new Runnable() {
//            @Override
//            public void run() {
//                Log.d(MARKER, "MyJobService is running...");
//                try {
//                    final String lastPostDate = BlogManager.getInstance().getLastPost().getCreationDate();
//                    api.getLastPostsFrom(lastPostDate, new WPListener<ListsPostsResponse>() {
//                        @Override
//                        public void onResponse(final ListsPostsResponse response) {
//                            if (response.getList().size() > 0) {
//                                /**
//                                 * We assume there will only be one new notification as it will be like this 99.9%
//                                 * and it doesn't make sense to display several notifications to open the app
//                                 * All new posts should be managed correctly by  {@link PostsListPresenterImpl}
//                                 */
//                                Log.d(MARKER, "MyJobService | new entry ! | Creating notification..");
//                                Post newPost = response.getList().get(0);
//                                Bitmap featuredImage = retrieveFeaturedImage(c, newPost);
//                                displayAndroidNotification(newPost, featuredImage);
//
//                            } else {
//                                Log.d(MARKER, "MyJobService | no new entries in the blog");
//                            }
//
//                            jobFinished(job, false);
//                        }
//
//                        @Override
//                        public void onError(ErrorResponse error) {
//                            Log.d(MARKER, "MyJobService | Response error | Job rescheduled");
//                            jobFinished(job, true);
//                        }
//                    });
//
//                } catch (
//                        Exception e)
//
//                {
//                    Log.d(MARKER, "MyJobService | Error while checking new entries | Job rescheduled");
//                    jobFinished(job, true);
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        return true;
//    }
//
//    @Override
//    public boolean onStopJob(JobParameters job) {
//        return true;
//    }
//
//    private void displayAndroidNotification(final Post p, final Bitmap featuredImage) {
//        ThreadPool.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                NotificationCompat.Builder builder = new NotificationCompat.Builder(c);
//                builder.setColor(c.getResources().getColor(R.color.colorPrimary))
//                        .setLargeIcon(largeIcon(c))
//                        .setSmallIcon(R.drawable.hal_9000)
//                        .setContentTitle(c.getResources().getString(R.string.android_notification_title))
//                        .setContentText(c.getResources().getString(R.string.android_notification_text, p.getTitle()))
//                        .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(featuredImage))
//                        .setDefaults(Notification.DEFAULT_VIBRATE)
//                        .setContentIntent(createPendingIntent(c))
//                        .setAutoCancel(true)
//                        .setPriority(Notification.PRIORITY_HIGH);
//
//
//                NotificationManager notificationManager = (NotificationManager)
//                        c.getSystemService(Context.NOTIFICATION_SERVICE);
//                notificationManager.notify(Integer.parseInt(p.getId()), builder.build());
//            }
//        });
//    }
//
//
//    private Bitmap largeIcon(Context context) {
//        Resources res = context.getResources();
//        Bitmap largeIcon = BitmapFactory.decodeResource(res, R.drawable.hal_9000);
//        return largeIcon;
//    }
//
//    /**
//     * Retrieve the featured image of the new post synchronously and set it in the Notification
//     *
//     * @param context
//     * @param p
//     * @return
//     */
//    private Bitmap retrieveFeaturedImage(Context context, Post p) {
//        try {
//            return Glide.with(this)
//                    .load(p.getFeaturedImage())
//                    .asBitmap()
//                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
//                    .get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//
//        /**
//         * In case there is a problem with the featured image
//         */
//        Resources res = context.getResources();
//        Bitmap largeIcon = BitmapFactory.decodeResource(res, R.drawable.logo);
//        return largeIcon;
//    }
//
//    /**
//     * Create a PendingIntent so the NotificationManager "app" can open our app
//     *
//     * @param context
//     * @return
//     */
//    private PendingIntent createPendingIntent(Context context) {
//        Intent startActivityIntent = new Intent(context, PostsListActivity.class);
//        return PendingIntent.getActivity(
//                context,
//                AWOISOAK_BLOG_PENDING_INTENT_ID,
//                startActivityIntent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//    }
//}
