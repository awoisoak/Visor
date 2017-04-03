package com.awoisoak.visor.presentation.postgallery;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
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
import android.widget.Toast;

import com.awoisoak.visor.R;
import com.awoisoak.visor.data.source.Image;
import com.awoisoak.visor.data.source.Post;
import com.awoisoak.visor.presentation.VisorApplication;
import com.awoisoak.visor.presentation.postgallery.dagger.DaggerPostGalleryComponent;
import com.awoisoak.visor.presentation.postgallery.dagger.PostGalleryModule;

import org.w3c.dom.Text;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostGalleryActivity extends AppCompatActivity
        implements PostGalleryView, PostGalleryAdapter.ImageClickListener {
    private static final String MARKER = PostGalleryActivity.class.getSimpleName();

    public static final String EXTRA_POST_ID = "post_id";
    public static final String EXTRA_POST_TITLE = "post_title";

    @BindView(R.id.post_gallery_toolbar) Toolbar toolbar;
    @BindView(R.id.post_gallery_recycler) RecyclerView mRecyclerView;
    @BindView(R.id.post_gallery_progress_bar) ProgressBar mProgressBar;
    @BindView(R.id.post_gallery_text_view) TextView mLoadingText;

    @BindView(R.id.posts_gallery_title) TextView mTitle;


    Snackbar mSnackbar;
    @Inject
    PostGalleryPresenter mPresenter;

    PostGalleryAdapter mAdapter;
    LinearLayoutManager mLayoutManager; //TODO GridLayout?
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
        if (bundle != null) {
            mPostId = bundle.getString(EXTRA_POST_ID);
            title = bundle.getString(EXTRA_POST_TITLE);
        } else {
            Log.e(MARKER, "Bundle passed to PostGalleryActivity is null");
        }
        mTitle.setText(title);

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
    public void bindPostGallery(List<Image> image) {
        mAdapter = new PostGalleryAdapter(image, this, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void updatePostGallery(List<Image> images) {
        mAdapter.notifyItemRangeInserted(mAdapter.getItemCount() - images.size(), images.size());
        //        mAdapter.addNewPosts(posts);
    }

    @Override
    public void showLoadingSnackbar() {
        mSnackbar = Snackbar.make(mRecyclerView, getString(R.string.loading_new_posts), Snackbar.LENGTH_INDEFINITE);
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
        Snackbar.make(mRecyclerView, R.string.click_to_display_high_res, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorSnackbar() {
        mSnackbar =
                Snackbar.make(mRecyclerView, getString(R.string.error_downloading_pictures), Snackbar.LENGTH_INDEFINITE)
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
