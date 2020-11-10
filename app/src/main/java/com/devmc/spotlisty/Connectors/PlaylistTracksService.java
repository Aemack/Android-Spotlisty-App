package com.devmc.spotlisty.Connectors;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.devmc.spotlisty.Model.Playlist;
import com.devmc.spotlisty.Model.Song;
import com.devmc.spotlisty.VolleyCallBack;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.util.Log;


public class PlaylistTracksService {
    private ArrayList<Song> playlist = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private RequestQueue queue;
    private String playlistId;

    public PlaylistTracksService(Context context) {
        sharedPreferences = context.getSharedPreferences("SPOTIFY",0);
        queue = Volley.newRequestQueue(context);
    }

    public void setPlaylistId(String playlistId){
        this.playlistId = playlistId;
    }

    public ArrayList<Song> getPlaylistTracks(){
        return playlist;
    }

    public ArrayList<Song> getPlaylistTracks(final VolleyCallBack callBack){
        String endpoint = "https://api.spotify.com/v1/playlists/"+playlistId+"/tracks";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, endpoint, null, response -> {
            Gson gson = new Gson();
            JSONArray jsonArray = response.optJSONArray("items");


            for (int n = 0; n < jsonArray.length(); n++) {
                try {
                    JSONObject object = jsonArray.getJSONObject(n);
                    object = object.optJSONObject("track");
                    JSONObject albumObject = object.getJSONObject("album");

                    JSONArray artistsArray = albumObject.getJSONArray("artists");
                    JSONObject artistObject = artistsArray.getJSONObject(0);

                    JSONArray imagesArray = albumObject.getJSONArray("images");
                    JSONObject imageObject = imagesArray.getJSONObject(0);

                    String trackName = object.getString("name");
                    String trackId = object.getString("id");
                    String album = albumObject.getString("name");
                    String artist = artistObject.getString("name");
                    String imageUrl = imageObject.getString("url");

                    Song song = new Song(trackId,trackName,album, artist, imageUrl);
                    playlist.add(song);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            callBack.onSuccess();
        }, error -> {
            //TODO: HANDLE ERROR
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token","");
                String auth = "Bearer "+token;
                headers.put("Authorization",auth);
                return headers;
            }
        };

        queue.add(jsonObjectRequest);
        return playlist;
    }
}
