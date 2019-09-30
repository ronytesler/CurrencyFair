package com.example.currencyfair.Models;

import com.example.currencyfair.Repositories.FlickrRepository;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PhotoList {

    @SerializedName("photos")
    @Expose
    private Photos photos;
    @SerializedName("stat")
    @Expose
    private String stat;

    public Photos getPhotos() {
        return photos;
    }

    public void setPhotos(Photos photos) {
        this.photos = photos;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public void initPhotos() {
        if (photos == null)
            return;
        photos.initPhotos();
    }
}