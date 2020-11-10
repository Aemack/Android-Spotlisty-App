package com.devmc.spotlisty;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.devmc.spotlisty.Model.Song;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TracksAdapter extends RecyclerView.Adapter<TracksAdapter.MyViewHolder> {


    private ArrayList<Song> mDataset;

    public TracksAdapter(ArrayList<Song> mDataset){
        this.mDataset = mDataset;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_track_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        // Get element from dataset at this position
        // Replace contents of the view with that element

        holder.trackTitle.setText((mDataset.get(position).getName()));
        holder.trackAlbum.setText(mDataset.get(position).getAlbum());
        holder.trackArtist.setText(mDataset.get(position).getArtist());

        String imageUri = mDataset.get(position).getImageUrl();
        ImageView ivBasicImage = holder.albumImage;
        Picasso.get().load(imageUri).into(ivBasicImage);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView trackTitle;
        public TextView trackAlbum;
        public TextView trackArtist;
        public ImageView albumImage;

        public MyViewHolder(View v) {
            super(v);
            trackTitle = v.findViewById(R.id.track_title);
            trackAlbum = v.findViewById(R.id.track_album);
            trackArtist = v.findViewById(R.id.track_artist);
            albumImage = v.findViewById(R.id.album_image);
        }
    }
}
