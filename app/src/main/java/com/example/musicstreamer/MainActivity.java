package com.example.musicstreamer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.musicstreamer.POJO.GetAudio;
import com.example.musicstreamer.POJO.Model1;
import com.example.musicstreamer.POJO.ModelLogout;
import com.example.musicstreamer.POJO.ModelUser;
import com.example.musicstreamer.RouteHandlers.AudioInterface;
import com.example.musicstreamer.RouteHandlers.UserInterface;
import com.example.musicstreamer.Utility.Audio;
import com.example.musicstreamer.Utility.Audio_Service;
import com.example.musicstreamer.Utility.MainUser;
import com.example.musicstreamer.Utility.SongListAdapter;
import com.gauravk.audiovisualizer.visualizer.BarVisualizer;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.slidingpanelayout.widget.SlidingPaneLayout;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private SlidingUpPanelLayout slidingPaneLayout;
    private ConstraintLayout smallPlayer;
    private ProgressBar progressBar;
    private DrawerLayout drawerLayout;
    private boolean panelUP = false;
    private AppBarLayout appBarLayout;

    private String TAG = "INFORMATION";

    private String email, username;
    private String[] genresLiked;
    private String _id;

    private Toolbar toolbar;
    private ArrayList<Audio> audioList;

    private RecyclerView recyclerView;
    private Audio_Service audio_service;

    //panel UI-----------------------------------------------------------
    private FloatingActionButton playPauseButton, playNext, playPrevious;
    private ImageView loopButton, shareButton, shuffleButton;
    private SeekBar audioSeekBar;
    private TextView audioNameLarge, artistNameLarge, smallTrackName, smallTrackArtist;
    private ImageView audioPoster, playPauseSmall;
    private CircleImageView smallPoster;
    private ProgressBar smallProgressBar;
    private BarVisualizer barVisualizer;
    //-------------------------------------------------------------------

    private boolean isCalled = false;
    private int songPosition, audioSessionID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        appBarLayout = findViewById(R.id.appBar);
        slidingPaneLayout = findViewById(R.id.sliding_layout);
        smallPlayer = findViewById(R.id.smallPanel);
        progressBar = findViewById(R.id.pbMainact);
        NavigationView navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        recyclerView = findViewById(R.id.audioShowerRV);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawerLayout)
                .build();

        slidingFunction();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(!isCalled){
            getUserProfile();
            getAllAudios();
            isCalled = true;
        }
        registerReceiver(songChangedBroadCastReceiver, new IntentFilter(Config.TRACK_CHANGE_ACTION));

    }

    private BroadcastReceiver songChangedBroadCastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            songPosition = intent.getIntExtra("position", 0);
            audioSessionID = intent.getIntExtra("sessionid", 0);
            activateAllUI();
        }
    };

    private void activateAllUI() {
        engagePanelUI();
        Audio song = audioList.get(songPosition);
        Glide.with(this).load(song.getImageURL()).into(audioPoster);
        Glide.with(this).load(song.getImageURL()).into(smallPoster);
        audioNameLarge.setText(song.getTitle());
        artistNameLarge.setText(song.getArtistName());
        smallTrackName.setText(song.getTitle());
        smallTrackArtist.setText(song.getArtistName());
        barVisualizer.setAudioSessionId(audioSessionID);
    }

    private void slidingFunction(){
        slidingPaneLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                float presentVal = 0f;
                if(slideOffset>presentVal)
                {
                    smallPlayer.animate().alpha(0).setDuration(200);
                    smallPlayer.setVisibility(View.GONE);
                    //audioPBar.setVisibility(View.GONE);
                    panelUP = true;
                }
                else
                {
                    smallPlayer.setVisibility(View.VISIBLE);
                    //audioPBar.setVisibility(View.VISIBLE);
                    smallPlayer.animate().alpha(1).setDuration(200);
                    panelUP = false;
                }

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {

            }
        });
    }

    private void engagePanelUI(){
        playPauseButton = findViewById(R.id.playPauseLarge);//
        playNext = findViewById(R.id.playNext);//
        playPrevious = findViewById(R.id.previousTrack);//
        audioSeekBar = findViewById(R.id.seekBar);
        shuffleButton = findViewById(R.id.shuffle);
        loopButton = findViewById(R.id.repeat);//
        shareButton = findViewById(R.id.share);
        audioNameLarge = findViewById(R.id.trackNameLarge);
        artistNameLarge = findViewById(R.id.trackArtistLarge);
        audioPoster = findViewById(R.id.audioPoster);//
        smallPoster = findViewById(R.id.circleImageView);
        smallTrackName = findViewById(R.id.audioNameSmall);
        smallTrackArtist = findViewById(R.id.smallAudioAlbum);
        barVisualizer = findViewById(R.id.barVisualizerSmall);
        playPauseSmall = findViewById(R.id.smallPlayPause);//

        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audio_service.playPauseAudio();
            }
        });

        playPauseSmall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audio_service.playPauseAudio();
            }
        });

        playNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audio_service.playNext();
            }
        });

        playPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audio_service.playPrevious();
            }
        });

        loopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audio_service.loopSong();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_logout:
                //show confirmation dialog
                new AlertDialog.Builder(this)
                        .setIcon(getResources().getDrawable(R.drawable.app_icon))
                        .setTitle("Logout")
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                logoutUser();
                            }
                        }).setNegativeButton("No", null).show();
                return true;

            case R.id.action_profile:
                if(_id!=null){
                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    intent.putExtra("_id_", _id);
                    startActivity(intent);
                    finish();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    private void logoutUser(){
        progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserInterface ui = retrofit.create(UserInterface.class);
        Call<ModelLogout> call = ui.logoutUser(getSharedPreferences("PREFS", MODE_PRIVATE).getString("token", null));
        call.enqueue(new Callback<ModelLogout>() {
            @Override
            public void onResponse(Call<ModelLogout> call, Response<ModelLogout> response) {
                if(response.isSuccessful()){
                    ModelLogout expModel = response.body();
                    if(expModel.getMessage().equals("logged_out")){
                        SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                        editor.remove("token");
                        editor.apply();
                        progressBar.setVisibility(View.GONE);
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                    }else{
                        Snackbar.make(drawerLayout, "Failed to logout", Snackbar.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        return;
                    }
                }
            }

            @Override
            public void onFailure(Call<ModelLogout> call, Throwable t) {
                Log.e("ERROR", t.getMessage().toString());
            }
        });
    }

    private void getUserProfile(){
        progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserInterface ui = retrofit.create(UserInterface.class);
        Call<ModelUser> call = ui.getUserProfile(getSharedPreferences("PREFS", MODE_PRIVATE).getString("token", null));
        call.enqueue(new Callback<ModelUser>() {
            @Override
            public void onResponse(Call<ModelUser> call, Response<ModelUser> response) {
                if(response.isSuccessful()){
                   assert response.body() != null;
                    _id = response.body().getId();
                    toolbar.setTitle("Hi!, "+response.body().getUsername());
                    if(response.body().getGenresLiked().size()>0){
                       for(int i = 0;i<response.body().getGenresLiked().size();i++){
                           genresLiked[i] = response.body().getGenresLiked().get(i).getGenre();
                       }
                       new MainUser(response.body().getUsername(),response.body().getEmail(), genresLiked);
                   }else{
                       //show genre selection dialog
                       /**
                        * TODO:
                        */
                   }
                }
            }

            @Override
            public void onFailure(Call<ModelUser> call, Throwable t) {
                Log.e("ERROR", t.getMessage().toString());
            }
        });

    }

    private void getAllAudios(){
        audioList = new ArrayList<>();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AudioInterface ai = retrofit.create(AudioInterface.class);
        Call<List<GetAudio>> call = ai.getAllAudio(getSharedPreferences("PREFS", MODE_PRIVATE).getString("token", null));
        call.enqueue(new Callback<List<GetAudio>>() {
            @Override
            public void onResponse(Call<List<GetAudio>> call, Response<List<GetAudio>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    List<GetAudio> list = response.body();
                    for (GetAudio item : list) {
                        audioList.add(new Audio(item.getTitle(), item.getAlbum(), item.getArtist(), item.getImages(), item.getDescription(), item.getUrl(), item.getUploadedBy()));
                        Log.i("ITEM", item.getTitle());
                    }
                    recyclerView.setAdapter(new SongListAdapter(MainActivity.this, audioList));
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    engagePanelUI();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<GetAudio>> call, Throwable t) {
                Log.e("ERROR", t.getMessage().toString());
                progressBar.setVisibility(View.GONE);

            }
        });

    }
}
