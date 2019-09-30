package com.example.currencyfair.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PhotoSizes {

    @SerializedName("sizes")
    @Expose
    private Sizes sizesHolder;
    @SerializedName("stat")
    @Expose
    private String stat;

    public Sizes getSizesHolder() {
        return sizesHolder;
    }

    public void setSizesHolder(Sizes sizesHolder) {
        this.sizesHolder = sizesHolder;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

}