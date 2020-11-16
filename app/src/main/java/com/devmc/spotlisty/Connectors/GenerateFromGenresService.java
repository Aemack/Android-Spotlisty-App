package com.devmc.spotlisty.Connectors;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.devmc.spotlisty.Model.Song;
import com.devmc.spotlisty.VolleyCallBack;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GenerateFromGenresService {
    private ArrayList<Song> playlist = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private RequestQueue queue;
    private String genreSeed;

    public GenerateFromGenresService(Context context){
        sharedPreferences = context.getSharedPreferences("SPOTIFY",0);
        queue = Volley.newRequestQueue(context);
    }

    public void setGenreSeed(String genreSeed){ this.genreSeed = genreSeed; }

    public String getGenreSeed(){ return genreSeed; }

    public ArrayList<Song> getTracks() { return playlist; }

    public ArrayList<Song> getTracks(final VolleyCallBack callBack){
        String endpoint = "https://api.spotify.com/v1/recommendations?limit=100&seed_genres="+genreSeed;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, endpoint, null, response -> {
            Gson gson = new Gson();
            JSONArray jsonArray = response.optJSONArray("tracks");

            for (int n = 0; n < jsonArray.length(); n++) {
                try {
                    JSONObject object = jsonArray.getJSONObject(n);
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
