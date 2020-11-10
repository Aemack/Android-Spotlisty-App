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

import com.devmc.spotlisty.Connectors.GenerateFromRecentService;
import com.devmc.spotlisty.Connectors.GenerateFromTracksService;
import com.devmc.spotlisty.Connectors.PlaylistTracksService;
import com.devmc.spotlisty.Model.Song;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class GenerateFromRecentActivity extends Activity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private GenerateFromRecentService generateFromRecentService;
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
    private Button genresBtn;
    private Button generateBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generated_from_recent_activity);
        recyclerView = findViewById(R.id.generated_from_recent_recycler);


        generateFromRecentService = new GenerateFromRecentService(getApplicationContext());
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Call to get tracks
        getTracks();


        regenerateButton = findViewById(R.id.regenerate_playlist_from_recent_button);
        regenerateButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                trackIds = getTrackIds();
                Intent i = new Intent(v.getContext(), GenerateFromRecentActivity.class);

                startActivity(i);
            }
        });

        editPlaylistName = findViewById(R.id.editRecentPlaylistTitle);
        saveButton = findViewById(R.id.save_playlist_from_recent_button);
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
        generateBtn = (Button) findViewById(R.id.generate_button);
        generateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GenerateFromRecentActivity.this, GenerateFromRecentActivity.class));
            }
        });

        genresBtn = (Button) findViewById(R.id.genres_button);
        genresBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GenerateFromRecentActivity.this, GenresActivity.class));
            }
        });

        userPlaylistsBtn = (Button) findViewById(R.id.your_playlist_button);
        userPlaylistsBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(GenerateFromRecentActivity.this, PlaylistsActivity.class));
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
        generateFromRecentService.setRecentlyPlayed(() -> {
            tracks = generateFromRecentService.generateTracks(()->{
                Log.i("TRACKS>>>>>>>",""+tracks);
                //Call to update playlists
                updateTracks();
            });
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

