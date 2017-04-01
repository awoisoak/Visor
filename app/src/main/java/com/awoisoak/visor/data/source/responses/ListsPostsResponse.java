package com.awoisoak.visor.data.source.responses;

import com.awoisoak.visor.data.source.Post;

import java.util.List;

/**
 * Response for a List Posts Requests
 */
public class ListsPostsResponse extends WPResponse{

    List<Post> list;

    public ListsPostsResponse(List<Post> list) {
        this.list = list;
    }

    public List<Post> getList() {
        return list;
    }
}
