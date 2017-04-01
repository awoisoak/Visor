package com.awoisoak.visor.data.source;

import android.support.test.runner.AndroidJUnit4;

import com.awoisoak.visor.data.source.responses.ErrorResponse;
import com.awoisoak.visor.data.source.responses.ListsPostsResponse;
import com.awoisoak.visor.data.source.responses.MediaFromPostResponse;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class WPManagerTests {
    private static final int OK_RESPONSE = 200;
    private static String mPostId;


    @Test
    public void test1_listPosts() throws Exception {
        WPManager.getInstance().listPosts(0, new WPListener<ListsPostsResponse>() {
            @Override
            public void onResponse(ListsPostsResponse response) {
                assertEquals(response.getCode(), OK_RESPONSE);
                mPostId = response.getList().get(0).id;
            }

            @Override
            public void onError(ErrorResponse error) {
                System.out.println("listPosts onError" + error.getMessage());
                fail();
            }
        });
    }

    @Test
    public void test2_retrieveAllMediaFromPost() throws Exception {
        WPManager.getInstance().retrieveAllMediaFromPost(mPostId, new WPListener<MediaFromPostResponse>() {
            @Override
            public void onResponse(MediaFromPostResponse response) {
                assertEquals(response.getCode(), OK_RESPONSE);
                assertNotNull(response.getList().get(1).getFull());
                System.out.println("awooooooo | retrieveAllMediaFromPost| full image href"+response.getList().get(1).getFull());
            }

            @Override
            public void onError(ErrorResponse error) {
                System.out.println("retrieveAllMediaFromPost onError" + error.getMessage());
                fail();
            }
        });
    }

}