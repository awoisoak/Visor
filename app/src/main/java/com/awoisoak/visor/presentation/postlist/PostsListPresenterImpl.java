package com.awoisoak.visor.presentation.postlist;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

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
    private static String TOTAL_POSTS = "total_records";

    boolean isFirstRequest = true;
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
            //TODO we should create a checkTotalRecords to detect when there are new posts entries
            //TODO We might call it too(not only) from a future Android Notification
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
        } else {
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
        if (!mIsPostRequestRunning) {
            if (!isFirstRequest) {
                mView.showLoadingSnackbar();
            }
            mIsPostRequestRunning = true;
            ThreadPool.run(new Runnable() {
                @Override
                public void run() {
                    mInteractor.getPosts(mOffset);
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
                if (isFirstRequest) {
                    saveTotalRecords(response.getTotalRecords());
                    isFirstRequest = false;
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

    /**
     * Save a list of posts into the DB
     * @param posts
     */
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

    /**
     * Save total number of posts available in the blog
     * @param totalRecords
     */
    private void saveTotalRecords(int totalRecords) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(TOTAL_POSTS, totalRecords);
        editor.apply();
    }

    /**
     * Get total number of posts available in the blog
     * @return
     */
    private int getTotalRecords() {
        return mSharedPreferences.getInt(TOTAL_POSTS, -1);
    }

    /**
     * Check if the table 'Posts' has already been created in the DB
     * The table might not include all posts records yet
     *
     * @return
     */
    private boolean isPostsTableCreated() {
        return mSharedPreferences.getBoolean(POSTS_TABLE_CREATED, false);
    }


    /**
     * Get posts stored in the DB using the current offset
     * @return
     */
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
     * Check whether the number of posts retrieved from the DB is the expected one.
     * (The DB might not include all available posts in the Blog yet)
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
            if (isFirstRequest) {
                isFirstRequest = false;
                mView.bindPostsList(mPosts);
            } else {
                mView.updatePostsList(mPosts);
            }
        } else {
            requestNewPosts();
        }
    }
}
