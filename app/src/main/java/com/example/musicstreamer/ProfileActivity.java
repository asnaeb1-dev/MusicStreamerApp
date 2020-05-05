package com.example.musicstreamer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicstreamer.POJO.SearchAudioModel;
import com.example.musicstreamer.POJO.UploadAudio;
import com.example.musicstreamer.RouteHandlers.AudioInterface;
import com.example.musicstreamer.Utility.Audio;
import com.example.musicstreamer.Utility.MemoryAccess;
import com.example.musicstreamer.Utility.SelectorAdapter;
import com.example.musicstreamer.Utility.TempAudio;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileActivity extends AppCompatActivity {

    private EditText songNameET, songArtistET;
    private Audio prepareAudio;

    private Button findButton, uploadButton, pushButton;
    private ProgressBar progressBar, roundPB;
    private TextView findManually, filename;
    private StorageReference storageRef;
    private Intent intent;
    private Dialog dialog;
    private String _id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        songNameET = findViewById(R.id.songNameET);
        songArtistET = findViewById(R.id.songArtistET);
        progressBar = findViewById(R.id.uploadPB);
        uploadButton = findViewById(R.id.uploadButton);
        pushButton = findViewById(R.id.pushButton);
        findButton = findViewById(R.id.findAudioButton);
        findManually = findViewById(R.id.findManuallyTV);
        filename = findViewById(R.id.fileName);
        roundPB = findViewById(R.id.pbUploadScreen);

        storageRef = FirebaseStorage.getInstance().getReference();
    }

    @Override
    protected void onStart() {
        super.onStart();

        _id = getIntent().getStringExtra("_id_");

        dialog = new Dialog(ProfileActivity.this);
        findManually.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileActivity.this, "Show find manually dialog box", Toast.LENGTH_SHORT).show();
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //generate selector dialog
                progressBar.setProgress(0);
                MemoryAccess memoryAccess = new MemoryAccess(getApplicationContext());
                ArrayList<TempAudio> al  = memoryAccess.getSongs();

                dialog.setContentView(R.layout.housing_view);
                RecyclerView rv = dialog.findViewById(R.id.selectorRV);
                rv.setAdapter(new SelectorAdapter(getApplicationContext(),al));
                rv.setLayoutManager(new LinearLayoutManager(ProfileActivity.this));
                dialog.show();
            }
        });

        registerReceiver(broadcastReceiver, new IntentFilter("SEND_DATA"));
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            dialog.dismiss();
            uploadFile( intent.getStringExtra("trackname"), new File(intent.getStringExtra("data")));
        }
    };

    public void findAudioDetails(View view) {
        String songName = songNameET.getText().toString();
        String songArtist = songArtistET.getText().toString();

        findAudio(songName, songArtist);
    }

    private void findAudio(String songName, String songArtist){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AudioInterface ai = retrofit.create(AudioInterface.class);
        Call<SearchAudioModel> call = ai.findAudio(songName, songArtist);
        call.enqueue(new Callback<SearchAudioModel>() {
            @Override
            public void onResponse(Call<SearchAudioModel> call, Response<SearchAudioModel> response) {
                if(response.isSuccessful()){
                    assert response.body() != null;
                    SearchAudioModel model = response.body();
                    if(model.getTrack().getAlbum().getTitle()!= null ||model.getTrack().getArtist().getName()!=null || model.getTrack().getAlbum().getImage().get(2).getText()!=null || model.getTrack().getWiki().getSummary() != null){
                        prepareAudio = new Audio(model.getTrack().getName(), model.getTrack().getAlbum().getTitle(), model.getTrack().getArtist().getName(), model.getTrack().getAlbum().getImage().get(2).getText(), model.getTrack().getWiki().getSummary());
                    }else if(model.getTrack().getAlbum().getTitle() == null){
                        //TODO
                    }
                    Snackbar.make(findViewById(R.id.parentLayoutUpload), "Found!", Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SearchAudioModel> call, Throwable t) {
                Snackbar.make(findViewById(R.id.parentLayoutUpload), "Sorry! Not found.", Snackbar.LENGTH_LONG).show();
                Log.e("ERROR", t.getMessage().toString());
            }
        });
    }

    private void uploadFile(String filename, File file){
        Uri file1 = Uri.fromFile(file);
        final StorageReference audioRef = storageRef.child("music/"+new Config().generateAudioName());

        audioRef.putFile(file1)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        pushButton.setEnabled(true);

                        audioRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                prepareAudio.setUrl(uri.toString());
                                prepareAudio.setUploaded_by(_id);
                                Log.i("INFORMATION", prepareAudio.getTitle() + " "+ prepareAudio.getAlbumName()+" "+prepareAudio.getArtistName()+" "+prepareAudio.getImageURL()+" " + prepareAudio.getUrl() +" "+ prepareAudio.getUploaded_by());
                                pushButton.setEnabled(true);
                            }
                        });
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    progressBar.setProgress((int)progress);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        Toast.makeText(ProfileActivity.this, "Failed to upload!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void pushAudio(View view) {
        roundPB.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AudioInterface ai = retrofit.create(AudioInterface.class);
        Call<UploadAudio> call = ai.pushAudio(new UploadAudio(prepareAudio.getTitle(),
                prepareAudio.getArtistName(),
                prepareAudio.getAlbumName(),
                prepareAudio.getUrl(),
                prepareAudio.getUploaded_by(),
                prepareAudio.getImageURL(),
                prepareAudio.getSummary()),
                getSharedPreferences("PREFS", MODE_PRIVATE).getString("token", null));
        call.enqueue(new Callback<UploadAudio>() {
            @Override
            public void onResponse(Call<UploadAudio> call, Response<UploadAudio> response) {
                if(response.isSuccessful()){
                    assert response.body() != null;
                    if(response.body().getId() != null){
                        Snackbar.make(findViewById(R.id.parentLayoutUpload),"Successfully pushed! Song now available to everyone!", Snackbar.LENGTH_LONG).show();
                    }
                }
                roundPB.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<UploadAudio> call, Throwable t) {
                Snackbar.make(findViewById(R.id.parentLayoutUpload), "Sorry! Failed to push.", Snackbar.LENGTH_LONG).show();
                Log.e("ERROR", t.getMessage().toString());
                roundPB.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        unregisterReceiver(broadcastReceiver);
        finish();
    }
}
