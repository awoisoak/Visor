package com.awoisoak.visor.presentation.postgallery;


import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.awoisoak.visor.data.source.Image;
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
    private boolean mIsFirstRequest = true;
    private List<Image> mImages = new ArrayList<>();

    private boolean mAllImagesDownloaded = false;
    private int mOffset;

    @Inject
    public PostGalleryPresenterImpl(PostGalleryView view, PostGalleryInteractor interactor) {
        mView = view;
        mInteractor = interactor;
    }

    @Override
    public void onCreate() {
        SignalManagerFactory.getSignalManager().register(this);
        requestNewImages();
    }


    @Override
    public void onRetryPostRequest() {
        requestNewImages();
    }

    @Override
    public void onBottomReached() {
        if (mAllImagesDownloaded) {
            return;
        }
        if (!mIsGalleryRequestRunning) {
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
        if (!mIsFirstRequest) {
            mView.showLoadingSnackbar();
        }
        mIsGalleryRequestRunning = true;
        ThreadPool.run(new Runnable() {
            @Override
            public void run() {
                mInteractor.getImages(mView.getPostId(), mOffset);

            }
        });
    }

    /**
     * This method will be called when the interactor returns the new posts
     *
     * @param response
     */
    @Subscribe
    public void onPostsReceivedEvent(final MediaFromPostResponse response) {
        Log.d(MARKER, "@BUS | onPostsReceived | response | code = " + response.getCode());
        increaseOffset();
        mView.hideSnackbar();
        mImages.addAll(response.getList());
        ThreadPool.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mView.hideProgressBar();
                if (mIsFirstRequest) {
                    mView.showWelcomeSnackbar();
                    mIsFirstRequest = false;
                    mView.bindPostGallery(mImages);

                } else {
                    mView.updatePostGallery(response.getList());
                    mView.hideSnackbar();
                }
                mIsGalleryRequestRunning = false;
            }
        });
        if (mOffset >= response.getTotalRecords()) {
            mAllImagesDownloaded = true;
        }

    }

    public void increaseOffset() {
        mOffset += mInteractor.MAX_NUMBER_IMAGES_RETURNED;
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


}
