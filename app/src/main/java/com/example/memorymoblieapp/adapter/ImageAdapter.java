package com.example.memorymoblieapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memorymoblieapp.R;
import com.example.memorymoblieapp.obj.Image;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    static ArrayList<Image> images;
    Context context;
    static boolean detailed;

    public ImageAdapter(ArrayList<Image> images, Context context, boolean detailed) {
        ImageAdapter.images = images;
        this.context = context;
        ImageAdapter.detailed = detailed;
    }

    @NonNull
    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        if (detailed)
            itemView = layoutInflater.inflate(R.layout.image_detail_item, parent, false);
        else
            itemView = layoutInflater.inflate(R.layout.image_item, parent, false);

        return new ViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.ViewHolder holder, int position) {
        if (detailed) {
            holder.name.setText("Tên: " + images.get(position).getName());
            holder.createdDate.setText("Ngày tạo: " + images.get(position).getCreatedDate());
            holder.size.setText("Dung lượng: " + images.get(position).getSize());
            holder.dimensions.setText("Kích thước: " + images.get(position).getDimensions());
            holder.location.setText("Địa điểm: " + images.get(position).getLocation());
        }
        holder.img.setImageResource(Integer.parseInt(Integer.toString(images.get(position).getImg())));
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView createdDate;
        TextView size;
        TextView dimensions;
        TextView location;
        ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            if (detailed) {
                name = (TextView) itemView.findViewById(R.id.txtImageName);
                createdDate = (TextView) itemView.findViewById(R.id.txtCreatedDate);
                size = (TextView) itemView.findViewById(R.id.txtSize);
                dimensions = (TextView) itemView.findViewById(R.id.txtDimensions);
                location = (TextView) itemView.findViewById(R.id.txtLocation);
            }
            img = (ImageView) itemView.findViewById(R.id.ivImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), images.get(getAdapterPosition()).getName(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}