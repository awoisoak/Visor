package com.awoisoak.visor.data;

import com.awoisoak.visor.data.source.Post;
import com.awoisoak.visor.data.source.WPAPI;
import com.awoisoak.visor.data.source.WPListener;
import com.awoisoak.visor.data.source.responses.ListsPostsResponse;
import com.awoisoak.visor.data.source.responses.MediaFromPostResponse;

import java.util.ArrayList;
import java.util.List;

/**
 *  Mock Wordpress Manager
 */

public class MockWPManager implements WPAPI {
    /**
     * It assumes there is 15 posts in the website. As the default per_page argument is 10, it means 2 total pages
     * +info: https://developer.wordpress.org/rest-api/reference/posts/#arguments
     */
    private static int TOTAL_POSTS = 15;
    private static int TOTAL_PAGES = 2;
    private static int STATUS_OK = 200;

    static MockWPManager mInstance;

    public static MockWPManager getInstance() {
        if (mInstance == null) {
            mInstance = new MockWPManager();
        }
        return mInstance;
    }

    @Override
    public void listPosts(int offset, WPListener<ListsPostsResponse> l) {
        System.out.println("awoooooo | MockWPManager | listPosts");
        List<Post> postList = new ArrayList<Post>();
        ListsPostsResponse r = null;

        //Fill the List with all Posts available
        Post post;
        for (int i = 0; i < TOTAL_POSTS; i++) {
            post = new Post(Integer.toString(i), "01/01/01", "01/01/02", "post" + i,
                            "http://awoisoak.com/wp-content/uploads/2017/02/dsc02961_resized-768x513.jpg",
                            "url_attachment");
            postList.add(post);
        }
        // Modify the List depending on the offset
        if (offset < postList.size()) {
            postList = postList.subList(offset, postList.size() - 1);
            r = new ListsPostsResponse(postList);
            r.setCode(STATUS_OK);
            r.setTotalPages(TOTAL_PAGES);
            r.setTotalRecords(TOTAL_POSTS);
        } else {
            List<Post> emptyList = new ArrayList<>();
            r = new ListsPostsResponse(emptyList);
            r.setCode(STATUS_OK);
            r.setTotalPages(0);
            r.setTotalRecords(0);
        }
        l.onResponse(r);
    }

    @Override
    public void retrieveAllMediaFromPost(String parent, WPListener<MediaFromPostResponse> l) {
        //TODO
    }
}
