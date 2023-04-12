package com.example.memorymoblieapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memorymoblieapp.FilterItem;
import com.example.memorymoblieapp.R;
import com.example.memorymoblieapp.view.ViewEdit;

import java.util.ArrayList;

public class FilterRecViewAdapter extends RecyclerView.Adapter<FilterRecViewAdapter.ViewHolder> {
    private ArrayList<FilterItem> filters = new ArrayList<>();
    private final Context context;
    private Bitmap imageFilterView;

    public FilterRecViewAdapter(Context context, Bitmap bitmap) {
        this.imageFilterView = bitmap;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    public void setImageFilterView(Bitmap bitmap){
        this.imageFilterView = bitmap;
        notifyDataSetChanged();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.filterName.setText(filters.get(position).getName());
        Log.d("ten filter", "onBindViewHolder: " + imageFilterView.toString());

        holder.filterItemViewEdit.setImageBitmap(imageFilterView);
        holder.filterItemViewEdit.setLayoutParams(new LinearLayout.LayoutParams(100, 100));

        if (filters.get(position).getActived())
            holder.filterItemViewEdit.setBackground(context.getDrawable(R.drawable.bg_solid_seleted));
        else
            holder.filterItemViewEdit.setBackground(context.getDrawable(R.drawable.bg_shape_conner_10_nocolor));

        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.set(filters.get(position).getColorMatrix());
        ColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
        holder.filterItemViewEdit.setColorFilter(filter);

        holder.parent_list_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ViewEdit) view.getContext()).setImageViewEdit(holder.filterItemViewEdit.getDrawable());
                for (int i = 0; i < filters.size(); i++) {
                    if (i == position) {
                        filters.get(i).setActivated(true);
                    } else {
                        filters.get(i).setActivated(false);
                    }
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return filters.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFilters(ArrayList<FilterItem> filters) {
        this.filters = filters;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView filterName;
        private ImageFilterView filterItemViewEdit;
        private LinearLayout parent_list_filter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            filterName = itemView.findViewById(R.id.filterName);
            parent_list_filter = itemView.findViewById(R.id.parent_list_filter);
            filterItemViewEdit = itemView.findViewById(R.id.filterItemViewEdit);
        }
    }
}
