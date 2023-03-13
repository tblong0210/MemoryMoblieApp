package com.example.memorymoblieapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memorymoblieapp.R;
import com.example.memorymoblieapp.obj.Image;

import java.util.ArrayList;

public class ImageDetailAdapter extends RecyclerView.Adapter<ImageDetailAdapter.ViewHolder> {
    ArrayList<Image> images;
    Context context;

    public ImageDetailAdapter(ArrayList<Image> images, Context context) {
        this.images = images;
        this.context = context;
    }

    @NonNull
    @Override
    public ImageDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.image_detail_item, parent, false);
        return new ViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ImageDetailAdapter.ViewHolder holder, int position) {
        holder.name.setText("Name: " + images.get(position).getName());
        holder.size.setText("Size: " + images.get(position).getSize());
        holder.dimensions.setText("Dimensions: " + images.get(position).getDimensions());
        holder.location.setText("Location: " + images.get(position).getLocation());
        holder.img.setImageResource(Integer.parseInt(Integer.toString(images.get(position).getImg())));
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView size;
        TextView dimensions;
        TextView location;
        ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.txtImageName);
            size = (TextView) itemView.findViewById(R.id.txtSize);
            dimensions = (TextView) itemView.findViewById(R.id.txtDimensions);
            location = (TextView) itemView.findViewById(R.id.txtLocation);
            img = (ImageView) itemView.findViewById(R.id.ivAlbum);
        }
    }
}
