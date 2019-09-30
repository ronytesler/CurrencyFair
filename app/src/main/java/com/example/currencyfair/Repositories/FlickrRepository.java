package com.example.currencyfair.Repositories;

import android.content.Context;
import android.net.Uri;

import com.example.currencyfair.App;
import com.example.currencyfair.Interceptors.CacheInterceptor;
import com.example.currencyfair.Interceptors.OfflineInterceptor;
import com.example.currencyfair.Models.Photo;
import com.example.currencyfair.Models.PhotoList;
import com.example.currencyfair.Models.PhotoSizes;
import com.example.currencyfair.Models.Photos;
import com.example.currencyfair.interfaces.OnNewPhotosListener;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.CertificatePinner;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FlickrRepository {

    private static FlickrRepository instance;
    private static OkHttpClient client;
    private Context context;
    private int currentPage;
    private String tags;
    private OnNewPhotosListener newPhotosListener;

    private static final String API_KEY = "api_key";
    private static final String PHOTO_ID = "photo_id";
    private static final String FORMAT = "format";
    private static final String NO_JSON_CALLBACK = "nojsoncallback";
    private static final String TAGS = "tags";
    private static final String PAGE = "page";
    private String baseSearchUrl = "https://api.flickr.com/services/rest/?method=flickr.photos.search";
    private String baseSizesUrl = "https://api.flickr.com/services/rest/?method=flickr.photos.getSizes";

    private String apiKey = "e8d50e23925beee987d19c27116e8e57";

    public FlickrRepository(Context context) {
        this.context = context;
    }


    public synchronized static FlickrRepository getInstance(Context context) {
        if (instance == null) {
            init();
            instance = new FlickrRepository(context);
        }

        instance.context = context;
        return instance;
    }

    public synchronized static FlickrRepository getInstance() {
        if (instance == null) {
            instance = new FlickrRepository(App.getAppContext());
        }
        return instance;
    }

    public void setOnNewPhotosListener(OnNewPhotosListener onNewPhotosListener) {
        this.newPhotosListener = onNewPhotosListener;
    }

    private void notifyNewPhotosListener(List<Photo> images, int page, int pages, int perpage) {
        if (newPhotosListener != null)
            newPhotosListener.onNewPhotos(images, page, pages, perpage);
    }

    private void notifyNewPhotosListenerOnError(String error) {
        if (newPhotosListener != null)
            newPhotosListener.onError(error);
    }

    private static void init() {
        int cacheSize = 15 * 1024 * 1024; // 15 MiB
//        File cacheLocation = new File("/var/tmp/okhttp");
        File cacheDirectory = new File(App.getAppContext().getCacheDir(), "http-cache");
        Cache cache = new Cache(cacheDirectory, cacheSize);

        String hostname = "api.flickr.com";
        CertificatePinner certificatePinner = new CertificatePinner.Builder()
                .add(hostname, "sha256/jvVjba1CXlECoHTLs2rZ24MKFwtejtlh4BDhpbpR47U=")
                .add(hostname, "sha256/JSMzqOOrtyOT1kmau6zKhgT676hGgczD5VMdRMyJZFA=")
                .add(hostname, "sha256/++MBgDH5WGvL9Bcn5Be30cRcL0f5O+NyoXuWtQdX1aI=")
                .build();

        client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new CacheInterceptor())
                .addInterceptor(new OfflineInterceptor())
                .cache(cache)
                .callTimeout(10, TimeUnit.SECONDS)
                .certificatePinner(certificatePinner)
                .build();
    }

    public void getPhotosAsync(String tags) {
        getPhotosAsync(tags, 1);
    }

    public void getPhotosNextPageAsync() {
        getPhotosAsync(tags, currentPage + 1);
    }


    private void getPhotosAsync(String tags, int page) {
        this.tags = tags;
        currentPage = page;
        String searchUrl = buildSearchUrl(tags, currentPage);
        Request request = new Request.Builder().url(searchUrl).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                // not handling this for now
                notifyNewPhotosListenerOnError("");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    // not handling this for now
                    notifyNewPhotosListenerOnError("");
                    return;
                }
                Gson gson = new Gson();
                PhotoList photoList = gson.fromJson(response.body().string(), PhotoList.class);
                photoList.initPhotos();

                Photos photos = photoList.getPhotos();
                notifyNewPhotosListener(photos.getAllPhotos(), photos.getPage(), photos.getPages(), photos.getPerpage());
            }
        });
    }

    public PhotoSizes getPhotoSizes(String photoId) {
        String sizesUrl = buildSizesUrl(photoId);
        Request request = new Request.Builder().url(sizesUrl).build();
        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                // not handling this for now
                return null;
            }
            Gson gson = new Gson();
            PhotoSizes photoSizes = gson.fromJson(response.body().string(), PhotoSizes.class);
            return photoSizes;
        } catch (IOException e) {
            // not handling this for now
            return null;
        }
    }

    private String buildSizesUrl(String photoId) {
        Uri.Builder builder = Uri.parse(baseSizesUrl).buildUpon();
        builder.appendQueryParameter(API_KEY, apiKey)
                .appendQueryParameter(PHOTO_ID, photoId)
                .appendQueryParameter(FORMAT, "json")
                .appendQueryParameter(NO_JSON_CALLBACK, "1");
        return builder.build().toString();
    }

    private String buildSearchUrl(String tags, int page) {
        Uri.Builder builder = Uri.parse(baseSearchUrl).buildUpon();
        builder.appendQueryParameter(API_KEY, apiKey)
                .appendQueryParameter(TAGS, tags)
                .appendQueryParameter(PAGE, String.valueOf(page))
                .appendQueryParameter(FORMAT, "json")
                .appendQueryParameter(NO_JSON_CALLBACK, "1");
        return builder.build().toString();
    }
}
