package com.example.currencyfair.Interceptors;

import com.example.currencyfair.App;
import com.example.currencyfair.Utils.Utils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class OfflineInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        if (!Utils.hasNetwork(App.getAppContext())) {

            int maxStale = 60 * 60 * 24 * 28; // 4-weeks stale
            request = request.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build();
        }

        return chain.proceed(request);
    }
}