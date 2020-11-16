package com.devmc.spotlisty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devmc.spotlisty.Connectors.GenerateFromRecentService;
import com.devmc.spotlisty.Connectors.GenerateFromTracksService;
import com.devmc.spotlisty.Connectors.PlaylistTracksService;
import com.devmc.spotlisty.Model.GenerationOptions;
import com.devmc.spotlisty.Model.Song;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ListHolder;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.orhanobut.dialogplus.ViewHolder;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class GenerateFromRecentActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private GenerateFromRecentService generateFromRecentService;
    private Song song;
    private ArrayList<Song> tracks;
    private String trackIds;
    private String trackUris;
    private EditText editPlaylistName;
    private TextView noTracksTextView;


    //Generate Button
    private Button regenerateButton;
    private Button saveButton;

    // Options
    private Button optionsBtn;
    private DialogPlus dialog;
    private GenerationOptions oldGenerationOptions;
    private GenerationOptions newGenerationOptions;

    private boolean sizeChecked;
    private SeekBar sizeSeekBar;
    private TextView sizeTextView;
    private Switch sizeSwitch;

    private String playlistMode;
    private Switch majorMinorSwitch;
    private TextView modeTextView;
    private Switch modeSwitch;

    private SeekBar popularitySeekBar;
    private Switch popularitySwitch;
    private TextView popularityTextView;

    private Switch tempoSwitch;
    private SeekBar tempoSeekBar;
    private TextView tempoTextView;
    private boolean tempoChecked;

    private Switch valenceSwitch;
    private SeekBar valenceSeekBar;
    private TextView valenceTextView;

    private Switch keySwitch;
    private SeekBar keySeekbar;
    private TextView keyTextView;
    private String[]  keyArray = {"C","C#/D♭","D","D#/E♭","E","F","F#/G♭","G","G#/A♭","A","A#/B♭","B"};

    //Nav Buttons
    private Button userPlaylistsBtn;
    private Button genresBtn;
    private Button generateBtn;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generated_from_recent_activity);
        recyclerView = findViewById(R.id.generated_from_recent_recycler);


        newGenerationOptions = new GenerationOptions();
        oldGenerationOptions = new GenerationOptions();

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            oldGenerationOptions.setSize(extras.getInt("playlistSize"));
            newGenerationOptions.setSize(extras.getInt("playlistSize"));

            oldGenerationOptions.setMode(extras.getString("playlistMode"));
            newGenerationOptions.setMode(extras.getString("playlistMode"));

            oldGenerationOptions.setPopularity(extras.getInt("playlistPopularity"));
            newGenerationOptions.setPopularity(extras.getInt("playlistPopularity"));

            oldGenerationOptions.setTempo(extras.getInt("playlistTempo"));
            newGenerationOptions.setTempo(extras.getInt("playlistTempo"));


            oldGenerationOptions.setValence(extras.getInt("playlistValence"));
            newGenerationOptions.setValence(extras.getInt("playlistValence"));

            oldGenerationOptions.setKey(extras.getInt("playlistKey"));
            newGenerationOptions.setKey(extras.getInt("playlistKey"));
        }


        //Options drop down
        dialog = DialogPlus.newDialog(this)
                .setFooter(R.layout.options_footer)
                .setGravity(Gravity.TOP)
                .setContentHolder(new ViewHolder(R.layout.generate_options))
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                    }
                })
                .setExpanded(false)  // This will enable the expand feature, (similar to android L share dialog)
                .setOnDismissListener((res)->{
                    Intent i = new Intent(getApplicationContext(), GenerateFromRecentActivity.class);
                    if (newGenerationOptions.getSize() > 0){
                        i.putExtra("playlistSize",newGenerationOptions.getSize());
                    }
                    if (newGenerationOptions.getMode() != null && newGenerationOptions.getMode() !=""){
                        i.putExtra("playlistMode",newGenerationOptions.getMode());
                    }
                    if (newGenerationOptions.getPopularity() > 0){
                        i.putExtra("playlistPopularity",newGenerationOptions.getPopularity());
                    }
                    if (newGenerationOptions.getTempo() > 0){
                        i.putExtra("playlistTempo", newGenerationOptions.getTempo());
                    }
                    if (newGenerationOptions.getValence() > 0){
                        i.putExtra("playlistValence",newGenerationOptions.getValence());
                    }
                    if (newGenerationOptions.getValence() >= 0){
                        i.putExtra("playlistKey",newGenerationOptions.getKey());
                    }
                    startActivity(i);
                })
                .create();

        generateFromRecentService = new GenerateFromRecentService(getApplicationContext());
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        noTracksTextView = (TextView) findViewById(R.id.no_track_message);


        //OPTIONS
        optionsBtn = findViewById(R.id.options_button);
        optionsBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });





        sizeTextView = dialog.getHolderView().findViewById(R.id.playlist_size_text_view);
        sizeSeekBar = dialog.getHolderView().findViewById(R.id.playlist_size_seekbar);
        sizeSwitch = dialog.getHolderView().findViewById(R.id.size_switch);

        popularitySwitch = dialog.getHolderView().findViewById(R.id.popularity_switch);
        popularitySeekBar = dialog.getHolderView().findViewById(R.id.playlist_popularity_seekbar);
        popularityTextView = dialog.getHolderView().findViewById(R.id.playlist_popularity_text_view);

        tempoTextView = dialog.getHolderView().findViewById(R.id.tempo_text_view);
        tempoSwitch = dialog.getHolderView().findViewById(R.id.tempo_switch);
        tempoSeekBar = dialog.getHolderView().findViewById(R.id.playlist_tempo_seekbar);

        modeSwitch = dialog.getHolderView().findViewById(R.id.mode_switch);
        modeTextView = dialog.getHolderView().findViewById(R.id.playlist_mode_text_view);
        majorMinorSwitch = dialog.getHolderView().findViewById(R.id.major_minor_switch);

        valenceSwitch = dialog.getHolderView().findViewById(R.id.valence_switch);
        valenceSeekBar = dialog.getHolderView().findViewById(R.id.valence_seekbar);
        valenceTextView = dialog.getHolderView().findViewById(R.id.valence_text_view);

        keySwitch = dialog.getHolderView().findViewById(R.id.key_switch);
        keySeekbar = dialog.getHolderView().findViewById(R.id.key_seekbar);
        keyTextView = dialog.getHolderView().findViewById(R.id.key_text_view);

        //Turn Key on/off
        keySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked){
                    keySeekbar.setProgress(0);
                    keySeekbar.setEnabled(false);
                    keyTextView.setText("All");
                    newGenerationOptions.setKey(-1);
                } else {
                    newGenerationOptions.setKey(0);
                    keySeekbar.setEnabled(true);
                    keySeekbar.setProgress(0);
                    keyTextView.setText("C");
                }
            }
        });

        keySeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                keyTextView.setText(keyArray[progress]);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                keyTextView.setText(keyArray[seekBar.getProgress()]);
                newGenerationOptions.setKey(seekBar.getProgress());
            }
        });

        //Turn Valence on/off
        valenceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked){
                    valenceSeekBar.setProgress(0);
                    valenceSeekBar.setEnabled(false);
                    valenceTextView.setText("All");
                    newGenerationOptions.setValence(0);
                } else {
                    newGenerationOptions.setValence(50);
                    valenceSeekBar.setEnabled(true);
                    valenceSeekBar.setProgress(50);
                    valenceTextView.setText("50%");
                }
            }
        });


        valenceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                valenceTextView.setText((progress)+"%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                valenceTextView.setText(seekBar.getProgress()+"%");
                newGenerationOptions.setValence(seekBar.getProgress());
            }
        });
        //Turn Tempo on/off
        tempoSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked){
                    tempoSeekBar.setProgress(0);
                    tempoSeekBar.setEnabled(false);
                    tempoTextView.setText("All");
                    newGenerationOptions.setTempo(0);
                } else {
                    newGenerationOptions.setTempo(newGenerationOptions.getTempo());
                    tempoSeekBar.setEnabled(true);
                    tempoSeekBar.setProgress(newGenerationOptions.getTempo());
                    tempoTextView.setText(newGenerationOptions.getTempo()+" (BPM)");
                }
            }
        });

        tempoSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tempoTextView.setText(progress+" (BPM)");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tempoTextView.setText(seekBar.getProgress()+" (BPM)");
                newGenerationOptions.setTempo(seekBar.getProgress());
            }
        });

        //Turn Popularity on/off
        popularitySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked){
                    popularitySeekBar.setProgress(0);
                    popularitySeekBar.setEnabled(false);
                    popularityTextView.setText("All");
                    newGenerationOptions.setPopularity(0);
                } else {
                    popularitySeekBar.setProgress(newGenerationOptions.getPopularity());
                    popularityTextView.setText(newGenerationOptions.getPopularity()+"%");
                    newGenerationOptions.setPopularity(newGenerationOptions.getPopularity());
                    popularitySeekBar.setEnabled(true);
                }
            }
        });

        popularitySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                popularityTextView.setText(""+seekBar.getProgress()+"%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {    }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                newGenerationOptions.setPopularity(seekBar.getProgress());
                popularityTextView.setText(""+seekBar.getProgress()+"%");
            }
        });


        //Turn Mode on/off
        modeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //modeChecked = isChecked;
                if (isChecked){
                    majorMinorSwitch.setEnabled(true);
                    majorMinorSwitch.setChecked(false);
                    modeTextView.setText("Minor");
                    newGenerationOptions.setMode("minor");
                } else {
                    resetMode();
                }
            }
        });

        // Change Major to Minor in mode
        majorMinorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    modeTextView.setText("Major");
                    newGenerationOptions.setMode("major");
                } else {
                    modeTextView.setText("Minor");
                    newGenerationOptions.setMode("minor");
                }
            }
        });

        // Turn size on/off
        sizeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked){
                    sizeSeekBar.setProgress(20);
                    sizeTextView.setText("20 Tracks");
                    newGenerationOptions.setSize(0);
                }
                sizeChecked = isChecked;
                sizeSeekBar.setEnabled(sizeChecked);
            }
        });


        sizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sizeTextView.setText(""+seekBar.getProgress()+" Tracks");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {    }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                newGenerationOptions.setSize(seekBar.getProgress());
                sizeTextView.setText(""+seekBar.getProgress()+" Tracks");
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


        //NAV BUTTONS
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


        //Call to get tracks
        getTracks();

        setOptions();

    }




    //GENERATION OPTION FUNCTIONS
    public void resetKey(){
        keySeekbar.setProgress(0);
        keySwitch.setChecked(false);
        keySeekbar.setEnabled(false);
        keyTextView.setText("All");
        newGenerationOptions.setKey(-1);
    }

    public void resetValence(){
        valenceSeekBar.setProgress(0);
        valenceSwitch.setChecked(false);
        valenceSeekBar.setEnabled(false);
        valenceTextView.setText("All");
        newGenerationOptions.setValence(0);
    }

    public void resetTempo(){
        tempoSeekBar.setProgress(0);
        tempoSwitch.setChecked(false);
        tempoSeekBar.setEnabled(false);
        tempoTextView.setText("All");
        newGenerationOptions.setTempo(0);
    }

    public void resetPopularity(){
        popularitySeekBar.setProgress(0);
        popularitySwitch.setChecked(false);
        popularitySeekBar.setEnabled(false);
        popularityTextView.setText("All");
        newGenerationOptions.setPopularity(0);
    }

    public void resetSize(){
        sizeSeekBar.setProgress(20);
        sizeSwitch.setChecked(false);
        sizeSeekBar.setEnabled(false);
        sizeTextView.setText("20 Tracks");
        newGenerationOptions.setSize(0);
    }

    public void resetMode(){
        modeSwitch.setChecked(false);
        majorMinorSwitch.setChecked(false);
        majorMinorSwitch.setEnabled(false);
        newGenerationOptions.setMode(null);
        modeTextView.setText("All");
    }
    public void setOptions(){
        // If there are options already set
        if (oldGenerationOptions != null) {

            // If the size is set
            if (oldGenerationOptions.getSize() > 0) {
                sizeSwitch.setChecked(true);
                sizeTextView.setText(oldGenerationOptions.getSize() + " Tracks");
                sizeSeekBar.setProgress(oldGenerationOptions.getSize());
                newGenerationOptions.setSize(oldGenerationOptions.getSize());
                sizeSeekBar.setEnabled(true);
            } else {
                //If size is not set
                resetSize();
            }

            // If the popularity is set
            if (oldGenerationOptions.getPopularity() > 0) {
                newGenerationOptions.setPopularity(oldGenerationOptions.getPopularity());
                popularityTextView.setText(oldGenerationOptions.getPopularity() + "%");
                popularitySeekBar.setEnabled(true);
                popularitySeekBar.setProgress(oldGenerationOptions.getPopularity());
                popularitySwitch.setChecked(true);
            } else {
                //If popularity is not set
                resetPopularity();
            }


            if (oldGenerationOptions.getMode() == null){
                //If no mode is set
                resetMode();
            } else {
                // If Mode is set
                modeSwitch.setChecked(true);
                playlistMode = oldGenerationOptions.getMode();
                if (playlistMode.equals("major")){
                    modeTextView.setText("Major");
                    majorMinorSwitch.setChecked(true);
                    newGenerationOptions.setMode("major");
                } else if (playlistMode.equals("minor")){
                    modeTextView.setText("Minor");
                    majorMinorSwitch.setChecked(false);
                    newGenerationOptions.setMode("minor");
                }
            }

            //If Tempo is set
            if (oldGenerationOptions.getTempo() > 0){
                tempoSeekBar.setEnabled(true);
                tempoSeekBar.setProgress(oldGenerationOptions.getTempo());
                tempoSwitch.setChecked(true);
                newGenerationOptions.setTempo(oldGenerationOptions.getTempo());
                tempoTextView.setText(oldGenerationOptions.getTempo()+" (BPM)");
            } else {
                resetTempo();
            }

            if (oldGenerationOptions.getValence() > 0){
                valenceSeekBar.setEnabled(true);
                valenceSwitch.setChecked(true);
                valenceSeekBar.setProgress(oldGenerationOptions.getValence());
                newGenerationOptions.setValence(oldGenerationOptions.getTempo());
                valenceTextView.setText(oldGenerationOptions.getValence()+"%");
            } else {
                resetValence();
            }

            if (oldGenerationOptions.getKey() >= 0){
                keySeekbar.setEnabled(true);
                keySwitch.setChecked(true);
                newGenerationOptions.setKey(oldGenerationOptions.getKey());
                keySeekbar.setProgress(oldGenerationOptions.getKey());
                keyTextView.setText(keyArray[oldGenerationOptions.getKey()]);
            } else {
                resetKey();
            }
        } else {
            // If no options have been selected
            resetKey();
            resetTempo();
            resetSize();
            resetMode();
            resetPopularity();
        }

    }




    private String getAllTrackUris(){
        String uriString = "";
        int iter = 0;
        for (Song track : tracks) {
            if (iter == 0){
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
        if (oldGenerationOptions != null){
            generateFromRecentService.setGenerationOptions(oldGenerationOptions);
        }
        generateFromRecentService.setRecentlyPlayed(() -> {
            tracks = generateFromRecentService.generateTracks(()->{
                //Call to update playlists
                updateTracks();
            });
        });
    }

    private void updateTracks(){
        // Creates adapter, passing in user playlists

        mAdapter = new TracksAdapter(tracks);
        // Sets adapter on recycle view
        if (mAdapter.getItemCount() == 0){
            recyclerView.setVisibility(View.INVISIBLE);
            noTracksTextView.setVisibility(View.VISIBLE);
        } else {
            noTracksTextView.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(mAdapter);
        }
    }


}

