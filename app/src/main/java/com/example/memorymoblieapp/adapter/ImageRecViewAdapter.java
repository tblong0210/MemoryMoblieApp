package com.example.memorymoblieapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideContext;
import com.example.memorymoblieapp.Image;
import com.example.memorymoblieapp.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ImageRecViewAdapter extends RecyclerView.Adapter<ImageRecViewAdapter.ViewHolder> {
    private ArrayList<Image> images = new ArrayList<>();
    private Context context;
    public ImageRecViewAdapter(Context context) {
        this.context= context;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_list_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imgName.setText(images.get(position).getName());
        String name = images.get(position).getName();
        holder.imgItemViewEdit.setImageResource(images.get(position).getImageResource());
        holder.parent_list_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, name, Toast.LENGTH_SHORT).show();
            }
        });
//
//        Glide.with(context)
//                .asBitmap()
//                .load(images.get(position).getImageUrl())
//                .into(holder.imgItemViewEdit);

//        Glide.with(context)
//                .asBitmap()
//                .load(R.drawable.image1)
//                .into(holder.imgItemViewEdit);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public void setImages(ArrayList<Image> images) {
        this.images = images;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView imgName;
        private ImageView imgItemViewEdit;
        private CardView parent_list_image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgName= itemView.findViewById(R.id.imgName);
            parent_list_image=itemView.findViewById(R.id.parent_list_image);
            imgItemViewEdit = itemView.findViewById(R.id.imgItemViewEdit);
        }
    }
}
