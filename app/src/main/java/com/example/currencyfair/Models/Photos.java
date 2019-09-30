package com.example.currencyfair.Models;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.currencyfair.Repositories.FlickrRepository;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Photos {

    @SerializedName("page")
    @Expose
    private int page;
    @SerializedName("pages")
    @Expose
    private int pages;
    @SerializedName("perpage")
    @Expose
    private int perpage;
    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("photo")
    @Expose
    private List<Photo> allPhotos = null;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getPerpage() {
        return perpage;
    }

    public void setPerpage(int perpage) {
        this.perpage = perpage;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Photo> getAllPhotos() {
        return allPhotos;
    }

    public void setAllPhotos(List<Photo> allPhotos) {
        this.allPhotos = allPhotos;
    }


    public void initPhotos() {
        if (allPhotos == null)
            return;
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (final Photo photo : allPhotos) {
            executorService.execute(new Runnable(){
                @Override
                public void run(){
                    PhotoSizes photoSizes = FlickrRepository.getInstance().getPhotoSizes(photo.getId());
                    if (photoSizes == null || photoSizes.getSizesHolder() == null || photoSizes.getSizesHolder().getSizesList() == null)
                        return;
                    for (Size size : photoSizes.getSizesHolder().getSizesList())
                    {
                        if (size.getLabel().equals("Large Square"))
                        {
                            photo.setUrlSmall(size.getSource());
                            // if the other param was already set, we can break the loop
                            if (photo.getUrlLarge() != null)
                                break;
                        }
                        if (size.getLabel().equals("Large"))
                        {
                            photo.setUrlLarge(size.getSource());
                            // if the other param was already set, we can break the loop
                            if (photo.getUrlSmall() != null)
                                break;
                        }

                    }
                }
            });
        }
    }
}