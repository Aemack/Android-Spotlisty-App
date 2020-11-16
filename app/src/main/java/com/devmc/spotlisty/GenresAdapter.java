package com.devmc.spotlisty;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.devmc.spotlisty.Model.Genre;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GenresAdapter extends RecyclerView.Adapter<GenresAdapter.MyViewHolder> {

    private ArrayList<Genre> mDataset;
    Intent i;

    public GenresAdapter(ArrayList<Genre> mDataset) {
        this.mDataset = mDataset;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_genre_item, parent, false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        //Get Element from dataset at this psoition and
        // replace contents with that element
        /*String imageUri = "https://icons-for-free.com/iconfiles/png/512/music-131964784909142833.png";
        ImageView ivBasicImage = holder.genreImage;
        Picasso.get().load(imageUri).into(ivBasicImage);
        */
        holder.genreName.setText(mDataset.get(position).getName());
        holder.parentLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                i = new Intent(view.getContext(), GeneratedFromGenreActivity.class);
                i.putExtra("genreSeed",mDataset.get(position).getName());
                view.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView genreName;
        public ConstraintLayout parentLayout;
        //public ImageView genreImage;

        public MyViewHolder(View v) {
            super(v);
            genreName = v.findViewById(R.id.genre_name);
            //genreImage = v.findViewById(R.id.genre_image);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
