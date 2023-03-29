package com.example.memorymoblieapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memorymoblieapp.R;
import com.example.memorymoblieapp.fragment.AlbumBlockFragment;
import com.example.memorymoblieapp.fragment.ImageFragment2;

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
//                Toast.makeText(itemView.getContext(), settings.get(getAdapterPosition()) + "", Toast.LENGTH_SHORT).show();

                switch (getAdapterPosition()) {
                    case 0:
                        Toast.makeText(itemView.getContext(), "Ngôn ngữ", Toast.LENGTH_SHORT).show();
                        break;

                    case 1:
                        Toast.makeText(itemView.getContext(), "Chế độ tối", Toast.LENGTH_SHORT).show();
                        break;

                    case 2:
                        AlbumBlockFragment albumBlockFragment = new AlbumBlockFragment();
                        AppCompatActivity activity = (AppCompatActivity) view.getContext();
                        FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame_layout_content, albumBlockFragment).commit();
                        fragmentTransaction.addToBackStack("more");
                        break;
                }
            });
        }
    }
}
