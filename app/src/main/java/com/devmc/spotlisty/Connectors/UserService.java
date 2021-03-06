package com.devmc.spotlisty.Connectors;

import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.devmc.spotlisty.Model.User;
import com.devmc.spotlisty.VolleyCallBack;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class UserService {

    private static final String ENDPOINT = "https://api.spotify.com/v1/me";
    private SharedPreferences msharedPreferences;
    private RequestQueue mqueue;
    private User user;

    public UserService(RequestQueue queue, SharedPreferences sharedPreferences) {
        mqueue = queue;
        msharedPreferences = sharedPreferences;
    }

    public User getUser(){
        return user;
    }

    public void get(final VolleyCallBack callBack) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(ENDPOINT, null, response -> {
            Gson gson = new Gson();
            user = gson.fromJson(response.toString(), User.class);
            String displayName = "";
            try{
                displayName = response.getString("display_name");
            } catch (JSONException e){
                e.printStackTrace();
            }
            user.setDisplay_name(displayName);
            callBack.onSuccess();
        }, error -> get(() -> {

        })) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = msharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                return headers;
            }
        };
        mqueue.add(jsonObjectRequest);
    }
}
