package com.example.memorymoblieapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memorymoblieapp.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ImageSearchAdapter extends RecyclerView.Adapter<ImageSearchAdapter.ViewHolder> {

    static final int MAX_CACHE_SIZE = 16;
    private List<String> images;
    private File[] fileImages;
    static HashMap<String, Drawable> drawableCache = new HashMap<String, Drawable>();

    public ImageSearchAdapter() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
           // Bitmap bitmap = BitmapFactory.decodeFile(images.get(position));
            String nameImage = images.get(position).substring(images.get(position).lastIndexOf('/') + 1);
            holder.textView.setText(nameImage);
            holder.imageView.setImageDrawable(getDrawable(images.get(position)));
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    private Drawable getDrawable(String key) {

        //Clear data when the memory is too large
        if (drawableCache.size() >= MAX_CACHE_SIZE) {
            drawableCache.clear();
        }

        //If there isn't the drawable exists => store it
        if (!drawableCache.containsKey(key)) {
            drawableCache.put(key, Drawable.createFromPath(key));
        }

        return drawableCache.get(key);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgSearch);
            textView = itemView.findViewById(R.id.nameImg);
        }
    }
}
