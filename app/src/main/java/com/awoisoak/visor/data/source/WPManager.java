package com.awoisoak.visor.data.source;

import android.support.annotation.Nullable;

import com.awoisoak.visor.data.source.responses.ErrorResponse;
import com.awoisoak.visor.data.source.responses.ListsPostsResponse;
import com.awoisoak.visor.data.source.responses.MediaFromPostResponse;
import com.awoisoak.visor.data.source.responses.WPResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * WordPress Manager to attack awoisoak.com
 */

public class WPManager implements WPAPI {
    private static final String MARKER = WPManager.class.getSimpleName();

    String URL = "http://awoisoak.com/";
    static WPService service;
    static int NO_CODE = -1;
    private static WPManager instance;
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(ListsPostsResponse.class, new ListPostsDeserializer<ListsPostsResponse>())
            .disableHtmlEscaping()
            .registerTypeAdapter(MediaFromPostResponse.class, new MediaFromPostDeserializer<MediaFromPostResponse>())
            .create();

    public static WPManager getInstance() {
        if (instance == null) {
            instance = new WPManager();
        }
        return instance;
    }

    public WPManager() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        service = retrofit.create(WPService.class);
    }


    @Override
    public void getPosts(@Nullable int offset, WPListener<ListsPostsResponse> l) {
        Call<ListsPostsResponse> c = service.getPosts(offset);
        responseRequest(c, l);
    }

    @Override
    public void getLastPostsFrom(String date, WPListener<ListsPostsResponse> l) {
        Call<ListsPostsResponse> c = service.getLastPostsFrom(date);
        responseRequest(c, l);
    }

    @Override
    public void getImagesFromPost(String parent, int offset, WPListener<MediaFromPostResponse> l) {
        Call<MediaFromPostResponse> c = service.getImagesFromPost(parent, offset);
        responseRequest(c, l);
    }


    private <T extends WPResponse> void responseRequest(Call<T> c, WPListener<T> l) {
        try {
            Response<T> retrofitResponse = c.execute();
            T response = retrofitResponse.body();
            int code = retrofitResponse.code();
            if (retrofitResponse.isSuccessful()) {
                String totalPages = retrofitResponse.headers().get(WPService.HEADER_TOTAL_PAGES);
                String totalRecords = retrofitResponse.headers().get(WPService.HEADER_TOTAL_RECORDS);
                if (totalPages != null && totalRecords != null) {
                    response.setTotalPages(Integer.parseInt(totalPages));
                    response.setTotalRecords(Integer.parseInt(totalRecords));
                }
                response.setCode(retrofitResponse.code());
                l.onResponse(response);
            } else {
                ErrorResponse errorResponse = convertErrorBody(retrofitResponse);
                l.onError(errorResponse);
            }
        } catch (IOException e) {
            ErrorResponse errorResponse = new ErrorResponse("IOexception while communicating with the Wordpress site");
            errorResponse.setCode(NO_CODE);
            l.onError(errorResponse);
            e.printStackTrace();
        }
    }

    private ErrorResponse convertErrorBody(Response retrofitResponse) {
        ErrorResponse errorResponse = null;
        errorResponse = new ErrorResponse(retrofitResponse.message());
        errorResponse.setCode(retrofitResponse.code());
        return errorResponse;
    }
}
