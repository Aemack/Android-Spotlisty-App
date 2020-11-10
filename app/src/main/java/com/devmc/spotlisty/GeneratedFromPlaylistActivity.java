package com.devmc.spotlisty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devmc.spotlisty.Connectors.GenerateFromTracksService;
import com.devmc.spotlisty.Connectors.PlaylistTracksService;
import com.devmc.spotlisty.Model.Song;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class GeneratedFromPlaylistActivity extends Activity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private GenerateFromTracksService generateFromTracksService;
    private Song song;
    private ArrayList<Song> tracks;
    private String trackIds;
    private String trackUris;
    private EditText editPlaylistName;

    //Generate Button
    private Button regenerateButton;
    private Button saveButton;

    //Nav Buttons
    private Button userPlaylistsBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generated_from_playlist);
        recyclerView = findViewById(R.id.generated_from_playlist_recycler);

        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            trackIds = extras.getString("trackIds");
        }


        generateFromTracksService = new GenerateFromTracksService(getApplicationContext());
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Call to get tracks
        getTracks();


        regenerateButton = findViewById(R.id.regenerate_playlist_from_playlist_button);
        regenerateButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                trackIds = getTrackIds();
                Intent i = new Intent(v.getContext(), GeneratedFromPlaylistActivity.class);
                i.putExtra("trackIds",trackIds);

                startActivity(i);
            }
        });

        editPlaylistName = findViewById(R.id.editPlaylistTitle);
        saveButton = findViewById(R.id.save_playlist_from_playlist_button);
        saveButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String playlistName = editPlaylistName.getText().toString();
                trackUris = getAllTrackUris();
                Intent i = new Intent(v.getContext(), SavePlaylistActivity.class);
                i.putExtra("trackUris",trackUris);
                i.putExtra("playlistName",playlistName);

                startActivity(i);
            }
        });


        //Nav Buttons
        userPlaylistsBtn = (Button) findViewById(R.id.your_playlist_button);
        userPlaylistsBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(GeneratedFromPlaylistActivity.this, PlaylistsActivity.class));
            }
        });
    }


    private String getAllTrackUris(){
        String uriString = "";
        int iter = 0;
         for (Song track : tracks) {
             if (iter == 0){
                 //spotify:track:4iEOVEULZRvmzYSZY2ViKN
                 uriString = "spotify:track:"+track.getId();
             } else {
                 uriString =uriString+",spotify:track:"+track.getId();
             }
             iter ++;
         }

        return uriString;
    }

    //Get tracks from playlistTracksService
    private void getTracks(){

        generateFromTracksService.setTrackIds(trackIds);

        generateFromTracksService.getTracks(() -> {
            tracks = generateFromTracksService.getTracks();

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

}

