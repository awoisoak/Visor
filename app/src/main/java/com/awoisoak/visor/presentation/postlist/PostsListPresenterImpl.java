package com.awoisoak.visor.presentation.postlist;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.awoisoak.visor.R;
import com.awoisoak.visor.data.source.Post;
import com.awoisoak.visor.data.source.local.PostManager;
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
    private List<Post> mPosts = new ArrayList<>();

    private boolean mAllPostsDownloaded = false;
    private int mOffset;

    //SharedPreferences keys
    private static String DATABASE_CREATED = "database_created";
    private static String FIRST_REQUEST = "first_server_request";
    SharedPreferences mSharedPreferences;




    @Inject
    public PostsListPresenterImpl(PostsListView view, PostsRequestInteractor interactor) {
        mView = view;
        mInteractor = interactor;
    }

    @Override
    public void onCreate() {
        SignalManagerFactory.getSignalManager().register(this);
        mSharedPreferences = mView.getActivity().getPreferences(Context.MODE_PRIVATE);
        if (isDatabaseCreated()) {
            displayPostsFromDb();
        }
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
        i.putExtra(PostGalleryActivity.EXTRA_CONTENT, post.getContent());
        activity.startActivity(i);
    }


    /**
     * This will request posts in background. The result will be given Bus event in the methods below
     */
    private void requestNewPosts() {
        String message;
        if (!isFirstRequest()) {
            message = mView.getActivity().getString(R.string.loading_new_posts);
        } else {
            message = mView.getActivity().getString(R.string.loading_posts_first_time);
        }
        mView.showLoadingSnackbar(message);

        mIsPostRequestRunning = true;
        ThreadPool.run(new Runnable() {
            @Override
            public void run() {
                mInteractor.getPosts(mOffset);
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
        increaseOffset();
        mView.hideSnackbar();
        mPosts.addAll(response.getList());
        ThreadPool.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mView.hideProgressBar();
                savePostsToDB(mPosts);
                if (isFirstRequest()) {
                    setFirstRequestFlag();
                    mView.bindPostsList(mPosts);

                } else {
                    mView.updatePostsList(response.getList());
                    mView.hideSnackbar();
                }
                mIsPostRequestRunning = false;
            }
        });
        if (mOffset >= response.getTotalRecords()) {
            mAllPostsDownloaded = true;
        }
    }

    public void increaseOffset() {
        mOffset += mInteractor.MAX_NUMBER_POSTS_RETURNED;
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
                String message;
                if (isFirstRequest()) {
                    message = mView.getActivity().getString(R.string.error_downloading_posts);
                } else {
                    message = mView.getActivity().getString(R.string.error_retrieving_new_posts);
                }
                mView.showErrorSnackbar(message);
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


    private void setFirstRequestFlag() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(FIRST_REQUEST, false);
        editor.apply();
    }

    private boolean isFirstRequest() {
        return mSharedPreferences.getBoolean(FIRST_REQUEST, true);
    }

    private void savePostsToDB(List<Post> posts) {
        try {
            PostManager.getInstance().addposts(posts);
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putBoolean(DATABASE_CREATED, true);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isDatabaseCreated() {
        return mSharedPreferences.getBoolean(DATABASE_CREATED, false);
    }

    private List<Post> getPostsFromDB() {
        List<Post> posts = new ArrayList<>();
        try {
            posts = PostManager.getInstance().getAllposts();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posts;
    }

    /**
     * If there are posts available in the DB they will be bind to the recyclerview
     */
    private void displayPostsFromDb() {
        mPosts = getPostsFromDB();
        mView.bindPostsList(mPosts);
    }

}
