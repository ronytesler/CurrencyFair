package com.example.currencyfair.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.currencyfair.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


public class LargeView extends AppCompatActivity {

    public static final String PHOTO_URL = "photoUrl";
    private ProgressBar progressBar;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_large_view);

        String photoUrl = getIntent().getStringExtra(PHOTO_URL);
        if (photoUrl == null || photoUrl.isEmpty()) {
            finish();
            return;
        }

        progressBar = findViewById(R.id.progress_bar);
        imageView = findViewById(R.id.image_large);
        Picasso.with(getApplicationContext()).load(photoUrl)
                .placeholder(R.mipmap.ic_icon).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                Toast.makeText(getApplicationContext(), getString(R.string.error_loading_large_photo), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                imageView.setVisibility(View.GONE);
            }
        });
    }
}
