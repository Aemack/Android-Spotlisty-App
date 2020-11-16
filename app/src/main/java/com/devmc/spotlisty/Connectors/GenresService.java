package com.devmc.spotlisty.Connectors;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.devmc.spotlisty.Model.Genre;
import com.devmc.spotlisty.VolleyCallBack;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GenresService {
    private ArrayList<Genre> genreSeeds = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private RequestQueue queue;

    public GenresService(Context context){
        //Get spotify details from shared preferences
        sharedPreferences = context.getSharedPreferences("SPOTIFY",0);
        //Makes new volley request queue
        queue = Volley.newRequestQueue(context);
    }

    public ArrayList<Genre> getGenreSeeds() { return  genreSeeds; }

    public ArrayList<Genre> getGenreSeeds(final VolleyCallBack callBack){
        String endpoint = "https://api.spotify.com/v1/recommendations/available-genre-seeds";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, endpoint, null, response -> {
            Gson gson = new Gson();
            JSONArray jsonArray = response.optJSONArray("genres");
            String seedString = "";

            for (int iter = 0; iter < jsonArray.length(); iter++) {
                try {
                    seedString = jsonArray.get(iter).toString();
                    Genre genre = new Genre(seedString);
                    genreSeeds.add(genre);
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
            callBack.onSuccess();
        }, error -> {
            //TODO: Handle errror
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
        return genreSeeds;
    }

}
