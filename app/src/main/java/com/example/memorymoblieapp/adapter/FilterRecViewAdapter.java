package com.example.memorymoblieapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memorymoblieapp.Filter;
import com.example.memorymoblieapp.R;

import java.util.ArrayList;

public class FilterRecViewAdapter extends RecyclerView.Adapter<FilterRecViewAdapter.ViewHolder> {
    private ArrayList<Filter> filters = new ArrayList<>();
    private Context context;
    public FilterRecViewAdapter(Context context) {
        this.context= context;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_list_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.filterName.setText(filters.get(position).getName());
        String name = filters.get(position).getName();
        holder.filterItemViewEdit.setImageResource(filters.get(position).getFilterResource());
        holder.parent_list_filter.setOnClickListener(new View.OnClickListener() {
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
        return filters.size();
    }

    public void setFilters(ArrayList<Filter> filters) {
        this.filters = filters;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView filterName;
        private ImageView filterItemViewEdit;
        private CardView parent_list_filter;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            filterName= itemView.findViewById(R.id.filterName);
            parent_list_filter=itemView.findViewById(R.id.parent_list_filter);
            filterItemViewEdit = itemView.findViewById(R.id.filterItemViewEdit);
        }
    }
}
