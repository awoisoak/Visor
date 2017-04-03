package com.awoisoak.visor.presentation.postlist;


import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.awoisoak.visor.data.source.Post;
import com.awoisoak.visor.data.source.responses.ErrorResponse;
import com.awoisoak.visor.data.source.responses.ListsPostsResponse;
import com.awoisoak.visor.domain.interactors.PostsRequestInteractor;
import com.awoisoak.visor.presentation.postgallery.PostGalleryActivity;
import com.awoisoak.visor.signals.SignalManagerFactory;
import com.awoisoak.visor.threading.ThreadPool;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class PostsListPresenterImpl implements PostsListPresenter {
    public static String MARKER = PostsListPresenterImpl.class.getSimpleName();


    private PostsListView mView;
    private PostsRequestInteractor mInteractor;

    private boolean mIsPostRequestRunning;
    private boolean mIsFirstRequest = true;
    private List<Post> mPosts = new ArrayList<>();

    private boolean mAllPostsDownloaded = false;

    @Inject
    public PostsListPresenterImpl(PostsListView view, PostsRequestInteractor interactor) {
        mView = view;
        mInteractor = interactor;
    }

    @Override
    public void onCreate() {
        SignalManagerFactory.getSignalManager().register(this);
        requestNewPosts();
    }


    @Override
    public void onRetryPostRequest() {
        requestNewPosts();
    }

    @Override
    public void onBottomReached() {
        if (mAllPostsDownloaded) {
            return;
        }
        if (!mIsPostRequestRunning) {
            requestNewPosts();
        }
    }

    @Override
    public void showPostGallery(Post post) {
        Activity activity = mView.getActivity();
        Intent i = new Intent(activity, PostGalleryActivity.class);
        i.putExtra(PostGalleryActivity.EXTRA_POST_ID, post.getId());
        i.putExtra(PostGalleryActivity.EXTRA_POST_TITLE, post.getTitle());
        activity.startActivity(i);
    }


    /**
     * This will request posts in background. The result will be given Bus event in the methods below
     */
    private void requestNewPosts() {
        if (!mIsFirstRequest) {
            mView.showLoadingSnackbar();
        }
        mIsPostRequestRunning = true;
        ThreadPool.run(new Runnable() {
            @Override
            public void run() {
                mInteractor.getPosts();
            }
        });
    }

    /**
     * This method will be called when the interactor returns the new posts
     *
     * @param response
     */
    @Subscribe
    public void onPostsReceivedEvent(final ListsPostsResponse response) {
        Log.d(MARKER, "@BUS | onPostsReceived | response | code = " + response.getCode());

        mView.hideSnackbar();
        mPosts.addAll(response.getList());
        ThreadPool.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mView.hideProgressBar();
                if (mIsFirstRequest) {
                    mIsFirstRequest = false;
                    mView.bindPostsList(mPosts);

                } else {
                    mView.updatePostsList(response.getList());
                    mView.hideSnackbar();
                }
                mIsPostRequestRunning = false;
            }
        });
        if (response.getList().size() < mInteractor.MAX_NUMBER_POSTS_RETURNED) {
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
        Log.d(MARKER, "@BUS | onErrorRetrievingPosts | response | code = " + response.getCode());

        mIsPostRequestRunning = false;
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
