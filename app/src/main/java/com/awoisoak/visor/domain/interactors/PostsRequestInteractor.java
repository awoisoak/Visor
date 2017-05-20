package com.awoisoak.visor.domain.interactors;

public interface PostsRequestInteractor {


    /**
     * Retrieve the {@link com.awoisoak.visor.data.source.WPAPI#MAX_NUMBER_POSTS_RETURNED} posts using the passed offset
     * @param offset
     */
    void getPosts(int offset);

    /**
     * Retrieve the posts newer that the passed date
     * @param date
     */
    void getLastPostsFrom(String date);
}
