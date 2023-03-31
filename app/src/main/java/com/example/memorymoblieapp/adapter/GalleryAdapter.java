package com.example.memorymoblieapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.memorymoblieapp.R;
import com.example.memorymoblieapp.view.ViewImage;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private Context context;
    private List<String> images;
//    protected PhotoListener photoListener;

    public GalleryAdapter(Context context, List<String> images)
    {
        this.context = context;
        this.images = images;
//        , PhotoListener photoListener
//        this.photoListener = photoListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
//        return new ViewHolder(
//                LayoutInflater.from(context).inflate(R.layout.gallery_item,parent,false)
//        );

        View v =LayoutInflater.from(context).inflate(R.layout.gallery_item, parent, false);


        return new ViewHolder(v);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {

        String image = images.get(position);
        Glide.with(context).load(image)
                .placeholder(R.drawable.image1)
                .error(R.drawable.image3).into(holder.image);


        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ViewImage.class);
                intent.putExtra("path_image", images.get(position));
                context.startActivity(intent);
            }
        });


//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                photoListener.onPhotoClick(image);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image ;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            image = itemView.findViewById(R.id.imageGallery);
        }
    }

//    public interface  PhotoListener{
//        void onPhotoClick(String path);
//    }
}
