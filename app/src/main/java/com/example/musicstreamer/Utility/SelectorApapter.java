package com.example.musicstreamer.Utility;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SelectorApapter  extends RecyclerView.Adapter<SelectorApapter.Viewholder>{
    private Context context;
    private ArrayList<TempAudio> songAl;

    public SelectorApapter(Context context, ArrayList<TempAudio> songAl) {
        this.context = context;
        this.songAl = songAl;
    }


    @NonNull
    @Override
    public SelectorApapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //responsible for inflating the view
        TextView tv = new TextView(context);
        //create the object of the Viewholder class down below
        SelectorApapter.Viewholder viewholder = new SelectorApapter.Viewholder(tv);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull final SelectorApapter.Viewholder holder, final int position) {
        //changes wrt to what the layout are and add a new item
        //takes the content and shows it on the imageView

    }


    @Override
    public int getItemCount() {
        //this sets the size of the recycler view
        //without this the recycler view will show 0 items
        return songAl.size();
    }


    public class Viewholder extends RecyclerView.ViewHolder
    {

        public Viewholder(View itemView) {
            super(itemView);

        }
    }

}
