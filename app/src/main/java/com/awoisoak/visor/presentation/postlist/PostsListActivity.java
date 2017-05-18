package com.awoisoak.visor.presentation.postlist;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.awoisoak.visor.R;
import com.awoisoak.visor.data.source.Post;
import com.awoisoak.visor.presentation.VisorApplication;
import com.awoisoak.visor.presentation.postlist.dagger.DaggerPostsListComponent;
import com.awoisoak.visor.presentation.postlist.dagger.PostsListModule;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostsListActivity extends AppCompatActivity
        implements PostsListView, PostsListAdapter.PostItemClickListener {
    private static final String MARKER = PostsListActivity.class.getSimpleName();

    @BindView(R.id.posts_list_toolbar) Toolbar mToolbar;
    @BindView(R.id.posts_list_logo) ImageView mLogo;
    @BindView(R.id.posts_list_recycler) RecyclerView mRecyclerView;
    @BindView(R.id.post_list_progress_bar) ProgressBar mProgressBar;
    @BindView(R.id.post_list_text_view) TextView mLoadingText;


    Snackbar mSnackbar;
    @Inject
    PostsListPresenter mPresenter;

    PostsListAdapter mAdapter;
    LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts_list);
        ButterKnife.bind(this);
        mToolbar.setTitle("");
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();
                if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                    if (!mRecyclerView.canScrollVertically(1)) {
                        mPresenter.onBottomReached();
                    }
                }
            }
        });

        DaggerPostsListComponent.builder()
                .wPAPIComponent(((VisorApplication) getApplication()).getWPAPIComponent())
                .postsListModule(new PostsListModule(this))
                .build().inject(this);
        mPresenter.onCreate();
    }


    @Override
    public void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
        mLoadingText.setVisibility(View.GONE);
    }

    @Override
    public void bindPostsList(List<Post> posts) {
        System.out.println("awoooooo | PostGalleryActivity | bindPostList");
        mAdapter = new PostsListAdapter(posts, this, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void updatePostsList(List<Post> posts) {
        System.out.println("awoooooo | PostGalleryActivity | updatePostList");

        if (mAdapter != null) {
            /**
             * We execute like this because of the next bug
             * http://stackoverflow.com/questions/39445330/cannot-call-notifyiteminserted-method-in-a-scroll-callback-recyclerview-v724-2
             */
            mRecyclerView.post(new Runnable() {
                public void run() {
                    /**
                     * We don't use notifyItemRangeInserted because we keep replicating this known Android bug
                     * https://issuetracker.google.com/issues/37007605
                     */
                    //mAdapter.notifyItemRangeInserted(mAdapter.getItemCount() - posts.size(), posts.size());
                    mAdapter.notifyDataSetChanged();
                }
            });
        } else {
            Log.d(MARKER, "awoooooo | PostGalleryActivity | updatePostGallery | mAdapter is null!");
        }
    }

    @Override
    public void showLoadingSnackbar() {
        mSnackbar = Snackbar.make(mRecyclerView, getResources().getString(R.string.loading_posts),
                                  Snackbar.LENGTH_INDEFINITE);
        mSnackbar.getView().setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.black));

        mSnackbar.show();
    }

    @Override
    public void hideSnackbar() {
        if (mSnackbar != null) {
            mSnackbar.dismiss();
        }
    }

    @Override
    public void showErrorSnackbar() {
        mSnackbar =
                Snackbar.make(mRecyclerView, getResources().getString(R.string.error_network_connection),
                              Snackbar.LENGTH_INDEFINITE).setAction(
                        "Retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mSnackbar.dismiss();
                                showLoadingSnackbar();
                                mPresenter.onRetryPostRequest();
                            }
                        });
        mSnackbar.show();
    }

    @Override
    public void onPostItemClick(Post post) {
        mPresenter.showPostGallery(post);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }
}
