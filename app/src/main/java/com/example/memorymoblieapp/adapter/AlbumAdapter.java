package com.example.memorymoblieapp.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memorymoblieapp.R;
import com.example.memorymoblieapp.obj.Album;

import java.util.ArrayList;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {
    ArrayList<Album> albums;
    Context context;

    public AlbumAdapter(ArrayList<Album> albums, Context context) {
        this.albums = albums;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.album_row, parent, false);
        return new ViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(albums.get(position).getName());
        holder.quantity.setText(Integer.toString(albums.get(position).getQuantity()) + " ảnh");
        holder.img.setImageResource(Integer.parseInt(Integer.toString(albums.get(position).getImg())));
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView quantity;
        ImageView img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.txtAlbumName);
            quantity = (TextView) itemView.findViewById(R.id.txtImgQuantity);
            img = (ImageView) itemView.findViewById(R.id.ivAlbum);
        }
    }
}
