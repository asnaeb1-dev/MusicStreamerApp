package com.example.musicstreamer.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Model1 {

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("token")
    @Expose
    private String token;

    public Model1(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Model1(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getToken() {
        return token;
    }
}
