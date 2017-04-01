package com.awoisoak.visor.presentation.postlist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.awoisoak.visor.R;
import com.awoisoak.visor.data.source.Post;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PostsListAdapter extends RecyclerView.Adapter<PostsListAdapter.PostViewHolder> {

    private List<Post> mPosts;
    private PostItemClickListener mListener;
    private Context mContext;

    public interface PostItemClickListener {
        void onPostItemClick(int item);
    }

    public PostsListAdapter(List<Post> posts, PostItemClickListener listener, Context context) {
        mPosts = posts;
        mListener = listener;
        mContext = context;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_post, parent, false);
        return new PostViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        holder.bindPost(mPosts.get(position));
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
    }


//    public void addNewPosts(List<Post> newPosts) {
////        mPosts.addAll(newPosts);
////        notifyItemRangeInserted(getItemCount() - newPosts.size(), newPosts.size());
////        notifyDataSetChanged();
//    }

    public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.item_post_image) ImageView featuredImage;
        @BindView(R.id.item_post_title) TextView title;

        public PostViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void bindPost(Post post) {
            if (post.getFeaturedImage().equals("") || post.getFeaturedImage() == null) {
                featuredImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.logo));
            }
            Glide.with(mContext).load(post.getFeaturedImage()).error(R.drawable.hal_9000).listener(
                    new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target,
                                                   boolean isFirstResource) {
                            System.out.println("awoooo | Glider listeber | onException | printing trace...");
                            e.printStackTrace();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model,
                                                       Target<GlideDrawable> target,
                                                       boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .placeholder(R.drawable.gradient).crossFade(1000).into(featuredImage);
            title.setText(post.getTitle());
        }

        @Override
        public void onClick(View v) {
            mListener.onPostItemClick(getAdapterPosition());
        }
    }
}
