package com.awoisoak.visor.presentation.postlist;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.awoisoak.visor.R;
import com.awoisoak.visor.data.source.Post;
import com.awoisoak.visor.data.source.local.BlogManager;
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
    private static String POSTS_TABLE_CREATED = "posts_table_created";
    private static String FIRST_REQUEST_POSTS_LIST = "first_request_posts_list";
    private static String TOTAL_RECORDS = "total_records";

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
        if (isPostsTableCreated()) {
            displayPostsFromDb();
            //TODO we should create a checkTotalRecords to see if there is new posts entries
        } else {
            requestNewPosts();
        }
    }


    @Override
    public void onRetryPostRequest() {
        requestNewPosts();
    }

    @Override
    public void onBottomReached() {
        if (mAllPostsDownloaded) {
            return;
        } else if (isPostsTableCreated()) {
            displayPostsFromDb();
        } else if (!mIsPostRequestRunning) {
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

        mView.showLoadingSnackbar();

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
                    saveTotalRecords(response.getTotalRecords());
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


    private void setFirstRequestFlag() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(FIRST_REQUEST_POSTS_LIST, false);
        editor.apply();
    }

    private boolean isFirstRequest() {
        return mSharedPreferences.getBoolean(FIRST_REQUEST_POSTS_LIST, true);
    }

    private void savePostsToDB(List<Post> posts) {
        try {
            BlogManager.getInstance().addposts(posts);
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putBoolean(POSTS_TABLE_CREATED, true);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveTotalRecords(int totalRecords) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(TOTAL_RECORDS, totalRecords);
        editor.apply();
    }

    private int getTotalRecords() {
        return mSharedPreferences.getInt(TOTAL_RECORDS, -1);
    }


    private boolean isPostsTableCreated() {
        return mSharedPreferences.getBoolean(POSTS_TABLE_CREATED, false);
    }

    private List<Post> getPostsFromDB() {
        List<Post> posts = new ArrayList<>();
        try {
            posts = BlogManager.getInstance().getPosts(mOffset);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posts;
    }

    /**
     * Check whether the number of posts retrieved from the DB is the expected.
     * Imagine the db only has 5 posts of the 10 that is supposed to to return...
     *
     * @param numberOfPostsReturned
     * @return
     */
    private boolean checkNumberOfPosts(int numberOfPostsReturned) {

        int expected;
        if (getTotalRecords() - mOffset > mInteractor.MAX_NUMBER_POSTS_RETURNED) {
            expected = mInteractor.MAX_NUMBER_POSTS_RETURNED;
        } else {
            expected = getTotalRecords() - mOffset;
        }

        if (numberOfPostsReturned == expected) {
            increaseOffset();
            if (mOffset >= getTotalRecords()) {
                mAllPostsDownloaded = true;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * If there are posts available in the DB they will be displayed
     */
    private void displayPostsFromDb() {
        List<Post> postsFromDB = getPostsFromDB();
        if (checkNumberOfPosts(postsFromDB.size())) {
            mPosts.addAll(postsFromDB);
            mView.bindPostsList(mPosts);
        } else {
            requestNewPosts();
        }
    }
}
