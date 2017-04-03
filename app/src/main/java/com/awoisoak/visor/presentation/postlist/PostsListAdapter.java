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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PostsListAdapter extends RecyclerView.Adapter<PostsListAdapter.PostViewHolder> {

    private List<Post> mPosts;
    private PostItemClickListener mListener;
    private Context mContext;

    public interface PostItemClickListener {
        void onPostItemClick(Post post);
    }

    public PostsListAdapter(List<Post> posts, PostItemClickListener listener, Context context) {
        mPosts = posts;
        mListener = listener;
        mContext = context;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_post_list, parent, false);
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

            Glide.with(mContext).load(post.getFeaturedImage())
                    .thumbnail(Glide.with(mContext).load(post.getFeaturedImageSmall())).dontAnimate()
                    .fitCenter().error(R.drawable.hal_9000).placeholder(R.drawable.place_holder_black)
                    .crossFade(1000).into(featuredImage);

            title.setText(post.getTitle());
        }

        @Override
        public void onClick(View v) {
            mListener.onPostItemClick(mPosts.get(getAdapterPosition()));
        }
    }
}
