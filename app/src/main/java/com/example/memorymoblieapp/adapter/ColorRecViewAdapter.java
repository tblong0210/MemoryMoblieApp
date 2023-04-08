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
import com.example.memorymoblieapp.obj.Color;
import com.example.memorymoblieapp.obj.ColorClass;

import java.util.ArrayList;

public class ColorRecViewAdapter extends RecyclerView.Adapter<ColorRecViewAdapter.ViewHolder> {
    private ArrayList<ColorClass> colors = new ArrayList<>();
    private Context context;
    public ColorRecViewAdapter(Context context) {
        this.context= context;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.paint_list_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.colorItemViewEdit.setBackgroundColor(colors.get(position).getColorType());
        String name = colors.get(position).getName();
        holder.parent_list_color.setOnClickListener(new View.OnClickListener() {
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
        return colors.size();
    }

    public void setFilters(ArrayList<ColorClass> colors) {
        this.colors = colors;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView colorItemViewEdit;
        private CardView parent_list_color;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            parent_list_color=itemView.findViewById(R.id.parent_list_color);
            colorItemViewEdit = itemView.findViewById(R.id.colorItemViewEdit);
        }
    }
}
