package com.example.memorymoblieapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memorymoblieapp.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    static ArrayList<String> images;
    Context context;
    static boolean detailed;

    public ImageAdapter(ArrayList<String> images, Context context, boolean detailed) {
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
        File currentFile = new File(images.get(position));

        if (currentFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(currentFile.getAbsolutePath());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ROOT);

            long fileSizeNumber = Math.round(currentFile.length() * 1.0 / 1000);
            String fileSizeResult;
            if (fileSizeNumber > 2000)
                fileSizeResult = String.format(Locale.ROOT, "%.2f MB", fileSizeNumber * 1.0 / 1000);
            else
                fileSizeResult = String.format(Locale.ROOT, "%d KB", fileSizeNumber);

            if (detailed) {
                holder.name.setText("Tên: " + currentFile.getName());
                holder.createdDate.setText("Ngày tạo: " + sdf.format(currentFile.lastModified()));
                holder.size.setText("Dung lượng: " + fileSizeResult);
                if (bitmap != null)
                    holder.dimensions.setText("Kích thước: " + bitmap.getWidth() + "x" + bitmap.getHeight());
                holder.location.setText("Vị trí: " + images.get(position));
            }
            holder.img.setImageBitmap(bitmap);
        }
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

            itemView.setOnClickListener(view -> Toast.makeText(view.getContext(), images.get(getAdapterPosition()) + "", Toast.LENGTH_LONG).show());
        }
    }
}