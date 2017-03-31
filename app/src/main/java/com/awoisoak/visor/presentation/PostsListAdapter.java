package com.awoisoak.visor.presentation;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.awoisoak.visor.R;
import com.awoisoak.visor.data.source.Post;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by awo on 31/03/17.
 */

public class PostsListAdapter extends RecyclerView.Adapter<PostsListAdapter.PostViewHolder> {

    private List<Post> mPosts;
    private PostItemClickListener mListener;
    private Context mContext;

    public interface PostItemClickListener{
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

    public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.item_post_image) ImageView featuredImage;

        public PostViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindPost(Post post){
            if (post.getFeaturedImage().equals("") || post.getFeaturedImage() == null){
                featuredImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.logo));
            }
            Glide.with(mContext).load(post.getFeaturedImage()).into(featuredImage);
        }

        @Override
        public void onClick(View v) {
            mListener.onPostItemClick(getAdapterPosition());
        }
    }
}
