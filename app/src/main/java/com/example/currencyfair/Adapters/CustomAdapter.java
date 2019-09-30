package com.example.currencyfair.Adapters;


import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.currencyfair.Activities.LargeView;
import com.example.currencyfair.Models.Photo;
import com.example.currencyfair.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private ArrayList<Photo> photos;
    private Context context;

    public CustomAdapter(Context context, ArrayList<Photo> photos) {
        this.context = context;
        this.photos = photos;

    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void addPhotos(List<Photo> newPhotos) {
        if (newPhotos == null)
            return;
        photos.addAll(newPhotos);
    }

    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
//        Picasso.with(context).load(photos.get(i).getUrlSmall()).resize(120, 60).into(viewHolder.imageView);
        Photo photo = photos.get(position);
        if (photo.getUrlSmall() == null)
        {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    notifyItemChanged(position);
                }
            });
            return;
        }
        //Picasso.with(context).setLoggingEnabled(true);
        Picasso.with(context).load(photo.getUrlSmall())
                .fit()
                .error(R.mipmap.ic_icon)
                .placeholder(R.mipmap.ic_icon)
                .into(viewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public void emptyData() {
        photos.clear();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.image);
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String largePhotoUrl = photos.get(getLayoutPosition()).getUrlLarge();
            if (largePhotoUrl == null) {
                photos.get(getAdapterPosition());
                Toast.makeText(context, context.getString(R.string.no_large_view_for_photo), Toast.LENGTH_SHORT).show();
                return;
            }
            Intent largePhotoActivityIntent = new Intent(context, LargeView.class);
            largePhotoActivityIntent.putExtra(LargeView.PHOTO_URL, largePhotoUrl);
            context.startActivity(largePhotoActivityIntent);

        }
    }
}