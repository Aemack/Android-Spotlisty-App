package com.devmc.spotlisty;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.devmc.spotlisty.Model.Playlist;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.MyViewHolder> {

    private ArrayList<Playlist> mDataset;
    Intent i;

    // Provide a suitable constructor (depends on the kind of dataset)
    public PlaylistAdapter(ArrayList<Playlist> myDataset) {
        //this.onItemListClick = onItemListClick;
        this.mDataset = myDataset;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_playlist_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.playlistTitle.setText(mDataset.get(position).getName());

        holder.playlistTrackCount.setText("Tracks: "+mDataset.get(position).getTrackCount());
        String imageUri = mDataset.get(position).getImageUrl();
        ImageView ivBasicImage = holder.playlistImage;
        Picasso.get().load(imageUri).into(ivBasicImage);

        holder.parentLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(view.getContext(), "PlaylistId="+mDataset.get(position).getId(), Toast.LENGTH_SHORT).show();
                i = new Intent(view.getContext(), PlaylistTracksActivity.class);
                i.putExtra("playlistId",mDataset.get(position).getId());
                view.getContext().startActivity(i);
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // each data item is just a string in this case
        public TextView playlistTitle;
        public TextView playlistTrackCount;
        public ImageView playlistImage;
        public ConstraintLayout parentLayout;
        public MyViewHolder(View v) {
            super(v);
            playlistTitle = v.findViewById(R.id.playlist_title);
            playlistTrackCount = v.findViewById(R.id.playlist_track_count);
            playlistImage = v.findViewById(R.id.playlist_image);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }


        @Override
        public void onClick(View v) {


        }
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

}