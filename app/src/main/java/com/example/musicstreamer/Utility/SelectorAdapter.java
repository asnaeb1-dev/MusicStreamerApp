package com.example.musicstreamer.Utility;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicstreamer.R;

import java.util.ArrayList;

public class SelectorAdapter extends RecyclerView.Adapter<SelectorAdapter.Viewholder>{
    private Context context;
    private ArrayList<TempAudio> songAl;

    public SelectorAdapter(Context context, ArrayList<TempAudio> songAl) {
        this.context = context;
        this.songAl = songAl;
    }


    @NonNull
    @Override
    public SelectorAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //responsible for inflating the view
        View view = LayoutInflater.from(context).inflate(R.layout.unit_ui, parent, false);
        //create the object of the Viewholder class down below
        SelectorAdapter.Viewholder viewholder = new SelectorAdapter.Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull final SelectorAdapter.Viewholder holder, final int position) {
        //changes wrt to what the layout are and add a new item
        //takes the content and shows it on the imageView

        holder.audioName.setText(songAl.get(holder.getAdapterPosition()).getTrackname());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("SEND_DATA");
                intent.putExtra("trackname",songAl.get(holder.getAdapterPosition()).getTrackname());
                intent.putExtra("data", songAl.get(holder.getAdapterPosition()).getTrackdata());
                context.sendBroadcast(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        //this sets the size of the recycler view
        //without this the recycler view will show 0 items
        return songAl.size();
    }


    public class Viewholder extends RecyclerView.ViewHolder
    {
        LinearLayout linearLayout;
        TextView audioName;
        public Viewholder(View itemView) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.parentUnitLayout);
            audioName = itemView.findViewById(R.id.unitTV);
        }
    }
}
