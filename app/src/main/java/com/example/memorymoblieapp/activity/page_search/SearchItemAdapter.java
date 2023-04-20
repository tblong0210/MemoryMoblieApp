package com.example.memorymoblieapp.activity.page_search;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import com.example.memorymoblieapp.activity.MainActivity;
import com.example.memorymoblieapp.activity.page_image.ViewImage;
import com.example.memorymoblieapp.local_data_storage.DataLocalManager;
import com.example.memorymoblieapp.local_data_storage.KeyData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SearchItemAdapter extends RecyclerView.Adapter<SearchItemAdapter.ViewHolder> {

    private ArrayList<String> element;
    private Context context;
    private int type;

    public SearchItemAdapter(int type) {
        this.type = type;
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
        if (element != null && !element.isEmpty()) {
            String nameImage = element.get(position).substring(element.get(position).lastIndexOf('/') + 1);
            holder.textView.setText(nameImage);

            if (type == R.id.name) {
                holder.parent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, ViewImage.class);
                        intent.putExtra(KeyData.PATH_CURRENT_IMAGE_VIEW.getKey(), element.get(position));
                        context.startActivity(intent);

                        Set<String> history = DataLocalManager.getSetList(KeyData.HISTORY_SEARCH_IMAGE.getKey());
                        if (history == null) history = new HashSet<>();
                        history.add(element.get(position));
                        DataLocalManager.saveSetStringData(KeyData.HISTORY_SEARCH_IMAGE.getKey(), history);
                    }
                });

                Glide.with(context).load(element.get(position)).override(300, 300) // giảm kích thước ảnh xuống 300x300
                        .centerCrop() // cắt ảnh với kích thước mới
                        .into(holder.imageView);
            } else {
                holder.parent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(), MainActivity.class);
                        intent.putExtra(KeyData.CURRENT_FRAGMENT.getKey(), R.string.view_album);
                        intent.putExtra("data", element.get(position));
                        view.getContext().startActivity(intent);

                        Set<String> history = DataLocalManager.getSetList(KeyData.HISTORY_SEARCH_ALBUM.getKey());
                        if (history == null) history = new HashSet<>();
                        history.add(element.get(position));
                        DataLocalManager.saveSetStringData(KeyData.HISTORY_SEARCH_ALBUM.getKey(), history);
                    }
                });

                if (DataLocalManager.getBooleanData(KeyData.DARK_MODE.getKey()))
                    holder.imageView.setImageResource(R.drawable.ic_folder_white);
                else holder.imageView.setImageResource(R.drawable.ic_folder);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (element == null) return 0;
        return element.size();
    }

    public void setElement(ArrayList<String> element) {
        this.element = element;
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
