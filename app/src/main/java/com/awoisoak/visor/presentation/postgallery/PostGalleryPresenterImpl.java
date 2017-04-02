package com.awoisoak.visor.presentation.postgallery;


import android.util.Log;
import android.widget.Toast;

import com.awoisoak.visor.data.source.Image;
import com.awoisoak.visor.data.source.responses.ErrorResponse;
import com.awoisoak.visor.data.source.responses.ListsPostsResponse;
import com.awoisoak.visor.data.source.responses.MediaFromPostResponse;
import com.awoisoak.visor.domain.interactors.PostGalleryInteractor;
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

    private boolean mIsPostRequestRunning;
    private boolean mIsFirstRequest = true;
    private List<Image> mImages = new ArrayList<>();

    private boolean mAllPostsDownloaded = false;

    @Inject
    public PostGalleryPresenterImpl(PostGalleryView view, PostGalleryInteractor interactor) {
        mView = view;
        mInteractor = interactor;
    }

    @Override
    //TODO check if we register to Otto here or in start
    //If we trigger here the requestPosts we need to be registered previously
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
        System.out.println("awoooooo | Gallery Presenter | onBottomReached");
        if (mAllPostsDownloaded) {
            return;
        }
        if (!mIsPostRequestRunning) {
            requestNewImages();
        }
    }

    @Override
    public void showImage(Image image) {
        //TODO
    }


    /**
     * This will request posts in background. The result will be given Bus event in the methods below
     */
    private void requestNewImages() {
        System.out.println("awoooooo | Presenter | requestNewImages");
        if (!mIsFirstRequest) {
            mView.showLoadingSnackbar();
        }
        mIsPostRequestRunning = true;
        ThreadPool.run(new Runnable() {
            @Override
            public void run() {
                mInteractor.getImages(mView.getPostId());

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
        Log.d(MARKER, "awooo @BUS | onPostsReceived | response | code = " + response.getCode());

        mImages.addAll(response.getList());
        ThreadPool.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mView.hideProgressBar();
                if (mIsFirstRequest) {
                    mIsFirstRequest = false;
                    mView.bindPostGallery(mImages);

                } else {
                    mView.updatePostGallery(response.getList());
                    mView.hideSnackbar();
                }
                mIsPostRequestRunning = false;
            }
        });
        if (response.getList().size() < mInteractor.MAX_NUMBER_IMAGES_RETURNED) {
            mAllPostsDownloaded = true;
        }
    }

    /**
     * This method will be called when the interactor returns an error trying to get the new posts
     *
     * @param response
     */
    @Subscribe
    public void onErrorRetrievingPostsEvent(ErrorResponse response) {
        Log.d(MARKER, "awoo @BUS | onErrorRetrievingPosts | response | code = " + response.getCode());

        mIsPostRequestRunning = false;
        ThreadPool.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //TODO create this methods in View to hide progress bar & display error snackbar
                mView.hideProgressBar();
                mView.showErrorSnackbar();
                Toast.makeText(mView.getActivity(), "Error receiving the Posts", Toast.LENGTH_LONG).show();
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
        //TODO you ahave to register the buss in OnCreate because there u are requesting the posts
        //if in onStop you unsubscribe (to avoid two activities receiving the same onResponseerrorEvent) then you should
        // register it here again (create the flag if needed)
        //        if (!registered){
        //            SignalManagerFactory.getSignalManager().register(this);
        //        }
    }

    @Override
    public void onStop() {
        System.out.println("awoooooo | Presenter | onStop");
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
