package com.devmc.spotlisty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devmc.spotlisty.Connectors.UserPlaylistService;
import com.devmc.spotlisty.Model.Playlist;

import java.util.ArrayList;

public class PlaylistsActivity extends Activity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private UserPlaylistService userPlaylistService;
    private ArrayList<Playlist> userPlaylists;

    //Nav Buttons
    private Button userPlaylistsBtn;
    private Button genresBtn;
    private Button generateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_playlists);
        recyclerView = (RecyclerView) findViewById(R.id.genres_recycler_view);
        userPlaylistService = new UserPlaylistService(getApplicationContext());

        generateBtn = (Button) findViewById(R.id.generate_button);
        generateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PlaylistsActivity.this, GenerateFromRecentActivity.class));
            }
        });

        genresBtn = (Button) findViewById(R.id.genres_button);
        genresBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PlaylistsActivity.this, GenresActivity.class));
            }
        });

        userPlaylistsBtn = (Button) findViewById(R.id.your_playlist_button);
        userPlaylistsBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(PlaylistsActivity.this, PlaylistsActivity.class));
            }
        });


        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //Call to get playlists
        getPlaylists();
    }

    //Get playlists from userPlaylistService
    private void getPlaylists(){
        userPlaylistService.getUserPlaylists(() -> {
            userPlaylists = userPlaylistService.getUserPlaylists();

            //Call to update playlists
            updatePlaylists();
        });
    }

    private void updatePlaylists(){
        // Creates adapter, passing in user playlists
        mAdapter = new PlaylistAdapter(userPlaylists);



        // Sets adapter on recycle view
        recyclerView.setAdapter(mAdapter);
    }
}
