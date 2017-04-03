package com.awoisoak.visor.data;

import android.util.Log;

import com.awoisoak.visor.data.source.Image;
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
    private static final String MARKER = MockWPManager.class.getSimpleName();

    /**
     * It assumes there is 15 posts/images in the website. As the default per_page argument is 10, it means 2 total pages
     * +info: https://developer.wordpress.org/rest-api/reference/posts/#arguments
     */
    private static int TOTAL_RECORDS = 15;
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
        for (int i = 0; i < TOTAL_RECORDS; i++) {
            post = new Post(Integer.toString(i), "01/01/01", "01/01/02", "post" + i,
                            "http://awoisoak.com/wp-content/uploads/2017/02/dsc02961_resized-768x513.jpg",
                            "http://awoisoak.com/wp-content/uploads/2017/02/dsc02961_resized-400x267.jpg",
                            "url_attachment");
            postList.add(post);
        }
        // Modify the List depending on the offset
        if (offset < postList.size()) {
            postList = postList.subList(offset, postList.size() - 1);
            r = new ListsPostsResponse(postList);
            r.setCode(STATUS_OK);
            r.setTotalPages(TOTAL_PAGES);
            r.setTotalRecords(TOTAL_RECORDS);
        } else {
            List<Post> emptyList = new ArrayList<>();
            r = new ListsPostsResponse(emptyList);
            r.setCode(STATUS_OK);
            r.setTotalPages(0);
            r.setTotalRecords(0);
        }

        try {
            Log.d(MARKER,"Sending request to the Mock server.... ;)");
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        l.onResponse(r);
    }

    @Override
    public void retrieveAllMediaFromPost(String parent, int offset, WPListener<MediaFromPostResponse> l) {
        System.out.println("awoooooo | MockWPManager | retrieveAllMediaFromPost");
        List<Image> imageList = new ArrayList<Image>();
        MediaFromPostResponse r = null;

        //Fill the List with all Images available
        Image image;
        for (int i = 0; i < TOTAL_RECORDS; i++) {
             image =
                    new Image("http://awoisoak.com/wp-content/uploads/2015/11/dsc06532_lzn_wm.resized-150x150.jpg",
                              "http://awoisoak.com/wp-content/uploads/2015/11/dsc06532_lzn_wm.resized-400x400.jpg",
                              "http://awoisoak.com/wp-content/uploads/2015/11/dsc06532_lzn_wm.resized-400x225.jpg",
                              "http://awoisoak.com/wp-content/uploads/2015/11/dsc06532_lzn_wm.resized-840x473.jpg",
                              "http://awoisoak.com/wp-content/uploads/2015/11/dsc06532_lzn_wm.resized-1024x577.jpg",
                              "http://awoisoak.com/wp-content/uploads/2015/11/dsc06532_lzn_wm.resized.jpg");
            imageList.add(image);
        }

        // Modify the List depending on the offset
        if (offset < imageList.size()) {
            imageList = imageList.subList(offset, imageList.size() - 1);
            r = new MediaFromPostResponse(imageList);
            r.setCode(STATUS_OK);
            r.setTotalPages(TOTAL_PAGES);
            r.setTotalRecords(TOTAL_RECORDS);
        } else {
            List<Image> emptyList = new ArrayList<>();
            r = new MediaFromPostResponse(emptyList);
            r.setCode(STATUS_OK);
            r.setTotalPages(0);
            r.setTotalRecords(0);
        }

        try {
            Log.d(MARKER,"Sending request to the Mock server.... ;)");
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        l.onResponse(r);
    }

}
