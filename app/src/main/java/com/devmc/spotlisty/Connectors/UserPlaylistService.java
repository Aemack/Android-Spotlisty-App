package com.devmc.spotlisty.Connectors;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.devmc.spotlisty.Model.Playlist;
import com.devmc.spotlisty.VolleyCallBack;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserPlaylistService {
    private ArrayList<Playlist> playlists = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private RequestQueue queue;

    public UserPlaylistService(Context context) {
        sharedPreferences = context.getSharedPreferences("SPOTIFY",0);
        queue = Volley.newRequestQueue(context);
    }

    public ArrayList<Playlist> getUserPlaylists(){
        return playlists;
    }

    public ArrayList<Playlist> getUserPlaylists(final VolleyCallBack callBack){
        String endpoint = "https://api.spotify.com/v1/me/playlists?limit=50";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, endpoint, null, response -> {
            Gson gson = new Gson();
            JSONArray jsonArray = response.optJSONArray("items");


            for (int n = 0; n < jsonArray.length(); n++) {
                try {
                     JSONObject object = jsonArray.getJSONObject(n);
                    Playlist playlist = gson.fromJson(object.toString(), Playlist.class);
                    JSONObject tracks  = object.getJSONObject("tracks");
                    int trackCount = tracks.getInt("total");
                    playlist.setTrackCount(trackCount);
                     try {
                         JSONArray imagesArray = object.getJSONArray("images");
                         JSONObject imagesObject = imagesArray.getJSONObject(0);
                         String imagesUrl = imagesObject.getString("url");
                         playlist.setImageUrl(imagesUrl);



                     } catch (JSONException e) {
                         e.printStackTrace();
                    }
                     playlists.add(playlist);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            callBack.onSuccess();
        }, error -> {
            //TODO: HANDLE ERROR
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError{
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token","");
                String auth = "Bearer "+token;
                headers.put("Authorization",auth);
                return headers;
            }
        };

        queue.add(jsonObjectRequest);
        return playlists;
    }

}
