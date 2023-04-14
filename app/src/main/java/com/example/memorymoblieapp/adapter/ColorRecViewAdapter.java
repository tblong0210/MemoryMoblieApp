package com.example.memorymoblieapp.adapter;

import android.content.ClipData;
import android.content.Context;
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

import java.util.ArrayList;

public class ColorRecViewAdapter extends RecyclerView.Adapter<ColorRecViewAdapter.ViewHolder> {
    private ArrayList<ColorClass> colors = new ArrayList<>();
    private Context context;
    private int ColorChosen;
    public ColorRecViewAdapter(Context context) {
        this.context= context;
    }

    public int getColorChosen(){
        return ColorChosen;
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
        int colorChoose = colors.get(position).getColorType();
        holder.parent_list_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ColorChosen= colorChoose;
            }
        });

    }

    @Override
    public int getItemCount() {
        return colors.size();
    }

    public void setColors(ArrayList<ColorClass> colors) {
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
