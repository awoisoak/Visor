package com.awoisoak.visor.domain.interactors;

public interface PostGalleryInteractor {

    /**
     * Retrieve the number of images given the passed offset.
     * {@link com.awoisoak.visor.data.source.WPAPI#MAX_NUMBER_IMAGES_RETURNED}
     *
     * @param offset, Offset the result set by a specific number of items.
     * @return
     */
    void getImages(String postId, int offset);
}
