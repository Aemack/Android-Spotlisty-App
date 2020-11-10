package com.devmc.spotlisty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.devmc.spotlisty.Connectors.SongService;
import com.devmc.spotlisty.Model.Song;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView userView;
    private TextView songView;
    private Button userPlaylistsBtn;
    private Song song;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userView = (TextView) findViewById(R.id.user);

        userPlaylistsBtn = (Button) findViewById(R.id.your_playlist_button);
        userPlaylistsBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, PlaylistsActivity.class));
            }
        });

        SharedPreferences sharedPreferences = this.getSharedPreferences("SPOTIFY",0);
        userView.setText(sharedPreferences.getString("userid","No User"));


    }
}