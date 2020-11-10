package com.devmc.spotlisty.Connectors;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.devmc.spotlisty.Model.Song;
import com.devmc.spotlisty.Model.User;
import com.devmc.spotlisty.VolleyCallBack;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SavePlaylistService {

    private SharedPreferences sharedPreferences;
    private RequestQueue queue;
    private String trackUris;
    private String playlistName;
    private String playlistId;

    public SavePlaylistService(Context context) {
        sharedPreferences = context.getSharedPreferences("SPOTIFY",0);
        queue = Volley.newRequestQueue(context);
    }

    public void setTrackUris(String trackUris){
        this.trackUris = trackUris;
    }

    public void setPlaylistName(String playlistName){
        this.playlistName = playlistName;
    }

    public void savePlaylist(final VolleyCallBack callBack) {
        createPlaylist(callBack);
    }

    public void createPlaylist(final VolleyCallBack callBack){
        Map<String, String> params = new HashMap();
        params.put("name", playlistName);
        JSONObject parameters = new JSONObject(params);
        String endpoint = "https://api.spotify.com/v1/users/"+ sharedPreferences.getString("userid","NO USER")+"/playlists";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, endpoint, parameters, response -> {
            Gson gson = new Gson();
            playlistId = response.optString("id");
            addTracksToPlaylist(callBack);
        } , error -> {
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


    }



    public void addTracksToPlaylist(final VolleyCallBack callBack){
        String endpoint = "https://api.spotify.com/v1/playlists/"+playlistId+"/tracks?uris="+trackUris;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, endpoint, null, response -> {
            callBack.onSuccess();
        } , error -> {
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

    }

}
