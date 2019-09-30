package com.example.currencyfair.interfaces;


import com.example.currencyfair.Models.Photo;

import java.util.List;

public interface OnNewPhotosListener {
    void onNewPhotos(List<Photo> photos, int page, int pages, int perPage);
    void onError(String error);
    void onNoPhotosFound(int page);
}
