package com.devmc.spotlisty.Model;

public class Song {

    private String id;
    private String name;
    private String album;
    private String artist;
    private String imageUrl;

    public Song(String id, String name, String album, String artist, String imageUrl) {
        this.name = name;
        this.id = id;
        this.album = album;
        this.artist = artist;
        this.imageUrl = imageUrl;
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

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album){
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist){
        this.artist = artist;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }





}
