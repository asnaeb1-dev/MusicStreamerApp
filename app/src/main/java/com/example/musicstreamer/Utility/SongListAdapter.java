package com.example.musicstreamer.Utility;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicstreamer.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.Viewholder>{
    private Context context;
    private ArrayList<Audio> songAl;

    private ServiceConnection serviceConnection;
    private Audio_Service audio_service;

    public SongListAdapter(Context context, ArrayList<Audio> songAl) {
        this.context = context;
        this.songAl = songAl;
    }

    @NonNull
    @Override
    public SongListAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //responsible for inflating the view
        View view = LayoutInflater.from(context).inflate(R.layout.audio_shower_unit_ui, parent, false);
        //create the object of the Viewholder class down below
        SongListAdapter.Viewholder viewholder = new SongListAdapter.Viewholder(view);
        return viewholder;
    }



    @Override
    public void onBindViewHolder(@NonNull final SongListAdapter.Viewholder holder, final int position) {
        //changes wrt to what the layout are and add a new item
        //takes the content and shows it on the imageView

        holder.trackName.setText(songAl.get(holder.getAdapterPosition()).getTitle());
        holder.trackAlbum.setText(songAl.get(holder.getAdapterPosition()).getAlbumName());
        if(songAl.get(holder.getAdapterPosition()).getImageURL()!=null){
            Glide.with(context).load(songAl.get(holder.getAdapterPosition()).getImageURL()).into(holder.poster);
        }else{
            holder.poster.setImageDrawable(context.getResources().getDrawable(R.drawable.app_icon));
        }

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, songAl.get(holder.getAdapterPosition()).getTitle(), Toast.LENGTH_SHORT).show();
                initiatizeServiceConnection();
                connectToService(holder.getAdapterPosition());
            }
        });

        holder.moreIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popupMenu = new PopupMenu(context, holder.moreIV);
                popupMenu.inflate(R.menu.popup_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){
                            case R.id.action_play:
                                Toast.makeText(context, "Play audio", Toast.LENGTH_SHORT).show();

                                return true;

                            case R.id.action_download:
                                Toast.makeText(context, "Download audio", Toast.LENGTH_SHORT).show();
                                return  true;

                            case R.id.action_share:
                                Toast.makeText(context, "Share audio", Toast.LENGTH_SHORT).show();
                                return true;

                            case R.id.action_interest:
                                Toast.makeText(context, "Interested in this audio", Toast.LENGTH_SHORT).show();
                                return true;
                        }
                        popupMenu.dismiss();
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    private ArrayList<String> compileList(){
        ArrayList<String> audioData = new ArrayList<>();
        for(int i = 0;i<songAl.size();i++){
            audioData.add(songAl.get(i).getUrl());
        }
        return audioData;
    }

    private void connectToService(int position){
        Intent binderIntent = new Intent(context, Audio_Service.class);
        binderIntent.putExtra("position", position);
        binderIntent.putExtra("songList", compileList());
        context.bindService(binderIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        context.startService(binderIntent);
    }

    private void initiatizeServiceConnection(){
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Audio_Service.MusicBinder binder = (Audio_Service.MusicBinder) service;
                audio_service = binder.getMusicService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
    }

    @Override
    public int getItemCount() {
        //this sets the size of the recycler view
        //without this the recycler view will show 0 items
        return songAl.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder
    {
        ConstraintLayout constraintLayout;
        TextView trackName, trackAlbum;
        CircleImageView poster;
        ImageView moreIV;
        public Viewholder(View itemView) {
            super(itemView);

            constraintLayout = itemView.findViewById(R.id.unitUICLayout);
            trackName = itemView.findViewById(R.id.tracknameunitui);
            trackAlbum = itemView.findViewById(R.id.albumNameUnitUI);
            poster = itemView.findViewById(R.id.smallPosterImageUnitUI);
            moreIV = itemView.findViewById(R.id.more);
        }
    }
}