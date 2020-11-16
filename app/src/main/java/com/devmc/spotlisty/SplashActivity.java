package com.devmc.spotlisty;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.devmc.spotlisty.Connectors.UserService;
import com.devmc.spotlisty.Model.User;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

public class SplashActivity extends AppCompatActivity {

    private SharedPreferences.Editor editor;
    private SharedPreferences msharedPreferences;

    private RequestQueue queue;

    private static final String CLIENT_ID = "727a7bbe443c4121aa847c3199abaaad";
    private static final String REDIRECT_URI = "com.devmc.spotlisty://callback";
    private static final int REQUEST_CODE = 1337;
    private static final String SCOPES = "user-read-recently-played user-read-playback-position user-top-read playlist-modify-public user-library-modify playlist-modify-private user-read-private playlist-read-collaborative user-read-email playlist-read-private";


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);

        authenticateSpotify();

        msharedPreferences = this.getSharedPreferences("SPOTIFY",0);
        queue = Volley.newRequestQueue(this);
    }

    private void authenticateSpotify(){

        //Open authentication request with ClientID, redirect URI and response type we want
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);

        //Set requested scopes
        builder.setScopes(new String[]{SCOPES});

        //Send the request, this will open spotify (if installed, failing that WebView)
        AuthenticationRequest request = builder.build();
        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE){
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);

            switch (response.getType()){
                // response was successful and contains auth token
                case TOKEN:
                    editor = getSharedPreferences("SPOTIFY",0).edit();
                    editor.putString("token", response.getAccessToken());
                    Log.d("Starting", "GOT AUTH TOKEN");
                    editor.apply();
                    waitForUserInfo();
                    break;

                case ERROR:
                    // Handle error response
                    break;

                default:
                    // Handle other cases
            }

        }
    }

    private void waitForUserInfo(){
        UserService userService = new UserService(queue, msharedPreferences);
        userService.get(() ->{
            User user = userService.getUser();
            editor = getSharedPreferences("SPOTIFY", 0).edit();
            editor.putString("userid",user.id);
            editor.putString("display_name", user.display_name);
            Log.d("STARTING", "GOT USER INFORMATION");

            // We use commit instead of apply because we don't need the infomration stored immediatley
            editor.commit();
            startMainActivity();
        });
    }

    private void startMainActivity(){
        Intent newIntent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(newIntent);
    }

}
