package com.example.musicstreamer.Utility;

public class Audio {


    private String title;
    private String albumName;
    private String artistName;
    private String imageURL;
    private String summary;
    private String url;
    private String uploaded_by;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTitle() {
        return title;
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getSummary() {
        return summary;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public String getUploaded_by() {
        return uploaded_by;
    }

    public void setUploaded_by(String uploaded_by) {
        this.uploaded_by = uploaded_by;
    }

}
