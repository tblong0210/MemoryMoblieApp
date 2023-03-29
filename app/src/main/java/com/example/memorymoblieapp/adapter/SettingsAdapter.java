package com.example.memorymoblieapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memorymoblieapp.R;

import java.util.ArrayList;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.ViewHolder> {
    static ArrayList<String> settings;
    Context context;

    public SettingsAdapter(ArrayList<String> settings, Context context) {
        SettingsAdapter.settings = settings;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.settings_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtSettingsName.setText(settings.get(position));
    }

    @Override
    public int getItemCount() {
        return settings.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtSettingsName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSettingsName = itemView.findViewById(R.id.txtSettingsName);

            itemView.setOnClickListener(view -> {
                Toast.makeText(itemView.getContext(), settings.get(getAdapterPosition()) + "", Toast.LENGTH_SHORT).show();
            });
        }
    }
}
