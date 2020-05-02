package com.example.musicstreamer.Utility;

public class Audio {
    private String title;
    private String albumName;
    private String artistName;
    private String imageURL;
    private String summary;

    public Audio(String title, String albumName, String artistName, String imageURL, String summary) {
        this.title = title;
        this.albumName = albumName;
        this.artistName = artistName;
        this.imageURL = imageURL;
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
}
