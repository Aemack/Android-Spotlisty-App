package com.devmc.spotlisty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devmc.spotlisty.Connectors.SavePlaylistService;
import com.devmc.spotlisty.Connectors.UserPlaylistService;
import com.devmc.spotlisty.Connectors.UserService;
import com.devmc.spotlisty.Model.Playlist;

import java.util.ArrayList;

public class SavePlaylistActivity extends Activity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private UserPlaylistService userPlaylistService;
    private ArrayList<Playlist> userPlaylists;
    private Button userPlaylistsBtn;
    private String trackUris;
    private String playlistName;

    private SavePlaylistService savePlaylistService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_playlists);
        recyclerView = (RecyclerView) findViewById(R.id.playlist_recycler_view);
        userPlaylistService = new UserPlaylistService(getApplicationContext());

        savePlaylistService = new SavePlaylistService(getApplicationContext());

        userPlaylistsBtn = (Button) findViewById(R.id.your_playlist_button);
        userPlaylistsBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(v.getContext(), PlaylistsActivity.class));
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            playlistName = extras.getString("playlistName");
            trackUris = extras.getString("trackUris");
        }

        savePlaylistService.setPlaylistName(playlistName);
        savePlaylistService.setTrackUris(trackUris);
        savePlaylistService.savePlaylist(() -> {

            recyclerView.setHasFixedSize(true);

            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);

            //Call to get userId to be able to save playlist
            getPlaylists();
            Toast.makeText(getApplicationContext(),"Playlist - "+playlistName+" - Saved",Toast.LENGTH_SHORT).show();
            });





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


