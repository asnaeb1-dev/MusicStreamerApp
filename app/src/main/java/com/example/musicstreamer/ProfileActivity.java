package com.example.musicstreamer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicstreamer.POJO.SearchAudioModel;
import com.example.musicstreamer.RouteHandlers.AudioInterface;
import com.example.musicstreamer.Utility.Audio;
import com.example.musicstreamer.Utility.MemoryAccess;
import com.example.musicstreamer.Utility.SelectorApapter;
import com.example.musicstreamer.Utility.TempAudio;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.net.URI;
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
    private ProgressBar progressBar;
    private TextView findManually, filename;
    private StorageReference storageRef;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        songNameET = findViewById(R.id.songNameET);
        songArtistET = findViewById(R.id.songArtistET);
        progressBar = findViewById(R.id.uploadPB);
        uploadButton = findViewById(R.id.uploadButton);
        pushButton = findViewById(R.id.pushButton);

        findManually = findViewById(R.id.findManuallyTV);
        filename = findViewById(R.id.fileName);

        storageRef = FirebaseStorage.getInstance().getReference();
    }

    @Override
    protected void onStart() {
        super.onStart();

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
                MemoryAccess memoryAccess = new MemoryAccess(getApplicationContext());
                ArrayList<TempAudio> al  = memoryAccess.getSongs();

                Dialog dialog = new Dialog(getApplicationContext());
                dialog.setContentView(R.layout.housing_view);
                RecyclerView rv = dialog.findViewById(R.id.selectorRV);
                rv.setAdapter(new SelectorApapter(getApplicationContext(),al));
                dialog.show();
                //uploadFile
            }
        });

        pushButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

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
                    prepareAudio = new Audio(model.getTrack().getName(), model.getTrack().getAlbum().getTitle(), model.getTrack().getArtist().getName(), model.getTrack().getAlbum().getImage().get(2).getText(), model.getTrack().getWiki().getSummary());
                    Log.i("INFORMATION", prepareAudio.getTitle() + " "+ prepareAudio.getAlbumName()+" "+prepareAudio.getArtistName()+" "+prepareAudio.getImageURL()+" ");
                }
            }

            @Override
            public void onFailure(Call<SearchAudioModel> call, Throwable t) {
                Log.e("ERROR", t.getMessage().toString());
            }
        });
    }

    private void uploadFile(String albumName, String filename, File file){
        Uri file1 = Uri.fromFile(file);
        StorageReference riversRef = storageRef.child(albumName+"/"+filename);

        riversRef.putFile(file1)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Toast.makeText(ProfileActivity.this, "File Uploaded! Press 'push' button to push your audio.", Toast.LENGTH_SHORT).show();
                        pushButton.setEnabled(true);
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    int progress = Math.round(taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount())*100;
                    progressBar.setProgress(progress);

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

}
