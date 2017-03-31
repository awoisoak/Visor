package com.awoisoak.visor.data.source;


import com.awoisoak.visor.data.source.responses.ErrorResponse;

/**
 * WordPress Listener
 *
 * @param <T>
 */
public interface WPListener<T> {
    /**
     * Called when the HTTP response is in the range [200..300).
     * @param response
     */
    void onResponse(T response);

    /**
     *  {@link ErrorResponse#code} will be  {@link WPManager#NO_CODE} if there was an IOException
     *
     * @param error
     */
    void onError(ErrorResponse error);
}
