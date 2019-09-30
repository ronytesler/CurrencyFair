package com.example.currencyfair.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.example.currencyfair.R;
import com.squareup.picasso.Picasso;


public class LargeView extends AppCompatActivity {

    public static final String PHOTO_URL = "photoUrl";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_large_view);

        String photoUrl = getIntent().getStringExtra(PHOTO_URL);
        if (photoUrl == null || photoUrl.isEmpty()) {
            finish();
            return;
        }
        ImageView imageView = findViewById(R.id.image_large);
        Picasso.with(getApplicationContext()).load(photoUrl)
                .placeholder(R.mipmap.ic_icon).into(imageView);
    }
}
