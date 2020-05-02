package com.example.musicstreamer.Utility;

public class TempAudio {

    private String trackname;
    private String trackdata;

    public TempAudio(String trackname, String trackdata) {
        this.trackname = trackname;
        this.trackdata = trackdata;
    }

    public String getTrackname() {
        return trackname;
    }

    public String getTrackdata() {
        return trackdata;
    }
}
