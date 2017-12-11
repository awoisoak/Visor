package com.awoisoak.visor.presentation.postgallery;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.awoisoak.visor.data.source.Image;
import com.awoisoak.visor.data.source.WPAPI;
import com.awoisoak.visor.data.source.local.BlogManager;
import com.awoisoak.visor.data.source.responses.ErrorResponse;
import com.awoisoak.visor.data.source.responses.MediaFromPostResponse;
import com.awoisoak.visor.domain.interactors.PostGalleryInteractor;
import com.awoisoak.visor.presentation.photo.PhotoActivity;
import com.awoisoak.visor.presentation.webview.WebViewActivity;
import com.awoisoak.visor.signals.SignalManagerFactory;
import com.awoisoak.visor.threading.ThreadPool;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class PostGalleryPresenterImpl implements PostGalleryPresenter {
    public static String MARKER = PostGalleryPresenterImpl.class.getSimpleName();


    private PostGalleryView mView;
    private PostGalleryInteractor mInteractor;

    private boolean mIsGalleryRequestRunning;
    private List<Image> mImages = new ArrayList<>();

    private boolean mAllImagesDownloaded = false;
    private int mOffset;

    String mPostId;

    //SharedPreferences keys
    private static String IMAGES_TABLE_CREATED = "images_table_created_";
    private static String TOTAL_IMAGES = "total_records_";
    boolean isFirstRequest = true;

    SharedPreferences mSharedPreferences;


    @Inject
    public PostGalleryPresenterImpl(PostGalleryView view, PostGalleryInteractor interactor) {
        mView = view;
        mInteractor = interactor;
    }

    @Override
    public void onCreate() {
        SignalManagerFactory.getSignalManager().register(this);
        mSharedPreferences = mView.getActivity().getPreferences(Context.MODE_PRIVATE);
        mPostId = mView.getPostId();
        if (isImagesTableCreated()) {
            displayImagesFromDb();
        } else {
            requestNewImages();
        }
    }


    @Override
    public void onRetryPostRequest() {
        requestNewImages();
    }

    @Override
    public void onBottomReached() {
        if (mAllImagesDownloaded) {
            return;
        } else if (isImagesTableCreated()) {
            displayImagesFromDb();
        } else {
            requestNewImages();
        }
    }

    @Override
    public void showImage(Image image) {
        Activity activity = mView.getActivity();
        Intent i = new Intent(activity, PhotoActivity.class);
        i.putExtra(PhotoActivity.EXTRA_FULL_IMAGE, image.getFull());
        activity.startActivity(i);
    }

    @Override
    public void showPostEntry(String content, String title) {
        Activity activity = mView.getActivity();
        Intent i = new Intent(activity, WebViewActivity.class);
        i.putExtra(WebViewActivity.EXTRA_CONTENT, content);
        i.putExtra(WebViewActivity.EXTRA_POST_TITLE, title);
        activity.startActivity(i);
    }


    /**
     * This will request posts in background. The result will be given Bus event in the methods below
     */
    private void requestNewImages() {
        if (!mIsGalleryRequestRunning) {
            if (!isFirstRequest) {
                mView.showLoadingSnackbar();
            }
            mIsGalleryRequestRunning = true;
            ThreadPool.run(new Runnable() {
                @Override
                public void run() {
                    mInteractor.getImages(mPostId, mOffset);

                }
            });
        }
    }

    /**
     * This method will be called when the interactor returns the new posts
     *
     * @param response
     */
    @Subscribe
    public void onPostsReceivedEvent(final MediaFromPostResponse response) {
        Log.d(MARKER, "@BUS | onPostsReceived | response | code = " + response.getCode());
        /**
         * To avoid the problem where the mPostId is changed just before we save the images to the DB.
         * Otherwise we might be saving images from a post entry into a different
         */
        synchronized (mPostId) {

            if (response.getList().size() == 0) {
                Log.d(MARKER,"onPostReceived | response.size = 0");
                mView.hideSnackbar();
                return;
            }

            /**
             * To avoid the bug when opening/closing quickly several posts.
             * The BUS events where being delivered to different instance of PostGalleryPresenterImpl
             */
            if (!response.getList().get(0).getPostId().equals(mPostId)) {
                Log.d(MARKER, "awooooo | PostGalleryPresenterImpl | onPostReceivedEvent for postid="
                        + response.getList().get(0).getPostId() + " instead of " + mPostId);
                return;
            }
            increaseOffset();
            mView.hideSnackbar();
            mImages.addAll(response.getList());
            ThreadPool.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mView.hideProgressBar();
                    saveImagesToDB(mImages);
                    if (isFirstRequest) {
                        mView.showWelcomeSnackbar();
                        isFirstRequest = false;
                        mView.bindImagesList(mImages);

                    } else {
                        mView.updatePostGallery(response.getList());
                        mView.hideSnackbar();
                    }
                    mIsGalleryRequestRunning = false;
                }
            });
            if (mOffset >= response.getTotalRecords()) {
                mAllImagesDownloaded = true;
                saveTotalRecords(mImages.size());
            }
        }

    }

    public void increaseOffset() {
        mOffset += WPAPI.MAX_NUMBER_IMAGES_RETURNED;
    }


    /**
     * This method will be called when the interactor returns an error trying to get the new posts
     *
     * @param response
     */
    @Subscribe
    public void onErrorRetrievingPostsEvent(ErrorResponse response) {
        Log.d(MARKER, "@BUS | onErrorRetrievingPosts | response | code = " + response.getCode());

        mIsGalleryRequestRunning = false;
        ThreadPool.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mView.hideProgressBar();
                mView.showErrorSnackbar();
            }
        });
    }

    @Override
    public void onDestroy() {
        SignalManagerFactory.getSignalManager().unregister(this);
        mView = null;
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onBackPressed() {
    }


    /**
     * Save a list of images into the DB
     *
     * @param images
     */
    private void saveImagesToDB(List<Image> images) {
        try {
            BlogManager.getInstance().addImages(images);
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putBoolean(IMAGES_TABLE_CREATED + mPostId, true);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Save total number of images available in the post entry
     * In this case total records won't be the value given by the server, but the total number of images
     * after the filters have been applied {@see MediaFromPostDeserializer}
     *
     * @param totalRecords
     */
    private void saveTotalRecords(int totalRecords) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(TOTAL_IMAGES + mPostId, totalRecords);
        editor.apply();
    }

    /**
     * Get total number of images available in the post entry
     *
     * @return
     */
    private int getTotalRecords() {
        return mSharedPreferences.getInt(TOTAL_IMAGES + mPostId, -1);
    }

    /**
     * Check if the table 'Images' has already been created in the DB
     * The table might not include all images records yet
     *
     * @return
     */
    private boolean isImagesTableCreated() {
        return mSharedPreferences.getBoolean(IMAGES_TABLE_CREATED + mPostId, false);
    }

    /**
     * Get images stored in the DB using the current offset
     *
     * @return
     */
    private List<Image> getImagesFromDB() {
        List<Image> image = new ArrayList<>();
        try {
            image = BlogManager.getInstance().getImagesFromPost(mPostId, mOffset);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    /**
     * Check whether the number of images retrieved from the DB is the expected one.
     * (The DB might not include all available images in the post entry yet)
     *
     * @param numberOfPostsReturned
     * @return
     */
    private boolean checkNumberOfImages(int numberOfPostsReturned) {
        int expected;
        if (getTotalRecords() - mOffset > WPAPI.MAX_NUMBER_IMAGES_RETURNED) {
            expected = WPAPI.MAX_NUMBER_IMAGES_RETURNED;
        } else {
            expected = getTotalRecords() - mOffset;
        }

        if (numberOfPostsReturned == expected) {
            increaseOffset();
            if (mOffset >= getTotalRecords()) {
                mAllImagesDownloaded = true;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * If there are images available in the DB they will be displayed
     */
    private void displayImagesFromDb() {
        if (getTotalRecords() ==
                -1) {// Becasuse we r filtering out images, so we can only trust the cache DB once all images were downloaded
            requestNewImages();
        } else {
            List<Image> imagesFromDB = getImagesFromDB();
            if (checkNumberOfImages(imagesFromDB.size())) {
                mImages.addAll(imagesFromDB);
                if (isFirstRequest) {
                    isFirstRequest = false;
                    mView.bindImagesList(mImages);
                } else {
                    mView.updatePostGallery(mImages);
                }

            } else {
                requestNewImages();
            }
        }

    }

}
