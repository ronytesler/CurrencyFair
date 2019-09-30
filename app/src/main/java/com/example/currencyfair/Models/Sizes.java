package com.example.currencyfair.Models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sizes {

    @SerializedName("canblog")
    @Expose
    private Integer canblog;
    @SerializedName("canprint")
    @Expose
    private Integer canprint;
    @SerializedName("candownload")
    @Expose
    private Integer candownload;
    @SerializedName("size")
    @Expose
    private List<Size> sizesList = null;

    public Integer getCanblog() {
        return canblog;
    }

    public void setCanblog(Integer canblog) {
        this.canblog = canblog;
    }

    public Integer getCanprint() {
        return canprint;
    }

    public void setCanprint(Integer canprint) {
        this.canprint = canprint;
    }

    public Integer getCandownload() {
        return candownload;
    }

    public void setCandownload(Integer candownload) {
        this.candownload = candownload;
    }

    public List<Size> getSizesList() {
        return sizesList;
    }

    public void setSizesList(List<Size> sizesList) {
        this.sizesList = sizesList;
    }

}