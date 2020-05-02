package com.example.musicstreamer.RouteHandlers;

import com.example.musicstreamer.POJO.Model1;
import com.example.musicstreamer.POJO.ModelLogout;
import com.example.musicstreamer.POJO.ModelUser;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface UserInterface {

    @POST("/user/create")
    Call<Model1> createUser(@Body Model1 model1);

    @POST("/user/login")
    Call<Model1> loginUser(@Body Model1 model1);

    @GET("/user/logout")
    Call<ModelLogout> logoutUser(@Header("Authorization") String token);

    @GET("/user/me")
    Call<ModelUser> getUserProfile(@Header("Authorization") String token);
}
