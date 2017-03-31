package com.awoisoak.visor.data.source.responses;

import com.awoisoak.visor.data.source.Image;

import java.util.List;

/**
 * Response for retrieving a specific post media
 */

public class MediaFromPostResponse extends WPResponse{
    List<Image> list;

    public MediaFromPostResponse(List<Image> list) {
        this.list = list;
    }

    public List<Image> getList() {
        return list;
    }
}
