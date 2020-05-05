package com.example.musicstreamer.Utility;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;

import com.example.musicstreamer.Config;

import java.util.ArrayList;

public class Audio_Service extends Service implements
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnSeekCompleteListener,
        AudioManager.OnAudioFocusChangeListener {

    private MediaPlayer mediaPlayer;
    private IBinder iBinder = new MusicBinder();
    private ArrayList<String> audioData = new ArrayList<>();

    private int songPosition;


    @Override
    public IBinder onBind(Intent intent) {
        if(intent!=null){
            return iBinder;
        }
        return  null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mediaPlayer = new MediaPlayer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        songPosition = intent.getIntExtra("position", 0);
        audioData = intent.getStringArrayListExtra("songList");
        initializeMediaPlayer();
        return START_NOT_STICKY;
    }

    private void initializeMediaPlayer() {

        mediaPlayer.reset();
        try{
            mediaPlayer.setDataSource(audioData.get(songPosition));
        }catch(Exception e){
            e.printStackTrace();
        }
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.prepareAsync();
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnPreparedListener(this);
        trackChanged();
    }

    public void loopSong(){
        if (mediaPlayer.isLooping()) {
            mediaPlayer.setLooping(false);
        } else {
            mediaPlayer.setLooping(true);
        }
    }

    public void playPauseAudio(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }else{
            mediaPlayer.start();
        }
    }

    public void playNext(){
        if(songPosition>audioData.size()-1){
            songPosition = 0;
            return;
        }
        songPosition++;
        initializeMediaPlayer();
    }

    public void playPrevious(){
        if(songPosition<0){
            songPosition = 0;
            return;
        }
        songPosition--;
        initializeMediaPlayer();
    }

    private void trackChanged(){
        Intent intent = new Intent(Config.TRACK_CHANGE_ACTION);
        intent.putExtra("position", songPosition);
        intent.putExtra("sessionid", mediaPlayer.getAudioSessionId());
        sendBroadcast(intent);
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange)
        {
            case AudioManager.AUDIOFOCUS_GAIN:
                // resume playback
                if (mediaPlayer == null) initializeMediaPlayer();
                else if (!mediaPlayer.isPlaying()) playPauseAudio();
                mediaPlayer.setVolume(1.0f, 1.0f);
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                // Lost focus for an unbounded amount of time: stop playback and release media player
                if (mediaPlayer.isPlaying())
                    playPauseAudio();
                mediaPlayer.pause();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                // Lost focus for a short time, but we have to stop
                // playback. We don't release the media player because playback
                // is likely to resume
                if (mediaPlayer.isPlaying())
                    playPauseAudio();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                // Lost focus for a short time, but it's ok to keep playing
                // at an attenuated level
                if (mediaPlayer.isPlaying())
                    mediaPlayer.setVolume(0.1f, 0.1f);
                break;
        }

    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        playNext();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();

        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        mp.start();
    }

    public class MusicBinder extends Binder {
        public Audio_Service getMusicService()
        {
            return Audio_Service.this;
        }
    }
}
