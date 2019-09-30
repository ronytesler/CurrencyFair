package com.example.currencyfair.Activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.currencyfair.Adapters.CustomAdapter;
import com.example.currencyfair.Models.Photo;
import com.example.currencyfair.PaginationListener;
import com.example.currencyfair.R;
import com.example.currencyfair.Repositories.FlickrRepository;
import com.example.currencyfair.interfaces.OnNewPhotosListener;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements OnNewPhotosListener {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private GridLayoutManager layoutManager;
    private CustomAdapter customAdapter;
    private String query;
    private SearchView searchView;

    private boolean isLastPage = false;
    private boolean isLoading = false;
    int itemCount = 0;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        if (query != null && !query.isEmpty())
            searchView.setQuery(query, false);

        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SEARCH)) {
            query = intent.getStringExtra(SearchManager.QUERY);
            progressBar.setVisibility(View.VISIBLE);
            FlickrRepository.getInstance(getApplicationContext()).getPhotosAsync(query);
            final int itemCount = customAdapter.getItemCount();
            customAdapter.emptyData();
            customAdapter.notifyItemRangeRemoved(0, itemCount);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (searchView != null) {
            searchView.clearFocus();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerview);
        progressBar = findViewById(R.id.progress_bar);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        FlickrRepository.getInstance(getApplicationContext()).setOnNewPhotosListener(this);
        customAdapter = new CustomAdapter(this, new ArrayList<Photo>());
        recyclerView.setAdapter(customAdapter);
    }

    @Override
    public void onNewPhotos(final List<Photo> photos, int page, int pages, int perPage) {
        if (page == 1) {
            recyclerView.addOnScrollListener(new PaginationListener((GridLayoutManager) recyclerView.getLayoutManager(), perPage) {
                @Override
                protected void loadMoreItems() {
                    isLoading = true;
                    FlickrRepository.getInstance().getPhotosNextPageAsync();
                }

                @Override
                public boolean isLastPage() {
                    return isLastPage;
                }

                @Override
                public boolean isLoading() {
                    return isLoading;
                }
            });
        }
        itemCount += photos.size();
        customAdapter.addPhotos(photos);
        if (page == pages)
            isLastPage = true;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                customAdapter.notifyDataSetChanged();
                isLoading = false;
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onError(String error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), getString(R.string.error_search), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onNoPhotosFound(int page) {
        if (page == 1)
        {
            Toast.makeText(getApplicationContext(), getString(R.string.no_photos_found), Toast.LENGTH_SHORT).show();
        }
    }
}
