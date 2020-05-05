package com.example.musicstreamer;

import java.util.Date;
import java.util.Random;

public class Config {

    public static String BASE_URL = "https://raha-music-streamer.herokuapp.com";
    public static String TRACK_CHANGE_ACTION = "com.example.musicstreamer.CHANGE_TRACK";

    public String generateAudioName(){
        String epoch = String.valueOf(new Date().getTime());
        //delimiter- d
        String rand = String.valueOf(new Random().nextInt(10000000));
        return epoch+"d"+rand;
    }
}
