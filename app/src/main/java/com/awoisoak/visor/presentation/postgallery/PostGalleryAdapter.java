package com.awoisoak.visor.presentation.postgallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.awoisoak.visor.R;
import com.awoisoak.visor.data.source.Image;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.bitmap;


public class PostGalleryAdapter extends RecyclerView.Adapter<PostGalleryAdapter.PostViewHolder> {

    private List<Image> mImages;
    private ImageClickListener mListener;
    private Context mContext;

    public interface ImageClickListener {
        void onClickImage(Image image);
    }

    public PostGalleryAdapter(List<Image> images, ImageClickListener listener, Context context) {
        mImages = images;
        mListener = listener;
        mContext = context;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_post_gallery, parent, false);
        return new PostViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        holder.bindPost(mImages.get(position));
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
    }


    public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.item_post_gallery_image) ImageView imageView;

        public PostViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void bindPost(final Image image) {
            if (image.getPostBig().equals("") || image.getPostBig() == null) {
                imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.logo));
            }
            Glide.with(mContext).load(image.getPostBig())
                    .thumbnail(Glide.with(mContext).load(image.getSmallSize())).dontAnimate()
                    .fitCenter().error(R.drawable.hal_9000).placeholder(R.drawable.place_holder_black)
                    .crossFade(750).into(imageView);
        }

        @Override
        public void onClick(View v) {
            mListener.onClickImage(mImages.get(getAdapterPosition()));
        }
    }
}
