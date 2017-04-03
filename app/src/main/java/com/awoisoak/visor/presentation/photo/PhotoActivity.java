package com.awoisoak.visor.presentation.photo;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.awoisoak.visor.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Activity to display a picture in high-res
 * (Pretty simple logic, not worth to apply MVP)
 *
 */
public class PhotoActivity extends AppCompatActivity {
    private static final String MARKER = PhotoActivity.class.getSimpleName();
    public static String EXTRA_FULL_IMAGE;
    private String mPhoto;


    @BindView(R.id.photo_activity_image_view) SubsamplingScaleImageView mImageView;
    @BindView(R.id.photo_activity_progress_bar) ProgressBar mProgressBar;
    @BindView(R.id.photo_activity_text_view) TextView mTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_fullscreen);

        ButterKnife.bind(this);

        mProgressBar.setVisibility(View.VISIBLE);
        Bundle bundle = getIntent().getExtras();
        mPhoto = bundle.getString(EXTRA_FULL_IMAGE);


        Glide.with(this).load(mPhoto)
                .asBitmap()
                .error(R.drawable.hal_9000)
                .placeholder(R.drawable.place_holder_black)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        mImageView.setImage(ImageSource.bitmap(resource));
                        mProgressBar.setVisibility(View.GONE);
                        mTextView.setVisibility(View.GONE);
                    }
                });
    }

}
