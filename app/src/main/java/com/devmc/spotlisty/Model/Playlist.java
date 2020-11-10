package com.devmc.spotlisty.Model;

import com.devmc.spotlisty.Model.Song;

import java.util.ArrayList;

public class Playlist {

    private String id;
    private String name;
    private String imageUrl;
    private int trackCount;

    public Playlist(String id, String name, String imageUrl, int trackCount){
        this.name = name;
        this.id = id;
        this.imageUrl = imageUrl;
        this.trackCount = trackCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setImageUrl(String url) {this.imageUrl = url;}

    public String getImageUrl(){ return imageUrl; }

    public void setTrackCount(int trackCount) {
          this.trackCount = trackCount;
    }

    public int getTrackCount(){
        return trackCount;
    }
}
