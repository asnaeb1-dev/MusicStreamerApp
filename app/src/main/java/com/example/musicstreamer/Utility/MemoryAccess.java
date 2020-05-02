package com.example.musicstreamer.Utility;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;

public class MemoryAccess {

    private Context context;

    private String[] projection = {"*"};

    public MemoryAccess(Context context) {
        this.context = context;
    }

    public ArrayList<TempAudio> getSongs(){
        ArrayList<TempAudio> al = new ArrayList<>();
        Cursor detailsCursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection,null,null,null);
        {
            if(detailsCursor!=null)
            {
                if(detailsCursor.moveToFirst())
                {
                    do{
                        String trackName = detailsCursor.getString(detailsCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
                        String songLocation = detailsCursor.getString(detailsCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                        al.add(new TempAudio(trackName, songLocation));
                    }while(detailsCursor.moveToNext());
                }
            }
        }
        return al;
    }
}
