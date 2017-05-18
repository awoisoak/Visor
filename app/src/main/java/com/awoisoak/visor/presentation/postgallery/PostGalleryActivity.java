package com.awoisoak.visor.presentation.postgallery;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.awoisoak.visor.R;
import com.awoisoak.visor.data.source.Image;
import com.awoisoak.visor.presentation.VisorApplication;
import com.awoisoak.visor.presentation.postgallery.dagger.DaggerPostGalleryComponent;
import com.awoisoak.visor.presentation.postgallery.dagger.PostGalleryModule;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostGalleryActivity extends AppCompatActivity
        implements PostGalleryView, PostGalleryAdapter.ImageClickListener {
    private static final String MARKER = PostGalleryActivity.class.getSimpleName();

    public static final String EXTRA_POST_ID = "post_id";
    public static final String EXTRA_POST_TITLE = "post_title";
    public static final String EXTRA_CONTENT = "content";

    @BindView(R.id.post_gallery_toolbar) Toolbar toolbar;
    @BindView(R.id.post_gallery_recycler) RecyclerView mRecyclerView;
    @BindView(R.id.post_gallery_progress_bar) ProgressBar mProgressBar;
    @BindView(R.id.post_gallery_text_view) TextView mLoadingText;
    @BindView(R.id.posts_gallery_title) TextView mTitle;
    @BindView(R.id.post_gallery_floating_button) FloatingActionButton mFloatingButton;


    Snackbar mSnackbar;
    @Inject
    PostGalleryPresenter mPresenter;

    PostGalleryAdapter mAdapter;
    LinearLayoutManager mLayoutManager;
    private String mPostId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_gallery);
        ButterKnife.bind(this);

        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_white_36dp);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
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


        Bundle bundle = getIntent().getExtras();
        String title = new String();
        String content = new String();
        if (bundle != null) {
            mPostId = bundle.getString(EXTRA_POST_ID);
            title = bundle.getString(EXTRA_POST_TITLE);
            content = bundle.getString(EXTRA_CONTENT);
        } else {
            Log.e(MARKER, "Bundle passed to PostGalleryActivity is null");
        }
        mTitle.setText(title);
        final String finalContent = content;
        final String finalTitle = title;
        mFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.showPostEntry(finalContent, finalTitle);
            }
        });

        DaggerPostGalleryComponent.builder()
                .wPAPIComponent(((VisorApplication) getApplication()).getWPAPIComponent())
                .postGalleryModule(new PostGalleryModule(this))
                .build().inject(this);
        mPresenter.onCreate();
    }


    @Override
    public void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
        mLoadingText.setVisibility(View.GONE);
    }

    @Override
    public void bindImagesList(List<Image> image) {
        Log.d(MARKER,"awoooooo | PostGalleryActivity | bindImagesList");
        mAdapter = new PostGalleryAdapter(image, this, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void updatePostGallery(final List<Image> images) {
        Log.d(MARKER,"awoooooo | PostGalleryActivity | updatePostGallery");
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
                    //mAdapter.notifyItemRangeInserted(mAdapter.getItemCount() - images.size(), images.size());
                    mAdapter.notifyDataSetChanged();
                }
            });
        } else {
            Log.d(MARKER, "awoooooo | PostGalleryActivity | updatePostGallery | mAdapter is null!");
        }
    }

    @Override
    public void showLoadingSnackbar() {
        mSnackbar = Snackbar.make(mRecyclerView, getString(R.string.loading_photos), Snackbar.LENGTH_INDEFINITE);
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
    public String getPostId() {
        return mPostId;
    }

    @Override
    public void showWelcomeSnackbar() {
        Snackbar.make(mRecyclerView, R.string.click_to_display_high_res, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showErrorSnackbar() {
        mSnackbar =
                Snackbar.make(mRecyclerView, getString(R.string.error_network_connection), Snackbar.LENGTH_INDEFINITE)
                        .setAction(
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
    public void onClickImage(Image image) {
        mPresenter.showImage(image);
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
