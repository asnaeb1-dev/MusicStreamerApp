package com.example.musicstreamer.RouteHandlers;

import com.example.musicstreamer.POJO.SearchAudioModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AudioInterface {

    @GET("/audio/search")
    Call<SearchAudioModel> findAudio(
            @Query("trackname") String trackname,
            @Query("trackartist") String trackartist
    );

}
