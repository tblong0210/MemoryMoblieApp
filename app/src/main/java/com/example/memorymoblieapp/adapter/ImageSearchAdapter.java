package com.example.memorymoblieapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.memorymoblieapp.R;
import com.example.memorymoblieapp.local_data_storage.DataLocalManager;
import com.example.memorymoblieapp.local_data_storage.KeyData;
import com.example.memorymoblieapp.view.ViewImage;
import com.example.memorymoblieapp.view.ViewSearch;

import java.security.Key;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ImageSearchAdapter extends RecyclerView.Adapter<ImageSearchAdapter.ViewHolder> {

    private ArrayList<String> images;
    private Context context;

    public ImageSearchAdapter() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_search_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (!images.isEmpty()) {
            String nameImage = images.get(position).substring(images.get(position).lastIndexOf('/') + 1);
            holder.textView.setText(nameImage);
            holder.parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ViewImage.class);
                    intent.putExtra("path_image", images.get(position));
                    context.startActivity(intent);

                    Set<String> history = DataLocalManager.getSetList(KeyData.HISTORY_SEARCH.getKey());
                    if(history == null)
                        history = new HashSet<>();
                    history.add(images.get(position));
                    DataLocalManager.saveSetStringData(KeyData.HISTORY_SEARCH.getKey(), history);
                }
            });

            Glide.with(context)
                    .load(images.get(position))
                    .override(300, 300) // giảm kích thước ảnh xuống 300x300
                    .centerCrop() // cắt ảnh với kích thước mới
                    .into(holder.imageView);

        }
    }

    @Override
    public int getItemCount() {
        if (images == null)
            return 0;
        return images.size();
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView textView;
        private LinearLayout parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgSearch);
            textView = itemView.findViewById(R.id.nameImg);
            parent = itemView.findViewById(R.id.parentSearchImage);
        }
    }
}
