package com.awoisoak.visor.presentation.postgallery;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.awoisoak.visor.R;
import com.awoisoak.visor.data.source.Image;
import com.awoisoak.visor.data.source.Post;
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

    @BindView(R.id.post_gallery_relative_layout) RelativeLayout mRelativeLayout;
    @BindView(R.id.post_gallery_recycler) RecyclerView mRecyclerView;
    @BindView(R.id.post_gallery_progress_bar) ProgressBar mProgressBar;

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
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);
        mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
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
        if (bundle != null) {
            mPostId = bundle.getString(EXTRA_POST_ID);
        } else {
            Log.e(MARKER, "unknown postID!");
        }


        DaggerPostGalleryComponent.builder()
                .wPAPIComponent(((VisorApplication) getApplication()).getWPAPIComponent())
                .postGalleryModule(new PostGalleryModule(this))
                .build().inject(this);
        mPresenter.onCreate();
    }


    @Override
    public void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
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
        mSnackbar.dismiss();
    }

    @Override
    public String getPostId() {
        return mPostId;
    }

    @Override
    public void showErrorSnackbar() {
        mSnackbar = Snackbar.make(mRecyclerView, getString(R.string.loading_new_posts), Snackbar.LENGTH_INDEFINITE)
                .setAction(
                        "Retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mSnackbar.dismiss();
                                mPresenter.onRetryPostRequest();
                            }
                        });

    }

    @Override
    public void onClickImage(ImageView photo,Image image) {
        Toast.makeText(this, "image was clicked!", Toast.LENGTH_SHORT).show();
//        mPresenter.showImage(image);
//        ImageView mapView = (ImageView) findViewById(R.id.post_gallery_map);
//        mapView.setImageDrawable(photo.getDrawable());
    }


//    Bitmap bitmap;
//    int originalPosition, width, height;
//    @Override
//    public void onTouch(View cardView, MotionEvent event) {
//        ViewGroup.LayoutParams params = mRelativeLayout.getLayoutParams();
//        ImageView imageView;
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_MOVE:
//                int x = (int)event.getRawX();
//                int y = (int) event.getRawY();
////                params.leftMargin =  x - width / 2;
////                params.topMargin =  y - height / 2;
////                imageView = new ImageView(v.getContext());
////                imageView.setLayoutParams(params);
//                break;
//            case MotionEvent.ACTION_DOWN:
//                Toast.makeText(this, "image was touched | ACTION_DOWN", Toast.LENGTH_SHORT).show();
////                imageView = new ImageView(v.getContext());
//                ImageView photo = (ImageView) cardView.findViewById(R.id.item_post_gallery_image);
////
////                ViewGroup.LayoutParams currentParams = photo.getLayoutParams();
////                photo.setLayoutParams(params);
////                photo.requestLayout();
//                ImageView mapView = (ImageView) findViewById(R.id.post_gallery_map);
//                mapView.setImageDrawable(photo.getDrawable());
//
//                //                originalPosition = (int)v.getTag();
//////                bitmap = mAdapter...pieces.remove(originalPosition);
//////                mAdapter.notifyDataSetChanged();
////                width = bitmap.getWidth(); //maybe you need the drawable to get the intrinsic original dimens
////                height = bitmap.getHeight();
////                imageView = new ImageView(v.getContext());
////                params = new RelativeLayout.LayoutParams(width, height);
////                imageView.setImageBitmap(bitmap);
////                RelativeLayout container; //here you need to bind this view to your container of fragment, better if it RelativeLayout to do dragging
////                container.addView(imageView, params);
////                // what you need to do is inflate here
//                break;
//            case MotionEvent.ACTION_CANCEL:
//            case MotionEvent.ACTION_UP:
////                container.removeView(imageView);
////                recycleAdapter.pieces.add(originalPosition, bitmap);
////                recycleAdapter.nottifyDataSetChanged();
//                break;
//            default:
//                break;
//        }
//    }

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
