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

import com.example.memorymoblieapp.Brightness;
import com.example.memorymoblieapp.R;

import java.util.ArrayList;


public class BrightnessRecViewAdapter extends RecyclerView.Adapter<BrightnessRecViewAdapter.ViewHolder> {
    private ArrayList<Brightness> brightnesses = new ArrayList<>();
    private Context context;

    public BrightnessRecViewAdapter(Context context) {
        this.context = context;
    }


    @NonNull
    @Override
    public BrightnessRecViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.brightness_list_item, parent, false);
        BrightnessRecViewAdapter.ViewHolder holder = new BrightnessRecViewAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BrightnessRecViewAdapter.ViewHolder holder, int position) {
        holder.brightnessName.setText(brightnesses.get(position).getName());
        String name = brightnesses.get(position).getName();
        holder.brightnessItemViewEdit.setImageResource(brightnesses.get(position).getBrightnessResource());
        holder.parent_list_brightness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, name, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return brightnesses.size();
    }

    public void setBrightnesses(ArrayList<Brightness> brightnesses) {
        this.brightnesses = brightnesses;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView brightnessName;
        private ImageView brightnessItemViewEdit;
        private CardView parent_list_brightness;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            brightnessName = itemView.findViewById(R.id.brightnessName);
            parent_list_brightness = itemView.findViewById(R.id.parent_list_brightness);
            brightnessItemViewEdit = itemView.findViewById(R.id.brightnessItemViewEdit);
        }
    }
}
