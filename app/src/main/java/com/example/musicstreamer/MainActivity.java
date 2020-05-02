package com.example.musicstreamer;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ProgressBar;

import com.example.musicstreamer.POJO.Model1;
import com.example.musicstreamer.POJO.ModelLogout;
import com.example.musicstreamer.POJO.ModelUser;
import com.example.musicstreamer.RouteHandlers.UserInterface;
import com.example.musicstreamer.Utility.MainUser;
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
import androidx.slidingpanelayout.widget.SlidingPaneLayout;

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

    private Toolbar toolbar;

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

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawerLayout)
                .build();

        slidingFunction();
    }

    @Override
    protected void onStart() {
        super.onStart();

        getUserProfile();
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
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
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
                   toolbar.setTitle("Hi!, "+username);
                   assert response.body() != null;
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
}
