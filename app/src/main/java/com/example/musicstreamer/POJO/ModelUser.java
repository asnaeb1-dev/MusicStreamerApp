package com.example.musicstreamer.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelUser {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("genres_liked")
    @Expose
    private List<GenresLiked> genresLiked;
    @SerializedName("__v")
    @Expose
    private Integer v;

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public List<GenresLiked> getGenresLiked() {
        return genresLiked;
    }

    public class GenresLiked {

        @SerializedName("_id")
        @Expose
        private String id;
        @SerializedName("genre")
        @Expose
        private String genre;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getGenre() {
            return genre;
        }

        public void setGenre(String genre) {
            this.genre = genre;
        }

    }
}
