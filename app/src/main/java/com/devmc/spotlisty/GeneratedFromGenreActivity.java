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

import com.devmc.spotlisty.Connectors.GenerateFromGenresService;
import com.devmc.spotlisty.Connectors.GenerateFromTracksService;
import com.devmc.spotlisty.Model.Song;

import java.util.ArrayList;
import java.util.Random;

public class GeneratedFromGenreActivity extends Activity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private GenerateFromGenresService generateFromGenresService;
    private Song song;
    private ArrayList<Song> tracks;
    private String genreSeed;
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
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generated_from_genre_activity);
        recyclerView = findViewById(R.id.generated_from_genre_recycler);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            genreSeed = extras.getString("genreSeed");
        }

        generateFromGenresService = new GenerateFromGenresService(getApplicationContext());
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        getTracks();


        //Nav Buttons
        generateBtn = (Button) findViewById(R.id.generate_button);
        generateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GeneratedFromGenreActivity.this, GenerateFromRecentActivity.class));
            }
        });

        genresBtn = (Button) findViewById(R.id.genres_button);
        genresBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GeneratedFromGenreActivity.this, GenresActivity.class));
            }
        });

        userPlaylistsBtn = (Button) findViewById(R.id.your_playlist_button);
        userPlaylistsBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(GeneratedFromGenreActivity.this, PlaylistsActivity.class));
            }
        });


        regenerateButton = findViewById(R.id.regenerate_playlist_from_genre_button);
        regenerateButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(v.getContext(), GeneratedFromGenreActivity.class);
                i.putExtra("genreSeed",genreSeed);
                startActivity(i);
            }
        });




        editPlaylistName = findViewById(R.id.editGenrePlaylistTitle);
        saveButton = findViewById(R.id.save_playlist_from_genre_button);
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
        generateFromGenresService.setGenreSeed(genreSeed);

        generateFromGenresService.getTracks(() -> {
            tracks = generateFromGenresService.getTracks();

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
