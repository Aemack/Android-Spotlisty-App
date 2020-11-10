package com.devmc.spotlisty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devmc.spotlisty.Connectors.PlaylistTracksService;
import com.devmc.spotlisty.Connectors.UserPlaylistService;
import com.devmc.spotlisty.Model.Playlist;
import com.devmc.spotlisty.Model.Song;

import java.util.ArrayList;
import java.util.Random;

public class PlaylistTracksActivity extends Activity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private PlaylistTracksService playlistTracksService;
    private Song song;
    private ArrayList<Song> tracks;
    private String playlistId;
    private String trackIds;

    private Button userPlaylistsBtn;
    private Button genresBtn;
    private Button generateBtn;

    private Button generateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist_tracks_activity);
        recyclerView = findViewById(R.id.playlist_tracks_recycler_view);
        userPlaylistsBtn = (Button) findViewById(R.id.your_playlist_button);
        userPlaylistsBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(PlaylistTracksActivity.this, PlaylistsActivity.class));
            }
        });

        generateBtn = (Button) findViewById(R.id.generate_button);
        generateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PlaylistTracksActivity.this, GenerateFromRecentActivity.class));
            }
        });

        genresBtn = (Button) findViewById(R.id.genres_button);
        genresBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PlaylistTracksActivity.this, GenresActivity.class));
            }
        });

        //Generate Button
        generateButton = (Button) findViewById(R.id.generate_from_playlist_button);
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), GeneratedFromPlaylistActivity.class);
                trackIds = getTrackIds();
                i.putExtra("trackIds",trackIds);
                startActivity(i);
            }
        });


        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            playlistId = extras.getString("playlistId");
        }

        playlistTracksService = new PlaylistTracksService(getApplicationContext());
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Call to get tracks
        getTracks();
    }


    private String getTrackIds() {
        String trackIdsString = "";
        Random rand = new Random();
        int iter = 0;
        while (iter < 5){
            int num = rand.nextInt(tracks.size());
            String id = tracks.get(num).getId();
            if (iter == 0){
                trackIdsString = id;
            } else {
                trackIdsString = trackIdsString + "," + id;
            }
            iter ++;
        }
        return trackIdsString;
    }



    //Get tracks from playlistTracksService
    private void getTracks(){

        playlistTracksService.setPlaylistId(playlistId);

        playlistTracksService.getPlaylistTracks(() -> {
            tracks = playlistTracksService.getPlaylistTracks();

            //Call to update playlists
            updateTracks();
        });
    }

    private void updateTracks(){
        // Creates adapter, passing in user playlists
        mAdapter = new TracksAdapter(tracks);




        // Sets adapter on recycle view
        recyclerView.setAdapter(mAdapter);
    }

}
