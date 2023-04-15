package com.example.memorymoblieapp.adapter;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memorymoblieapp.R;
import com.example.memorymoblieapp.obj.ColorClass;
import com.example.memorymoblieapp.obj.Sticker;

import java.util.ArrayList;

public class StickerRecViewAdapter extends RecyclerView.Adapter<StickerRecViewAdapter.ViewHolder> {
    private ArrayList<Sticker> stickers = new ArrayList<>();
    private Context context;
    private Bitmap StickerChosen;

    public StickerRecViewAdapter(Context context) {
        this.context = context;
    }

    public Bitmap getStickerChosen() {
        return StickerChosen;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sticker_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.stickerItemViewEdit.setImageBitmap(stickers.get(position).getImageResource());
        String name = stickers.get(position).getName();
        Bitmap stickerChoose = stickers.get(position).getImageResource();
        holder.parent_list_sticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StickerChosen = stickerChoose;
            }
        });

    }

    @Override
    public int getItemCount() {
        return stickers.size();
    }

    public void setStickers(ArrayList<Sticker> stickers) {
        this.stickers = stickers;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView stickerItemViewEdit;
        private CardView parent_list_sticker;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            parent_list_sticker = itemView.findViewById(R.id.parent_list_sticker);
            stickerItemViewEdit = itemView.findViewById(R.id.stickerItemViewEdit);
        }
    }
}
