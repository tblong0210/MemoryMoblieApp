package com.example.memorymoblieapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memorymoblieapp.R;

import java.util.ArrayList;

public class ZipFileAdapter extends RecyclerView.Adapter<ZipFileAdapter.ViewHolder> {
    ArrayList<String> zipList;
    Context context;

    public ZipFileAdapter(ArrayList<String> zipList, Context context) {
        this.zipList = zipList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.zip_file_item, parent, false);
        return new ZipFileAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtZipName.setText(zipList.get(position));
    }

    @Override
    public int getItemCount() {
        return zipList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtZipName;
        ImageView ivDelete;
        ImageView ivShare;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtZipName = itemView.findViewById(R.id.txtZipName);
            ivDelete = itemView.findViewById(R.id.ivDelete);
            ivShare = itemView.findViewById(R.id.ivShare);
        }
    }
}
