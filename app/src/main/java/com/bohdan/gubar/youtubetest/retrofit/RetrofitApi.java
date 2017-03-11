package com.bohdan.gubar.youtubetest.retrofit;

import com.bohdan.gubar.youtubetest.model.playlist.VideoInfo;
import com.bohdan.gubar.youtubetest.model.video.VideoDetail;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by gubar on 10.03.2017.
 */

public interface RetrofitApi {
    @GET("playlistItems")
    Call<VideoInfo> listRepos(@QueryMap Map<String, String> options);

    @GET("videos")
    Call<VideoDetail> videoDetail(@QueryMap Map<String, String> options);
}
