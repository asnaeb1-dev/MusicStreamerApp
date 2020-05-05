package com.example.musicstreamer.Utility;

public class Audio {


    private String title;
    private String albumName;
    private String artistName;
    private String imageURL;
    private String summary;
    private String url;
    private String uploaded_by;


    public Audio(String title, String albumName, String artistName, String imageURL, String summary) {
        this.title = title;
        this.albumName = albumName;
        this.artistName = artistName;
        this.imageURL = imageURL;
        this.summary = summary;
    }

    public Audio(String title, String albumName, String artistName, String imageURL, String summary, String url, String uploaded_by) {
        this.title = title;
        this.albumName = albumName;
        this.artistName = artistName;
        this.imageURL = imageURL;
        this.summary = summary;
        this.url = url;
        this.uploaded_by = uploaded_by;
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
