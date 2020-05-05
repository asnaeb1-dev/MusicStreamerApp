package com.example.musicstreamer.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadAudio {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("artist")
    @Expose
    private String artist;
    @SerializedName("album")
    @Expose
    private String album;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("uploaded_by")
    @Expose
    private String uploadedBy;

    @SerializedName("images")
    @Expose
    private String images;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("__v")
    @Expose
    private Integer v;


    public UploadAudio(String title, String artist, String album, String url, String uploadedBy, String images, String description) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.url = url;
        this.uploadedBy = uploadedBy;
        this.images = images;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public Integer getV() {
        return v;
    }
}
