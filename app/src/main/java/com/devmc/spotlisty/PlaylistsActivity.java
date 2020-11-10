package com.devmc.spotlisty;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
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
    private Button userPlaylistsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_playlists);
        recyclerView = (RecyclerView) findViewById(R.id.playlist_recycler_view);
        userPlaylistService = new UserPlaylistService(getApplicationContext());

        userPlaylistsBtn = (Button) findViewById(R.id.your_playlist_button);
        userPlaylistsBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(PlaylistsActivity.this, PlaylistsActivity.class));
            }
        });


        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
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
