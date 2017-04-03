package com.awoisoak.visor.domain.interactors;

public interface PostsRequestInteractor {
    /**
     * Default value in WP
     * https://developer.wordpress.org/rest-api/reference/posts/#arguments
     */
    public static int MAX_NUMBER_POSTS_RETURNED = 10;

    void getPosts(int offset);
}
