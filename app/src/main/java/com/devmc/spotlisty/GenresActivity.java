package com.devmc.spotlisty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devmc.spotlisty.Connectors.GenresService;
import com.devmc.spotlisty.Model.Genre;

import java.util.ArrayList;

//TODO:CHANGE IMAGES TO LETTERS OR SOMETHING

public class GenresActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Genre> genreSeeds;

    private Button userPlaylistsBtn;
    private Button genresBtn;
    private Button generateBtn;

    private GenresService genresService;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.genres_activity);
        recyclerView = (RecyclerView) findViewById(R.id.genres_recycler_view);

        genresService = new GenresService(getApplicationContext());

        generateBtn = (Button) findViewById(R.id.generate_button);
        generateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GenresActivity.this, GenerateFromRecentActivity.class));
            }
        });

        genresBtn = (Button) findViewById(R.id.genres_button);
        genresBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GenresActivity.this, GenresActivity.class));
            }
        });

        userPlaylistsBtn = (Button) findViewById(R.id.your_playlist_button);
        userPlaylistsBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(GenresActivity.this, PlaylistsActivity.class));
            }
        });


        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Call to get playlists
        getGenres();
    }

    public void getGenres(){

        genresService.getGenreSeeds(() -> {
           genreSeeds = genresService.getGenreSeeds();


           updateList();
        });
    }

    public void updateList(){
        mAdapter = new GenresAdapter(genreSeeds);
        // Sets adapter on recycle view
        recyclerView.setAdapter(mAdapter);
    }

}
