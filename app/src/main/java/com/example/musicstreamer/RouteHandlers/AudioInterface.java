package com.example.musicstreamer.RouteHandlers;

import com.example.musicstreamer.POJO.GetAudio;
import com.example.musicstreamer.POJO.SearchAudioModel;
import com.example.musicstreamer.POJO.UploadAudio;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AudioInterface {

    @GET("/audio/search")
    Call<SearchAudioModel> findAudio(
            @Query("trackname") String trackname,
            @Query("trackartist") String trackartist
    );

    @POST("/audio/new")
    Call<UploadAudio> pushAudio(
        @Body UploadAudio uploadAudio,
        @Header("Authorization") String authorization
    );

    @GET("/audio/all")
    Call<List<GetAudio>> getAllAudio(
            @Header("Authorization") String authorization
    );
}
